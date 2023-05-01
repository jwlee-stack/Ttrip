package com.ttrip.api.controller;

import com.ttrip.api.dto.chatroomDto.ChatMakerReqDto;
import com.ttrip.api.dto.DataResDto;
import com.ttrip.api.dto.chatroomDto.ExitChatReqDto;
import com.ttrip.api.dto.matchDto.MakeMatchReqDto;
import com.ttrip.api.service.ChatService;
import com.ttrip.api.service.impl.MemberDetails;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Api(tags = "채팅 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chats")
public class ChatController {
    private final ChatService chatService;

    @ApiResponses({
            @ApiResponse(code = 200, message = "채팅을 조회했습니다."),
            @ApiResponse(code = 400, message = "채팅 조회 실패")
    })
    @ApiOperation(value = "채팅 목록 조회 API", httpMethod = "GET")
    @GetMapping
    public DataResDto<?> search(@AuthenticationPrincipal MemberDetails memberDetails) {
        return chatService.getChatroomList(memberDetails.getMember());
    }

    @ApiResponses({
            @ApiResponse(code = 200, message = "채팅방을 나갔습니다."),
            @ApiResponse(code = 400, message = "채팅방 퇴장 실패")
    })
    @ApiOperation(value = "채팅방 퇴장 API", httpMethod = "POST")
    @PostMapping("/exit")
    public DataResDto<?> remove(@AuthenticationPrincipal MemberDetails memberDetails, @RequestBody ExitChatReqDto exitChatReqDto) {
        return chatService.removeChatroom(exitChatReqDto, memberDetails.getMember());
    }

    @ApiResponses({
            @ApiResponse(code = 200, message = "채팅방을 생성했습니다."),
            @ApiResponse(code = 400, message = "채팅방 생성 실패")
    })
    @ApiOperation(value = "채팅방 생성 API", httpMethod = "POST")
    @PostMapping
    public DataResDto<?> makeChat(@AuthenticationPrincipal MemberDetails memberDetails, @RequestBody ChatMakerReqDto chatMakerReqDto) {
        return chatService.makeChat(chatMakerReqDto, memberDetails.getMember());
    }

    @ApiResponses({
            @ApiResponse(code = 200, message = "매칭 성공했습니다."),
            @ApiResponse(code = 400, message = "매칭 실패")
    })
    @ApiOperation(value = "채팅방에서 매칭 API", httpMethod = "GET")
    @PostMapping("/match")
    public DataResDto<?> makeMatch(@AuthenticationPrincipal MemberDetails memberDetails, @RequestBody MakeMatchReqDto makeMatchREqDto) {
        return chatService.makeMatch(makeMatchREqDto, memberDetails.getMember());
    }

    @ApiResponses({
            @ApiResponse(code = 200, message = "채팅을 상세 조회했습니다."),
            @ApiResponse(code = 400, message = "채팅 상세 조회 실패")
    })
    @ApiOperation(value = "채팅 상세 목록 조회 API", httpMethod = "GET")
    @GetMapping("/{chatRoomId}")
    public DataResDto<?> detail(@AuthenticationPrincipal MemberDetails memberDetails, @PathVariable Integer chatRoomId) {
        return chatService.getDetail(chatRoomId, memberDetails.getMember());
    }
}
