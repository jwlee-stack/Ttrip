package org.sfy.ttrip.data.remote.datasorce.user

data class UserReportRequest(
    val reportContext: String,
    val reportedNickname: String,
    val matchHistoryId: String
)
