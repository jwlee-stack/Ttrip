package org.sfy.ttrip.data.remote.datasorce.live

interface LiveRemoteDataSource {

    suspend fun getLiveUsers(city: String, lng: Double, lat: Double): List<LiveUserResponse?>

    suspend fun createSession(): SessionResponse

    suspend fun getCallToken(sessionId: String, body: CallTokenRequest): CallTokenResponse
}