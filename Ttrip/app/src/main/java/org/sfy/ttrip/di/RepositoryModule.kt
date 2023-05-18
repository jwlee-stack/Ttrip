package org.sfy.ttrip.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.sfy.ttrip.data.remote.datasorce.auth.AuthRemoteDataSourceImpl
import org.sfy.ttrip.data.remote.datasorce.board.BoardRemoteDataSourceImpl
import org.sfy.ttrip.data.remote.datasorce.chat.ChatRemoteDataSourceImpl
import org.sfy.ttrip.data.remote.datasorce.landmark.LandmarkRemoteDataSourceImpl
import org.sfy.ttrip.data.remote.datasorce.live.LiveRemoteDataSourceImpl
import org.sfy.ttrip.data.remote.datasorce.mypage.MyPageRemoteDataSourceImpl
import org.sfy.ttrip.data.remote.datasorce.user.UserRemoteDataSourceImpl
import org.sfy.ttrip.data.remote.repository.*
import org.sfy.ttrip.domain.repository.auth.AuthRepository
import org.sfy.ttrip.domain.repository.board.BoardRepository
import org.sfy.ttrip.domain.repository.chat.ChatRepository
import org.sfy.ttrip.domain.repository.landmark.LandmarkRepository
import org.sfy.ttrip.domain.repository.live.LiveRepository
import org.sfy.ttrip.domain.repository.mypage.MyPageRepository
import org.sfy.ttrip.domain.repository.user.UserRepository
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RepositoryModule {

    @Provides
    @Singleton
    fun provideAuthRepository(
        authRemoteDataSourceImpl: AuthRemoteDataSourceImpl
    ): AuthRepository = AuthRepositoryImpl(authRemoteDataSourceImpl)

    @Provides
    @Singleton
    fun provideLiveRepository(
        liveRemoteDataSourceImpl: LiveRemoteDataSourceImpl
    ): LiveRepository = LiveRepositoryImpl(liveRemoteDataSourceImpl)

    @Provides
    @Singleton
    fun provideMyPageRepository(
        myPageRemoteDataSourceImpl: MyPageRemoteDataSourceImpl
    ): MyPageRepository = MyPageRepositoryImpl(myPageRemoteDataSourceImpl)

    @Provides
    @Singleton
    fun provideUserRepository(
        userRemoteDataSourceImpl: UserRemoteDataSourceImpl
    ): UserRepository = UserRepositoryImpl(userRemoteDataSourceImpl)

    @Provides
    @Singleton
    fun provideBoardRepository(
        boardRemoteDataSourceImpl: BoardRemoteDataSourceImpl
    ): BoardRepository = BoardRepositoryImpl(boardRemoteDataSourceImpl)

    @Provides
    @Singleton
    fun provideChatRepository(
        chatRemoteDataSourceImpl: ChatRemoteDataSourceImpl
    ): ChatRepository = ChatRepositoryImpl(chatRemoteDataSourceImpl)

    @Provides
    @Singleton
    fun provideLandmarkRepository(
        landmarkRemoteDataSourceImpl: LandmarkRemoteDataSourceImpl
    ): LandmarkRepository = LandmarkRepositoryImpl(landmarkRemoteDataSourceImpl)
}