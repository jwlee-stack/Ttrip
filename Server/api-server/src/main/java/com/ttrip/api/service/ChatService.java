package com.ttrip.api.service;

import com.ttrip.api.dto.ChatMakerReqDto;
import com.ttrip.api.dto.DataResDto;
import com.ttrip.api.dto.ExitChatReqDto;
import com.ttrip.api.dto.MakeMatchREqDto;
import com.ttrip.core.entity.member.Member;

public interface ChatService {
    DataResDto<?> getChatroomList(Member member);

    DataResDto<?> removeChatroom(ExitChatReqDto exitChatReqDto, Member member);

    DataResDto<?> makeMatch(MakeMatchREqDto makeMatchREqDto, Member member);

    DataResDto<?> makeChat(ChatMakerReqDto chatMakerReqDto, Member member);

    DataResDto<?> getDetail(Integer chatRoomId, Member member);
}
