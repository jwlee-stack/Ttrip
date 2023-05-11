package org.sfy.ttrip.data.local.datasource

import android.content.Context

class SharedPreferences(context: Context) {

    private val prefs = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)

    var accessToken: String?
        get() = prefs.getString("accessToken", null)
        set(value) = prefs.edit().putString("accessToken", value).apply()

    var refreshToken: String?
        get() = prefs.getString("refreshToken", null)
        set(value) = prefs.edit().putString("refreshToken", value).apply()

    var fcmToken: String?
        get() = prefs.getString("fcmToken", null)
        set(value) = prefs.edit().putString("fcmToken", value).apply()

    var userId: String?
        get() = prefs.getString("userId", null)
        set(value) = prefs.edit().putString("userId", value).apply()

    var nickname: String?
        get() = prefs.getString("nickname", null)
        set(value) = prefs.edit().putString("nickname", value).apply()

    var gender: String?
        get() = prefs.getString("gender", null)
        set(value) = prefs.edit().putString("gender", value).apply()

    var age: String?
        get() = prefs.getString("age", null)
        set(value) = prefs.edit().putString("age", value).apply()

    var profileImgPath: String?
        get() = prefs.getString("profileImgPath", null)
        set(value) = prefs.edit().putString("profileImgPath", value).apply()

    var markerImgPath: String?
        get() = prefs.getString("markerImgPath", null)
        set(value) = prefs.edit().putString("markerImgPath", value).apply()

    fun clearPreferences() {
        prefs.edit().clear().apply()
    }
}