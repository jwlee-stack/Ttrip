package org.sfy.ttrip.data.remote.service

import org.sfy.ttrip.data.remote.datasorce.base.BaseResponse
import org.sfy.ttrip.data.remote.datasorce.chat.ChatDetailResponse
import org.sfy.ttrip.data.remote.datasorce.chat.ChatRoomResponse
import org.sfy.ttrip.data.remote.datasorce.chat.CreateChatRequest
import org.sfy.ttrip.data.remote.datasorce.chat.ExitChatRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Part

interface ChatApiService {

    @GET("/api/chats")
    suspend fun getChatRooms(): BaseResponse<List<ChatRoomResponse>>

    @POST("/api/chats/exit")
    suspend fun exitChatRoom(@Body body: ExitChatRequest): BaseResponse<Boolean>

    @GET("/api/chats/{chatId}")
    suspend fun getChatDetail(@Part chatId: Int): BaseResponse<List<ChatDetailResponse>>

    @POST("/api/chats")
    suspend fun createChatRoom(@Body body: CreateChatRequest)
}