package org.sfy.ttrip.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.sfy.ttrip.AuthInterceptorClient
import org.sfy.ttrip.NoAuthInterceptorClient
import org.sfy.ttrip.data.remote.service.AuthApiService
import org.sfy.ttrip.data.remote.service.BoardApiService
import org.sfy.ttrip.data.remote.service.LiveApiService
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
    fun provideBoardApiService(
        @AuthInterceptorClient retrofit: Retrofit
    ): BoardApiService =
        retrofit.create(BoardApiService::class.java)
}