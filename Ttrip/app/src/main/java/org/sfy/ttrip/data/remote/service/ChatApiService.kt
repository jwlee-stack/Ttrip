package org.sfy.ttrip.data.remote.service

import org.sfy.ttrip.data.remote.datasorce.base.BaseResponse
import org.sfy.ttrip.data.remote.datasorce.chat.ChatRoomResponse
import retrofit2.http.GET

interface ChatApiService {

    @GET("/api/chats")
    suspend fun getChatRooms(): BaseResponse<List<ChatRoomResponse>>
}