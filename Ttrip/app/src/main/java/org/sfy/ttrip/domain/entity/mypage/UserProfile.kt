package org.sfy.ttrip.domain.entity.mypage

import android.content.Context
import org.sfy.ttrip.R

data class UserProfile(
    val uuid: String,
    val phoneNumber: String,
    val nickname: String?,
    val profileImgPath: String?,
    val markerImgPath: String?,
    val backgroundImgPath: String?,
    val intro: String?,
    val gender: String?,
    val age: String?,
    val fcmToken: String?,
    val shareLocation: Boolean,
    val profileVerification: Boolean
) {
    fun getGenderString(context: Context): String {
        return when (gender) {
            "MALE" -> context.getString(R.string.content_gender_male)
            "FEMALE" -> context.getString(R.string.content_gender_female)
            else -> gender!!
        }
    }
}