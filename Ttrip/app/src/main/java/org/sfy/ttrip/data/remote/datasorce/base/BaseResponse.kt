package org.sfy.ttrip.data.remote.datasorce.base

import com.google.gson.annotations.SerializedName

open class BaseResponse<T>(
    @SerializedName("status")
    var status: Int? = null,
    @SerializedName("message")
    var message: String? = null,
    @SerializedName("data")
    var data: T? = null,
)