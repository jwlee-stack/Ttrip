package org.sfy.ttrip.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.sfy.ttrip.data.remote.datasorce.auth.AuthRemoteDataSourceImpl
import org.sfy.ttrip.data.remote.datasorce.live.LiveRemoteDataSourceImpl
import org.sfy.ttrip.data.remote.datasorce.mypage.MyPageRemoteDataSourceImpl
import org.sfy.ttrip.data.remote.datasorce.user.UserRemoteDataSourceImpl
import org.sfy.ttrip.data.remote.service.AuthApiService
import org.sfy.ttrip.data.remote.service.LiveApiService
import org.sfy.ttrip.data.remote.service.MyPageApiService
import org.sfy.ttrip.data.remote.service.UserApiService
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {

    @Provides
    @Singleton
    fun provideAuthDataSource(
        authApiService: AuthApiService
    ): AuthRemoteDataSourceImpl = AuthRemoteDataSourceImpl(authApiService)

    @Provides
    @Singleton
    fun provideLiveDataSource(
        liveApiService: LiveApiService
    ): LiveRemoteDataSourceImpl = LiveRemoteDataSourceImpl(liveApiService)

    @Provides
    @Singleton
    fun provideUserDataSource(
        userApiService: UserApiService
    ): UserRemoteDataSourceImpl = UserRemoteDataSourceImpl(userApiService)

    @Provides
    @Singleton
    fun provideMyPageDataSource(
        myPageApiService: MyPageApiService
    ): MyPageRemoteDataSourceImpl = MyPageRemoteDataSourceImpl(myPageApiService)
}