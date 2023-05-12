package com.ttrip.api.service.impl;

import com.ttrip.api.dto.DataResDto;
import com.ttrip.api.dto.chatroomDto.ChatMakerReqDto;
import com.ttrip.api.dto.chatroomDto.ChatMessageResDto;
import com.ttrip.api.dto.chatroomDto.ExitChatReqDto;
import com.ttrip.api.dto.chatroomDto.GetChatroomResDto;
import com.ttrip.api.dto.matchDto.MakeMatchReqDto;
import com.ttrip.api.service.ChatService;
import com.ttrip.core.entity.article.Article;
import com.ttrip.core.entity.chatMember.ChatMember;
import com.ttrip.core.entity.chatmessage.ChatMessage;
import com.ttrip.core.entity.chatroom.Chatroom;
import com.ttrip.core.entity.matchHistory.MatchHistory;
import com.ttrip.core.entity.member.Member;
import com.ttrip.core.repository.article.ArticleRepository;
import com.ttrip.core.repository.chatMember.ChatMemberRepository;
import com.ttrip.core.repository.chatMessage.ChatMessageRepository;
import com.ttrip.core.repository.chatroom.ChatroomRepoistory;
import com.ttrip.core.repository.matchHistory.MatchHistoryRepository;
import com.ttrip.core.repository.member.MemberRepository;
import com.ttrip.core.utils.ErrorMessageEnum;
import com.ttrip.core.utils.EuclideanDistanceUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {
    private final ChatroomRepoistory chatroomRepoistory;
    private final ChatMemberRepository chatMemberRepository;
    private final MatchHistoryRepository matchHistoryRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final MemberRepository memberRepository;
    private final ArticleRepository articleRepository;
    private final EuclideanDistanceUtil euclideanDistanceUtil;

    @Override
    public DataResDto<?> getChatroomList(Member member) {
        List<GetChatroomResDto> chatroomList = new ArrayList<>();
        for (ChatMember chatMember : chatMemberRepository.findByMember(member)) {
            //대화상대를 조회
            Optional<ChatMember> optionalChatMember = chatMember.getChatroom().getChatMemberList().stream()
                    .filter(target -> !target.getMember().equals(member))
                    .findFirst();
            ChatMessage lastMessage = chatMessageRepository.findByChatroomId(chatMember.getChatroom().getChatRoomId())
                    .get(chatMessageRepository.findByChatroomId(chatMember.getChatroom().getChatRoomId()).size() - 1);
            //대화 상대가 없으면
            if (!optionalChatMember.isPresent()) {
                GetChatroomResDto getChatroomResDto = GetChatroomResDto.builder()
                        .chatroomId(chatMember.getChatroom().getChatRoomId())
                        .nickname("알수없음")
                        .memberUuid("알수없음")
                        .lastMessage(lastMessage.getText())
                        .updatedAt(lastMessage.getCreatedAt())
                        .imagePath(null)
                        .articleId(chatMember.getChatroom().getArticle().getArticleId())
                        .articleTitle(chatMember.getChatroom().getArticle().getTitle())
                        .similarity(0)
                        .status(chatMember.getChatroom().getArticle().getStatus())
                        .isMatch(chatMember.getChatroom().getStatus() == 'F')
                        .build();
                chatroomList.add(getChatroomResDto);
            } else {
                //상대가 있으면
                Member opponent = optionalChatMember.get().getMember();
                GetChatroomResDto getChatroomResDto = GetChatroomResDto.builder()
                        .chatroomId(chatMember.getChatroom().getChatRoomId())
                        .nickname(opponent.getNickname())
                        .memberUuid(opponent.getMemberUuid().toString())
                        .lastMessage(lastMessage.getText())
                        .updatedAt(lastMessage.getCreatedAt())
                        .imagePath(opponent.getProfileImgPath())
                        .articleId(chatMember.getChatroom().getArticle().getArticleId())
                        .articleTitle(chatMember.getChatroom().getArticle().getTitle())
                        .similarity(euclideanDistanceUtil.getMatchingRate(member.getSurvey(), opponent.getSurvey()))
                        .status(chatMember.getChatroom().getArticle().getStatus())
                        .isMatch(chatMember.getChatroom().getStatus() == 'F')
                        .build();
                chatroomList.add(getChatroomResDto);
            }
        }

        return DataResDto.builder().data(chatroomList).message("채팅목록을 조회했습니다.").build();
    }

    @Override
    @Transactional
    public DataResDto<?> removeChatroom(ExitChatReqDto exitChatReqDto, Member member) {
        Chatroom chatroom = chatroomRepoistory.findByChatRoomId(exitChatReqDto.getChatroomId())
                .orElseThrow(() -> new NoSuchElementException(ErrorMessageEnum.CHATMEMBER_NOT_EXIST.getMessage()));
        ChatMember chatMember = chatMemberRepository.findByMemberAndChatroom(member, chatroom)
                .orElseThrow(() -> new NoSuchElementException(ErrorMessageEnum.CHATMEMBER_NOT_EXIST.getMessage()));
        chatMemberRepository.delete(chatMember);
        //채팅방에서 모두 나가면 채팅방도 삭제
        if (chatroom.getChatMemberList().size() == 1) {
            chatroomRepoistory.delete(chatroom);
        }
        return DataResDto.builder().data(true).message("채팅방에서 나갔습니다.").build();
    }

    @Override
    public DataResDto<?> makeMatch(MakeMatchReqDto makeMatchREqDto, Member member) {
        Member opponent = memberRepository.findByMemberUuid(makeMatchREqDto.getOpponentUuid())
                .orElseThrow(() -> new NoSuchElementException(ErrorMessageEnum.USER_NOT_EXIST.getMessage()));
        Chatroom chatroom = chatroomRepoistory.findByChatRoomId(makeMatchREqDto.getChatroomId())
                .orElseThrow(() -> new NoSuchElementException(ErrorMessageEnum.CHATMEMBER_NOT_EXIST.getMessage()));
        Article article = chatroom.getArticle();

        if (article.getStatus() == 'T') {
            //article status변경
            article.setStatus('F');
            articleRepository.save(article);
            //평가 이력 2개 만들기
            MatchHistory matchHistory = new MatchHistory().builder()
                    .evaluator(member)
                    .evaluated(opponent)
                    .article(article)
                    .build();
            matchHistoryRepository.save(matchHistory);
            MatchHistory matchHistory2 = new MatchHistory().builder()
                    .evaluator(opponent)
                    .evaluated(member)
                    .article(article)
                    .build();
            matchHistoryRepository.save(matchHistory2);
            //chatroom의 매칭여부 변경
            chatroom.setStatus('F');
            chatroomRepoistory.save(chatroom);

            return DataResDto.builder().data(true).message("매칭에 성공했습니다.").build();
        } else {
            return DataResDto.builder().status(400).data(false).message("이미 매칭된 게시글입니다.").build();
        }
    }

    @Override
    public DataResDto<?> makeChat(ChatMakerReqDto chatMakerReqDto, Member member) {
        //상대 유저
        Member opponentUser = memberRepository.findByMemberUuid(UUID.fromString(chatMakerReqDto.getOpponentUserUuid()))
                .orElseThrow(() -> new NoSuchElementException(ErrorMessageEnum.USER_NOT_EXIST.getMessage()));
        //관련 게시글
        Article article = articleRepository.findByArticleId(chatMakerReqDto.getArticleId())
                .orElseThrow(() -> new NoSuchElementException(ErrorMessageEnum.ARTICLE_NOT_EXIST.getMessage()));
        //이미 채팅방이있다면?
        Optional<Chatroom> optionalChatroom = chatroomRepoistory.findByArticleAndMember(article, opponentUser);
        if (optionalChatroom.isPresent()) {
            //chatmember에 자기가 있으면 해당 챗룸 반환
            if (optionalChatroom.get().getChatMemberList().stream().filter(chatMember -> chatMember.getMember().equals(member)).findFirst().isPresent()) {
                List<ChatMessage> chatMessageList = chatMessageRepository.findByChatroomId(optionalChatroom.get().getChatRoomId());
                ChatMessage lastChatMessage = chatMessageList.get(chatMessageList.size() - 1);
                GetChatroomResDto getChatroomResDto = GetChatroomResDto.builder()
                        .chatroomId(optionalChatroom.get().getChatRoomId())
                        .nickname(opponentUser.getNickname())
                        .memberUuid(opponentUser.getMemberUuid().toString())
                        .imagePath(opponentUser.getProfileImgPath())
                        .updatedAt(lastChatMessage.getCreatedAt())
                        .lastMessage(lastChatMessage.getText())
                        .articleId(article.getArticleId())
                        .articleTitle(article.getTitle())
                        .similarity(euclideanDistanceUtil.getMatchingRate(member.getSurvey(), opponentUser.getSurvey()))
                        .status(article.getStatus())
                        .isMatch(optionalChatroom.get().getStatus() == 'F')
                        .build();
                return DataResDto.builder().status(201).data(getChatroomResDto).message("이미 생성된 채팅방이 있습니다.").build();
            } else {
                return DataResDto.builder().status(400).message("이미 나간 채팅방입니다.").data(false).build();
            }
        }
        //                ChatRoom
        //채팅방 저장
        Chatroom chatroom = new Chatroom().builder()
                .article(article)
                .status('T')
                .member(opponentUser)
                .build();
        Chatroom newChatroom = chatroomRepoistory.save(chatroom);
        //채팅방에 연결된  유저정보 저장
        chatMemberRepository.save(new ChatMember().builder()
                .member(member)
                .chatroom(newChatroom)
                .build());
        chatMemberRepository.save(new ChatMember().builder()
                .member(opponentUser)
                .chatroom(newChatroom)
                .build());
        //생성 메세지 추가
        chatMessageRepository.save(ChatMessage.builder()
                .text(member.getNickname() + "님과 " + opponentUser.getNickname() + "님의 채팅이 시작되었습니다.")
                .chatroomId(newChatroom.getChatRoomId())
                .createdAt(LocalDateTime.now())
                .memberUuid(member.getMemberUuid().toString())
                .build());
        GetChatroomResDto getChatroomResDto = GetChatroomResDto.builder()
                .chatroomId(chatroom.getChatRoomId())
                .nickname(opponentUser.getNickname())
                .memberUuid(opponentUser.getMemberUuid().toString())
                .imagePath(opponentUser.getProfileImgPath())
                .updatedAt(LocalDateTime.now())
                .lastMessage(member.getNickname() + "님과 " + opponentUser.getNickname() + "님의 채팅이 시작되었습니다.")
                .articleId(article.getArticleId())
                .articleTitle(article.getTitle())
                .similarity(euclideanDistanceUtil.getMatchingRate(member.getSurvey(), opponentUser.getSurvey()))
                .status(article.getStatus())
                .isMatch(false)
                .build();
        return DataResDto.builder().data(getChatroomResDto).message("채팅방을 생성했습니다.").build();
    }

    @Override
    public DataResDto<?> getDetail(Integer chatRoomId, Member member) {
        List<ChatMessageResDto> chatMessageResDtoList = new ArrayList<>();
        LocalDate localDate = LocalDate.parse("2000-01-01");
        for (ChatMessage chatMessage : chatMessageRepository.findByChatroomId(chatRoomId)) {
            Boolean isFirst = false;
            if (!localDate.equals(chatMessage.getCreatedAt().toLocalDate())) {
                isFirst = true;
                localDate = chatMessage.getCreatedAt().toLocalDate();
            }
            ChatMessageResDto chatMessageResDto = ChatMessageResDto.builder()
                    .content(chatMessage.getText())
                    .createdAt(chatMessage.getCreatedAt())
                    .isMine(member.getMemberUuid().toString().equals(chatMessage.getMemberUuid()))
                    .isFirst(isFirst)
                    .build();
            chatMessageResDtoList.add(chatMessageResDto);
        }
        return DataResDto.builder().data(chatMessageResDtoList).message("채팅 상세 조회를 성공했습니다.").build();
    }
}
