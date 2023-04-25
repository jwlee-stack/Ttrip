package com.ttrip.core.repository.chatroom;

import com.ttrip.core.entity.chatroom.Chatroom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatroomRepoistory extends JpaRepository<Chatroom, Integer> {
}
