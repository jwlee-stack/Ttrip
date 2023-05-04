package com.ttrip.api.service.impl;

import com.ttrip.api.config.jwt.TokenProvider;
import com.ttrip.api.dto.DataResDto;
import com.ttrip.api.dto.memberDto.memberReqDto.MemberLoginReqDto;
import com.ttrip.api.dto.memberDto.memberReqDto.MemberReportReqDto;
import com.ttrip.api.dto.memberDto.memberReqDto.MemberSignupReqDto;
import com.ttrip.api.dto.memberDto.memberReqDto.MemberUpdateReqDto;
import com.ttrip.api.dto.memberDto.memberResDto.MemberCheckNicknameResDto;
import com.ttrip.api.dto.memberDto.memberResDto.MemberLoginResDto;
import com.ttrip.api.dto.memberDto.memberResDto.MemberResDto;
import com.ttrip.api.dto.mypageDto.mypageReqDto.InfoUpdateReqDto;
import com.ttrip.api.dto.mypageDto.mypageReqDto.ProfileUpdateReqDto;
import com.ttrip.api.dto.surveyDto.surverReqDto.SurveyReqDto;
import com.ttrip.api.dto.surveyDto.surveyResDto.SurveyResDto;
import com.ttrip.api.dto.tokenDto.TokenDto;
import com.ttrip.api.dto.tokenDto.tokenReqDto.TokenReqDto;
import com.ttrip.api.exception.BadRequestException;
import com.ttrip.api.service.MemberService;
import com.ttrip.api.service.MypageService;
import com.ttrip.core.entity.blacklist.Blacklist;
import com.ttrip.core.entity.member.Member;
import com.ttrip.core.entity.refreshToken.RefreshToken;
import com.ttrip.core.entity.survey.Survey;
import com.ttrip.core.repository.blacklist.BlacklistRepository;
import com.ttrip.core.repository.liveRedisDao.LiveRedisDao;
import com.ttrip.core.repository.member.MemberRepository;
import com.ttrip.core.repository.refreshToken.RefreshTokenRepository;
import com.ttrip.core.repository.survey.SurveyRepository;
import com.ttrip.core.utils.ErrorMessageEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final SurveyRepository surveyRepository;
    private final BlacklistRepository blacklistRepository;
    private final MypageService mypageService;
    private final LiveRedisDao liveRedisDao;

    @Override
    @Transactional
    public DataResDto<?> signup(MemberSignupReqDto memberSignupReqDto) {
        log.info("멤버 회원 가입 요청: {}",memberSignupReqDto.toString());
        //이미 가입한 전화번호?
        if (memberRepository.existsByPhoneNumber(memberSignupReqDto.getPhoneNumber()))
            //이미 가입한 유저입니다.
            throw new BadRequestException(ErrorMessageEnum.EXISTS_ACCOUNT.getMessage());

        //멤버 생성
        Member member = memberSignupReqDto.toMember(passwordEncoder);
        log.info("멤버 생성: {}",member);
        log.info("응답값으로 변환: {}",MemberResDto.toBuild(member).toString());
        //DB에 저장
        try {
            memberRepository.save(member);
            return DataResDto.builder()
                    .message("회원가입이 완료되었습니다.")
                    .data(MemberResDto.toBuild(member))
                    .build();
        } catch (Exception e) {    //회원가입 실패
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

        Member member = memberRepository.findByPhoneNumber(memberLoginReqDto.getPhoneNumber()).get();

        // 4. RefreshToken 저장
        RefreshToken refreshToken = RefreshToken.builder()
                .key(member.getMemberUuid().toString()) //rt_key=uuid
                .value(tokenDto.getRefreshToken()) //rt_value=refresh token
                .build();
        refreshTokenRepository.save(refreshToken);

        // 5. 유저 설문 정보 캐싱
        if (Objects.isNull(liveRedisDao.getSurveyCache(member.getMemberUuid().toString())))
            liveRedisDao.saveSurveyCache(member.getMemberUuid().toString(), member.getSurvey().toVector());

        // 6. 멤버(회원가입), 리프래시 토큰 반환
        return DataResDto.builder()
                .message("토큰 생성 완료")
                .data(MemberLoginResDto.toBuild(member, tokenDto))
                .build();
    }

    @Override
    public DataResDto<?> logout(MemberDetails memberDetails) {
        //memberDetails를 통해, 내 UUID 획득
        String myUuid = memberDetails.getMember().getMemberUuid().toString();
        //내 UUID로 리프래시 토큰 서칭
        RefreshToken refreshToken = refreshTokenRepository.findByKey(myUuid).get();
        try {
            //해당 리프래시 토큰 삭제
            refreshTokenRepository.delete(refreshToken);
            return DataResDto.builder()
                    .message("로그아웃되었습니다.")
                    .data(true)
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            throw new NoSuchElementException(ErrorMessageEnum.FAIL_TO_LOGOUT.getMessage());
        }
    }

    @Override
    @Transactional
    public DataResDto<?> reissue(TokenReqDto tokenReqDto, UUID uuid) {
        // 1. Refresh Token 검증
        if (!tokenProvider.validateToken(tokenReqDto.getRefreshToken())) {
            throw new RuntimeException("Refresh Token 이 유효하지 않습니다.");
        }

        // 2. Access Token 에서 authentication 가져오기
        Authentication authentication = tokenProvider.getAuthentication(tokenReqDto.getAccessToken());

        // 3. 저장소에서 Member UUID 를 기반으로 Refresh Token 값 가져옴
        RefreshToken refreshToken = refreshTokenRepository.findByKey(uuid.toString())
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
    public DataResDto<?> setInfo(MemberUpdateReqDto memberUpdateReqDto, MemberDetails memberDetails) {
        //fcm 토큰 변경//
        memberDetails.getMember().setFcmToken(memberUpdateReqDto.getFcmToken().isEmpty() ? memberDetails.getMember().getFcmToken() : memberUpdateReqDto.getFcmToken());

        //이미지 변경//
        ProfileUpdateReqDto profileUpdateReqDto=MemberUpdateReqDto.toProfileUpdateReq(memberUpdateReqDto);
        mypageService.updateProfileAndMarkerImg(profileUpdateReqDto, memberDetails);

        //닉네임, 성별, 나이, 인트로 변경//
        InfoUpdateReqDto infoUpdateReqDto=MemberUpdateReqDto.toInfoUpdateReq(memberUpdateReqDto);
        mypageService.updateMember(infoUpdateReqDto,memberDetails);

        return DataResDto.builder()
                .message("회원 정보가 업데이트되었습니다.")
                .data(MemberResDto.toBuild(memberDetails.getMember()))
                .build();
    }

    @Override
    public DataResDto<?> checkNickname(String nickname) {
        if (memberRepository.existsByNickname(nickname)) {
            return DataResDto.builder()
                    .message("존재하는 닉네임입니다.")
                    .data(MemberCheckNicknameResDto.builder().isExist(true).build())
                    .build();
        } else {
            return DataResDto.builder()
                    .message("사용 가능한 닉네임입니다.")
                    .data(MemberCheckNicknameResDto.builder().isExist(false).build())
                    .build();
        }

    }

    @Override
    public DataResDto<?> updateSurvey(SurveyReqDto surveyReqDto, MemberDetails memberDetails) {
        Member member = memberDetails.getMember();

        Survey survey;
        if(!surveyRepository.existsByMember(member))
            survey=Survey.builder().member(member).build();
        else
            survey = surveyRepository.findByMember(member).get();
        survey.setPreferNatureThanCity(surveyReqDto.getPreferNatureThanCity());
        survey.setPreferPlan(surveyReqDto.getPreferPlan());
        survey.setPreferDirectFlight(surveyReqDto.getPreferDirectFlight());
        survey.setPreferCheapHotelThanComfort(surveyReqDto.getPreferCheapHotelThanComfort());
        survey.setPreferGoodFood(surveyReqDto.getPreferGoodFood());
        survey.setPreferCheapTraffic(surveyReqDto.getPreferCheapTraffic());
        survey.setPreferPersonalBudget(surveyReqDto.getPreferPersonalBudget());
        survey.setPreferTightSchedule(surveyReqDto.getPreferTightSchedule());
        survey.setPreferShoppingThanTour(surveyReqDto.getPreferShoppingThanTour());
        surveyRepository.save(survey);

        return DataResDto.builder()
                .message("회원의 여행 취향이 저장되었습니다.")
                .data(SurveyResDto.toBuild(survey))
                .build();
    }

    @Override
    public DataResDto<?> viewMemberInfo(String nickname) {
        if (!memberRepository.existsByNickname(nickname))
            throw new BadRequestException(ErrorMessageEnum.USER_NOT_EXIST.getMessage());

        Member member = memberRepository.findByNickname(nickname).get();
        return DataResDto.builder()
                .message("회원 프로필이 조회되었습니다.")
                .data(MemberResDto.toBuild(member))
                .build();
    }

    @Override
    public DataResDto<?> reportMember(MemberReportReqDto memberReportReqDto, MemberDetails memberDetails) {
        Member member = memberRepository.findByNickname(memberReportReqDto.getReportedNickname()).get();
        Blacklist blacklist = Blacklist.builder()
                .reporterId(memberDetails.getMember().getMemberId())
                .reportContext(memberReportReqDto.getReportContext())
                .member(member)
                .build();
        blacklistRepository.save(blacklist);

        return DataResDto.builder()
                .message("신고가 접수되었습니다.")
                .data(true)
                .build();
    }

}
