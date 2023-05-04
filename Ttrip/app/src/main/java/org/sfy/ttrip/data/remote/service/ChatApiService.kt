package org.sfy.ttrip.data.remote.service

import org.sfy.ttrip.data.remote.datasorce.base.BaseResponse
import org.sfy.ttrip.data.remote.datasorce.chat.ChatRoomResponse
import org.sfy.ttrip.data.remote.datasorce.chat.ExitChatRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ChatApiService {

    @GET("/api/chats")
    suspend fun getChatRooms(): BaseResponse<List<ChatRoomResponse>>

    @POST("/api/chats/exit")
    suspend fun exitChatRoom(@Body body: ExitChatRequest): BaseResponse<Boolean>
}