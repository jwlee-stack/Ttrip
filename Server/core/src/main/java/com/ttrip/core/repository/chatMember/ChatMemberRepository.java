package com.ttrip.core.repository.chatMember;

import com.ttrip.core.entity.chatMember.ChatMember;
import com.ttrip.core.entity.chatroom.Chatroom;
import com.ttrip.core.entity.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatMemberRepository extends JpaRepository<ChatMember, Integer> {
    List<ChatMember> findByMember(Member member);
    Optional<ChatMember> findByMemberAndChatroom(Member member, Chatroom chatroom);
}
