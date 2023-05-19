package com.ttrip.core.repository.chatroom;

import com.ttrip.core.entity.article.Article;
import com.ttrip.core.entity.chatroom.Chatroom;
import com.ttrip.core.entity.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatroomRepoistory extends JpaRepository<Chatroom, Integer> {
    Optional<Chatroom> findByChatRoomId(Integer chatRoomId);

    Optional<Chatroom> findByArticleAndMember(Article article, Member member);
}
