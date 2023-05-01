package org.sfy.ttrip.data.remote.repository

import com.google.gson.annotations.SerializedName
import org.sfy.ttrip.data.remote.datasorce.base.DataToDomainMapper
import org.sfy.ttrip.domain.entity.user.CheckDuplication

data class CheckDuplicationResponse(
    @SerializedName("isExist")
    val isExist: Boolean
) : DataToDomainMapper<CheckDuplication> {
    override fun toDomainModel(): CheckDuplication =
        CheckDuplication(
            isExist
        )
}
