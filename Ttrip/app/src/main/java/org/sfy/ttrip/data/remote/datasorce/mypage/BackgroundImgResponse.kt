package org.sfy.ttrip.data.remote.datasorce.mypage

import com.google.gson.annotations.SerializedName
import org.sfy.ttrip.data.remote.datasorce.base.DataToDomainMapper
import org.sfy.ttrip.domain.entity.mypage.BackgroundImg

data class BackgroundImgResponse(
    @SerializedName("backgroundImgPath")
    val backgroundImgPath: String
) : DataToDomainMapper<BackgroundImg> {
    override fun toDomainModel(): BackgroundImg =
        BackgroundImg(backgroundImgPath)
}