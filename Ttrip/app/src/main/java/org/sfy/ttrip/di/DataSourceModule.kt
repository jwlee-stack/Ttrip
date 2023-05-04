package org.sfy.ttrip.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.sfy.ttrip.data.remote.datasorce.auth.AuthRemoteDataSourceImpl
import org.sfy.ttrip.data.remote.datasorce.board.BoardRemoteDataSourceImpl
import org.sfy.ttrip.data.remote.datasorce.chat.ChatRemoteDataSourceImpl
import org.sfy.ttrip.data.remote.datasorce.live.LiveRemoteDataSourceImpl
import org.sfy.ttrip.data.remote.datasorce.mypage.MyPageRemoteDataSourceImpl
import org.sfy.ttrip.data.remote.datasorce.user.UserRemoteDataSourceImpl
import org.sfy.ttrip.data.remote.service.*
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
    fun provideBoardDataSource(
        boardApiService: BoardApiService
    ): BoardRemoteDataSourceImpl = BoardRemoteDataSourceImpl(boardApiService)

    @Provides
    @Singleton
    fun provideMyPageDataSource(
        myPageApiService: MyPageApiService
    ): MyPageRemoteDataSourceImpl = MyPageRemoteDataSourceImpl(myPageApiService)

    @Provides
    @Singleton
    fun provideChatDataSource(
        chatApiService: ChatApiService
    ): ChatRemoteDataSourceImpl = ChatRemoteDataSourceImpl(chatApiService)
}