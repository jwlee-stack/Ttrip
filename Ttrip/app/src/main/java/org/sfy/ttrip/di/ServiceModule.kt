package org.sfy.ttrip.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.sfy.ttrip.AuthInterceptorClient
import org.sfy.ttrip.NoAuthInterceptorClient
import org.sfy.ttrip.data.remote.service.*
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {

    @Provides
    @Singleton
    fun provideAuthApiService(
        @NoAuthInterceptorClient retrofit: Retrofit
    ): AuthApiService =
        retrofit.create(AuthApiService::class.java)

    @Provides
    @Singleton
    fun provideLiveApiService(
        @AuthInterceptorClient retrofit: Retrofit
    ): LiveApiService =
        retrofit.create(LiveApiService::class.java)

    @Provides
    @Singleton
    fun provideMyPageApiService(
        @AuthInterceptorClient retrofit: Retrofit
    ): MyPageApiService =
        retrofit.create(MyPageApiService::class.java)

    @Provides
    @Singleton
    fun provideUserApiService(
        @AuthInterceptorClient retrofit: Retrofit
    ): UserApiService =
        retrofit.create(UserApiService::class.java)

    @Provides
    @Singleton
    fun provideBoardApiService(
        @AuthInterceptorClient retrofit: Retrofit
    ): BoardApiService =
        retrofit.create(BoardApiService::class.java)

    @Provides
    @Singleton
    fun provideChatApiService(
        @AuthInterceptorClient retrofit: Retrofit
    ): ChatApiService =
        retrofit.create(ChatApiService::class.java)

    @Provides
    @Singleton
    fun provideLandmarkApiService(
        @AuthInterceptorClient retrofit: Retrofit
    ): LandmarkApiService =
        retrofit.create(LandmarkApiService::class.java)
}