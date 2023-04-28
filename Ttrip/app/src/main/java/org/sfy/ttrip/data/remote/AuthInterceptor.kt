package org.sfy.ttrip.data.remote

import okhttp3.Interceptor
import okhttp3.Response
import org.sfy.ttrip.data.local.datasource.SharedPreferences

class AuthInterceptor(private val preferences: SharedPreferences) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {

        val request = chain.request().newBuilder()
            .addHeader("Authorization", "bearer " + preferences.accessToken).build()

        return chain.proceed(request)
    }
}