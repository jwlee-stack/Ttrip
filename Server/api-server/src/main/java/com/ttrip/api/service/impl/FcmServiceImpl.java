package com.ttrip.api.service.impl;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import com.ttrip.api.config.webSocket.ChatHandler;
import com.ttrip.api.dto.DataResDto;
import com.ttrip.api.dto.chatroomDto.ChatMakerReqDto;
import com.ttrip.api.dto.fcmMessageDto.FcmMessageReqDto;
import com.ttrip.api.dto.fcmMessageDto.FcmMessageResDto;
import com.ttrip.api.service.ChatService;
import com.ttrip.api.service.FcmService;
import com.ttrip.core.entity.matchHistory.MatchHistory;
import com.ttrip.core.entity.member.Member;
import com.ttrip.core.repository.article.ArticleRepository;
import com.ttrip.core.repository.matchHistory.MatchHistoryRepository;
import com.ttrip.core.repository.member.MemberRepository;
import com.ttrip.core.utils.ErrorMessageEnum;
import lombok.RequiredArgsConstructor;
import okhttp3.*;
import org.apache.hc.core5.http.HttpHeaders;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@Component
@RequiredArgsConstructor
public class FcmServiceImpl implements FcmService {
    private final MemberRepository memberRepository;
    public final ObjectMapper objectMapper;
    private final Logger logger = LogManager.getLogger(ChatHandler.class);
    private final MatchHistoryRepository matchHistoryRepository;
    private final ArticleRepository articleRepository;
    private final ChatService chatService;
    private final String API_URL = "https://fcm.googleapis.com/v1/projects/ttrip-6f310/messages:send";

    //    FCM에 push 요청을 보낼 때 인증을 위해 Header에 포함시킬 AccessToken 생성
    private String getAccessToken() throws IOException {
        String firebaseConfigPath = "firebase/Ttrip-firebase-adminsdk.json";

        GoogleCredentials googleCredentials = GoogleCredentials
                .fromStream(new ClassPathResource(firebaseConfigPath).getInputStream())
                .createScoped(List.of("https://www.googleapis.com/auth/cloud-platform"));

        googleCredentials.refreshIfExpired();
        return googleCredentials.getAccessToken().getTokenValue();
    }

    @Override
    public DataResDto<?> sendMessageTo(Member member, FcmMessageReqDto fcmMessageReqDto) throws IOException {
        String message = makeMessage(member, fcmMessageReqDto);

        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(message,
                MediaType.get("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(API_URL)
                .post(requestBody)
                .addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken())
                .addHeader(HttpHeaders.CONTENT_TYPE, "application/json; UTF-8")
                .build();

        Response response = client.newCall(request).execute();
        logger.info("메세지 발송 요청 성공");

        return DataResDto.builder().message("FCM 메세지 발송성공했습니다.").data(true).build();
    }

    //추후에 batch로 옮길 예정
    @Override
    public void rate() throws IOException {
        // 평가하지 않았지만 게시글정보가 있으면 -> 라이브에서 매칭한게 아닌 매칭기록만
        List<MatchHistory> matchHistoryList = matchHistoryRepository.findByRateIsNullAndArticleIsNotNull();
        for (MatchHistory matchHistory : matchHistoryList) {
            //마감일로 하루 지났는데도 평가안됬으면
            if (ChronoUnit.DAYS.between(matchHistory.getArticle().getEndDate(), LocalDate.now()) > 0) {
                sendMessageTo(matchHistory.getEvaluator(), FcmMessageReqDto.builder()
                        .type(4)
                        .targetUuid(matchHistory.getEvaluator().getMemberUuid())
                        .extraId(matchHistory.getMatchHistoryId().toString())
                        .extraData(" ")
                        .build());
            }
        }
        logger.info("fcm 발송 성공: {}개 발송 요청", matchHistoryList.size());
    }

    @Override
    public void liveRate() throws IOException {
        // 평가하지 않았지만 게시글정보가 있으면 -> 라이브에서 매칭한게 아닌 매칭기록만
        List<MatchHistory> matchHistoryList = matchHistoryRepository.findByRateIsNullAndArticleIsNull();
        for (MatchHistory matchHistory : matchHistoryList) {
            //마감일로 하루 지났는데도 평가안됬으면
            System.out.println(ChronoUnit.HOURS.between(matchHistory.getCreatedAt(), LocalDateTime.now()) % 24);
            if (ChronoUnit.HOURS.between(matchHistory.getCreatedAt(), LocalDateTime.now()) % 24 == 0) {
                sendMessageTo(matchHistory.getEvaluator(), FcmMessageReqDto.builder()
                        .type(4)
                        .targetUuid(matchHistory.getEvaluator().getMemberUuid())
                        .extraId(matchHistory.getMatchHistoryId().toString())
                        .extraData(" ")
                        .build());
            }
        }
        logger.info("fcm 발송 성공: {}개 발송 요청", matchHistoryList.size());
    }

    //FCM 메세지를 만들어요 0: Live매칭 요청  1: 매칭요청 결과 2:article 3: Chat 4:매칭 평가 type으로 나눠서 사용
    private String makeMessage(Member member, FcmMessageReqDto fcmMessageReqDto) throws JsonParseException, JsonProcessingException {
        Member targetMember = memberRepository.findByMemberUuid(fcmMessageReqDto.getTargetUuid())
                .orElseThrow(() -> new NoSuchElementException(ErrorMessageEnum.USER_NOT_EXIST.getMessage()));
        String targetToken = targetMember.getFcmToken();
        //토큰이 있는지 확인
        if (targetToken == null) {
            new NoSuchElementException((ErrorMessageEnum.FCM_TOKEN_NOT_EXIST.getMessage()));
        }

        //string, string 말고 다른 타입도 되나요?
        Map<String, String> data = new HashMap<>();

        if (0 == fcmMessageReqDto.getType()) {
            //Live
            //다 스트링으로
            data.put("type", "0");
            data.put("nickName", member.getNickname());
            data.put("memberUuid", member.getMemberUuid().toString());

            FcmMessageResDto fcmMessage = FcmMessageResDto.builder()
                    .message(FcmMessageResDto.Message.builder()
                            .token(targetToken)
                            .notification(FcmMessageResDto.Notification.builder()
                                    .title("TTrIP")
                                    .body(member.getNickname() + "님의 매칭 요청이 도착했어요!")
                                    .build()
                            )
                            .data(data).build())
                    .validate_only(false).build();
            logger.info("live 매칭 요청fcm");
            return objectMapper.writeValueAsString(fcmMessage);
        } else if (1 == fcmMessageReqDto.getType()) {
            //Article
            //다 스트링으로
            data.put("type", "1");
            data.put("nickName", member.getNickname());
            data.put("memberUuid", member.getMemberUuid().toString());
            data.put("result", fcmMessageReqDto.getExtraData());

            FcmMessageResDto fcmMessage = FcmMessageResDto.builder()
                    .message(FcmMessageResDto.Message.builder()
                            .token(targetToken)
                            .notification(FcmMessageResDto.Notification.builder()
                                    .title("TTrIP")
                                    .body("true".equals(fcmMessageReqDto.getExtraData()) ?
                                            member.getNickname() + "님이 매칭 요청을 수락했어요!"
                                            : member.getNickname() + "님이 매칭 요청을 거절했어요!")
                                    .build()
                            )
                            .data(data).build())
                    .validate_only(false).build();
            if ("true".equals(fcmMessageReqDto.getExtraData())) {
                //매칭수락하면 매칭 이력 생성
                matchHistoryRepository.save(MatchHistory.builder()
                        .evaluator(targetMember)
                        .evaluated(member)
                        .build());
                matchHistoryRepository.save(MatchHistory.builder()
                        .evaluator(member)
                        .evaluated(targetMember)
                        .build());
                //채팅 추가
                chatService.makeChat(ChatMakerReqDto.builder()
                        //제목 빈 삭제된 게시글 참조
                        .articleId(18)
                        .opponentUserUuid(targetMember.getMemberUuid().toString())
                        .build(), member);
                logger.info("live 매칭 요청 수락!");
            } else {
                logger.info("live 매칭 요청 거절!");
            }

            return objectMapper.writeValueAsString(fcmMessage);

        } else if (2 == fcmMessageReqDto.getType()) {
            //Article
            //다 스트링으로
            data.put("type", "2");
            data.put("nickName", member.getNickname());
            data.put("memberUuid", member.getMemberUuid().toString());
            data.put("articleId", fcmMessageReqDto.getExtraId());

            FcmMessageResDto fcmMessage = FcmMessageResDto.builder()
                    .message(FcmMessageResDto.Message.builder()
                            .token(targetToken)
                            .notification(FcmMessageResDto.Notification.builder()
                                    .title("TTrIP")
                                    .body(member.getNickname() + "님의 게시글에 매칭 요청이 도착했어요!")
                                    .build()
                            )
                            .data(data).build())
                    .validate_only(false).build();
            logger.info(targetMember.getNickname() + "님의 게시글에 매칭 요청이 도착했어요!");

            return objectMapper.writeValueAsString(fcmMessage);

        } else if (3 == fcmMessageReqDto.getType()) {
            //Chat
            //다 스트링으로
            data.put("type", "3");
            data.put("nickName", member.getNickname());
            data.put("memberUuid", member.getMemberUuid().toString());
            data.put("chatroomId", fcmMessageReqDto.getExtraId());

            FcmMessageResDto fcmMessage = FcmMessageResDto.builder()
                    .message(FcmMessageResDto.Message.builder()
                            .token(targetToken)
                            .notification(FcmMessageResDto.Notification.builder()
                                    .title("TTrIP")
                                    .body(member.getNickname() + ": " + (fcmMessageReqDto.getExtraData().length() > 20 ?
                                            fcmMessageReqDto.getExtraData().substring(0, 19)
                                            :
                                            fcmMessageReqDto.getExtraData())) // 20자 제한
                                    .build()
                            )
                            .data(data).build())
                    .validate_only(false).build();

            logger.info("chat fcm " + member.getNickname() + ": " + (fcmMessageReqDto.getExtraData().length() > 20 ?
                    fcmMessageReqDto.getExtraData().substring(0, 19)
                    :
                    fcmMessageReqDto.getExtraData()));

            return objectMapper.writeValueAsString(fcmMessage);

        } else {
            //매칭 평가
            //다 스트링으로
            data.put("type", "4");
            data.put("nickName", member.getNickname());
            data.put("memberUuid", member.getMemberUuid().toString());
            data.put("matchHistoryId", fcmMessageReqDto.getExtraId());

            FcmMessageResDto fcmMessage = FcmMessageResDto.builder()
                    .message(FcmMessageResDto.Message.builder()
                            .token(targetToken)
                            .notification(FcmMessageResDto.Notification.builder()
                                    .title("TTrIP")
                                    .body(member.getNickname() + "님과의 동행은 만족스러웠나요?")
                                    .build()
                            )
                            .data(data).build())
                    .validate_only(false).build();

            logger.info("매칭 평가 fcm" + member.getNickname() + "님과의 동행은 만족스러웠나요?");

            return objectMapper.writeValueAsString(fcmMessage);

        }
    }
}
