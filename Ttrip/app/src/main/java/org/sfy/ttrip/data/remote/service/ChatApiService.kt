package org.sfy.ttrip.data.remote.service

import org.sfy.ttrip.data.remote.datasorce.base.BaseResponse
import org.sfy.ttrip.data.remote.datasorce.chat.*
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

    @POST("/api/chats/match")
    suspend fun chatMatch(@Body body: ChatMatchRequest): BaseResponse<Boolean>
}