package org.sfy.ttrip.data.remote.datasorce.mypage

import com.google.gson.annotations.SerializedName

data class UpdateUserInfoRequest(
    @SerializedName("age")
    val age: Int,
    @SerializedName("gender")
    val gender: String,
    @SerializedName("intro")
    val intro: String,
    @SerializedName("nickname")
    val nickname: String
)