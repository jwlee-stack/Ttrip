package com.ttrip.api.service.impl;

import com.ttrip.api.config.jwt.TokenProvider;
import com.ttrip.api.dto.DataResDto;
import com.ttrip.api.dto.memberDto.memberResDto.MemberCheckNicknameResDto;
import com.ttrip.api.dto.memberDto.memberResDto.MemberLoginResDto;
import com.ttrip.api.dto.memberDto.memberResDto.MemberResDto;
import com.ttrip.api.dto.memberDto.memberReqDto.MemberUpdateReqDto;
import com.ttrip.api.dto.tokenDto.TokenDto;
import com.ttrip.api.dto.tokenDto.tokenReqDto.TokenReqDto;
import com.ttrip.api.dto.memberDto.memberReqDto.MemberLoginReqDto;
import com.ttrip.api.dto.memberDto.memberReqDto.MemberSignupReqDto;
import com.ttrip.api.exception.BadRequestException;
import com.ttrip.core.entity.refreshToken.RefreshToken;
import com.ttrip.core.repository.refreshToken.RefreshTokenRepository;
import com.ttrip.api.service.MemberService;
import com.ttrip.core.entity.member.Member;
import com.ttrip.core.repository.member.MemberRepository;
import com.ttrip.core.utils.ErrorMessageEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Service
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    @Transactional
    public DataResDto<?> signup(MemberSignupReqDto memberSignupReqDto)
    {
        //이미 가입한 전화번호?
        if(memberRepository.existsByPhoneNumber(memberSignupReqDto.getPhoneNumber()))
            //이미 가입한 유저입니다.
            throw new BadRequestException(ErrorMessageEnum.EXISTS_ACCOUNT.getMessage());

        //멤버 생성
        Member member= memberSignupReqDto.toMember(passwordEncoder);

        //DB에 저장
        try
        {
            memberRepository.save(member);
            return DataResDto.builder()
                    .message("회원가입이 완료되었습니다.")
                    .data(MemberResDto.toBuild(member))
                    .build();
        }
        catch (Exception e) {    //회원가입 실패
            throw new NoSuchElementException(ErrorMessageEnum.FAIL_TO_SIGNUP.getMessage());
        }
    }
    @Override
    @Transactional
    public DataResDto<?> login(MemberLoginReqDto memberLoginReqDto) {

        // 1. Login ID/PW 를 기반으로 AuthenticationToken 생성
        UsernamePasswordAuthenticationToken authenticationToken = memberLoginReqDto.toAuthentication();

        // 2. 실제로 검증 (사용자 비밀번호 체크) 이 이루어지는 부분
        //    authenticate 메서드가 실행이 될 때 CustomUserDetailsService 에서 만들었던 loadUserByUsername 메서드가 실행됨
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

        Member member=memberRepository.findByPhoneNumber(memberLoginReqDto.getPhoneNumber()).get();

        // 4. RefreshToken 저장
        RefreshToken refreshToken = RefreshToken.builder()
                .key(member.getMemberUuid().toString()) //rt_key=uuid
                .value(tokenDto.getRefreshToken()) //rt_value=refresh token
                .build();

        refreshTokenRepository.save(refreshToken);

        // 5. 멤버(회원가입), 리프래시 토큰 반환

        return DataResDto.builder()
                .message("토큰 생성 완료")
                .data(MemberLoginResDto.toBuild(member,tokenDto))
                .build();
    }
    @Override
    public DataResDto<?> logout(MemberDetails memberDetails) {
        //memberDetails를 통해, 내 UUID 획득
        String myUuid=memberDetails.getMember().getMemberUuid().toString();
        //내 UUID로 리프래시 토큰 서칭
        RefreshToken refreshToken=refreshTokenRepository.findByKey(myUuid).get();
        try{
            //해당 리프래시 토큰 삭제
            refreshTokenRepository.delete(refreshToken);
            return DataResDto.builder()
                    .message("로그아웃되었습니다.")
                    .data(true)
                    .build();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new NoSuchElementException(ErrorMessageEnum.FAIL_TO_LOGOUT.getMessage());
        }
    }

    @Override
    @Transactional
    public DataResDto<?> reissue(TokenReqDto tokenReqDto) {
        // 1. Refresh Token 검증
        if (!tokenProvider.validateToken(tokenReqDto.getRefreshToken())) {
            throw new RuntimeException("Refresh Token 이 유효하지 않습니다.");
        }

        // 2. Access Token 에서 Member ID 가져오기
        Authentication authentication = tokenProvider.getAuthentication(tokenReqDto.getAccessToken());

        // 3. 저장소에서 Member ID 를 기반으로 Refresh Token 값 가져옴
        RefreshToken refreshToken = refreshTokenRepository.findByKey(authentication.getName())
                .orElseThrow(() -> new RuntimeException("로그아웃 된 사용자입니다."));

        // 4. Refresh Token 일치하는지 검사
        if (!refreshToken.getValue().equals(tokenReqDto.getRefreshToken())) {
            throw new RuntimeException("토큰의 유저 정보가 일치하지 않습니다.");
        }

        // 5. 새로운 토큰 생성
        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

        // 6. 저장소 정보 업데이트
        RefreshToken newRefreshToken = refreshToken.updateValue(tokenDto.getRefreshToken());
        refreshTokenRepository.save(newRefreshToken);

        // 토큰 발급
        return DataResDto.builder()
                .message("토큰 재생성 완료")
                .data(tokenDto)
                .build();
    }

    @Override
    @Transactional
    public DataResDto<?> updateMember(MemberUpdateReqDto memberUpdateReqDto, MemberDetails memberDetails) throws IOException {
        //닉네임, 인트로, 성별, 생일, fcm 토큰 변경//
        Member member=memberDetails.getMember();
        member.setNickname(memberUpdateReqDto.getNickname().isEmpty()?member.getNickname():memberUpdateReqDto.getNickname());
        member.setIntro(memberUpdateReqDto.getIntro().isEmpty()?"20자 이내로 입력해주세요":memberUpdateReqDto.getIntro());
        member.setGender(memberUpdateReqDto.getGender().toString().isEmpty()?member.getGender():memberUpdateReqDto.getGender());
        member.setBirthday(memberUpdateReqDto.getBirthday().toString().isEmpty()?member.getBirthday():memberUpdateReqDto.getBirthday());
        member.setFcmToken(memberUpdateReqDto.getFcmToken().isEmpty()?member.getFcmToken():memberUpdateReqDto.getFcmToken());

        //프사 변경//
        String path=System.getProperty("user.dir")+"\\profileImg\\"; //공통 경로
        String customImgPath=path+member.getMemberUuid()+".png"; //변경 프사 경로
        String defaultImgPath=path+"default.png"; //디폴트 프사 경로
        File profileImg=new File(customImgPath); //파일 객체 생성

        //사용자가 프사를 설정했는지?
        if(!memberUpdateReqDto.getProfileImg().isEmpty()) //프사 설정함
        {
            //프사 저장
            memberUpdateReqDto.getProfileImg().transferTo(profileImg);
            member.setProfileImgPath(customImgPath);

        }
        else if(profileImg.delete()) //프사 설정 안 함 && 기존 프사 삭제
            member.setProfileImgPath(defaultImgPath); //디폴트 프사로 변경

        //마커 변경//
        path=System.getProperty("user.dir")+"\\markerImg\\"; //공통 경로
        customImgPath=path+member.getMemberUuid()+".png"; //변경 마커 경로
        defaultImgPath=path+"default.png"; //디폴트 마커 경로
        File markerImg=new File(customImgPath); //파일 객체 생성

        //사용자가 마커를 설정했는지?
        if(!memberUpdateReqDto.getMarkerImg().isEmpty()) //마커 설정함
        {
            //마커 저장
            memberUpdateReqDto.getMarkerImg().transferTo(markerImg);
            member.setProfileImgPath(customImgPath);

        }
        else if(markerImg.delete()) //마커 설정 안 함 && 기존 마커 삭제
            member.setProfileImgPath(defaultImgPath); //디폴트 마커로 변경

        memberRepository.save(member);

        return DataResDto.builder()
                .message("회원 정보가 업데이트되었습니다.")
                .data(MemberResDto.toBuild(member))
                .build();
    }

    public DataResDto<?> checkNickname(String nickname)
    {
        if(memberRepository.existsByNickname(nickname))
        {
            return DataResDto.builder()
                    .message("존재하는 닉네임입니다.")
                    .data(MemberCheckNicknameResDto.builder().isExist(true).build())
                    .build();
        }
        else
        {
            return DataResDto.builder()
                    .message("사용 가능한 닉네임입니다.")
                    .data(MemberCheckNicknameResDto.builder().isExist(false).build())
                    .build();
        }

    }
}
