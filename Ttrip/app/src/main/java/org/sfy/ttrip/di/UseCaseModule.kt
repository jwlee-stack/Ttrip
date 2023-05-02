package org.sfy.ttrip.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.sfy.ttrip.domain.repository.auth.AuthRepository
import org.sfy.ttrip.domain.repository.board.BoardRepository
import org.sfy.ttrip.domain.repository.live.LiveRepository
import org.sfy.ttrip.domain.usecase.auth.LoginUseCase
import org.sfy.ttrip.domain.usecase.auth.SignUpUseCase
import org.sfy.ttrip.domain.usecase.board.GetBoardBriefUseCase
import org.sfy.ttrip.domain.usecase.live.GetLiveUsersUseCase
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object UseCaseModule {

    @Singleton
    @Provides
    fun provideSignUpUseCase(authRepository: AuthRepository): SignUpUseCase =
        SignUpUseCase(authRepository)

    @Singleton
    @Provides
    fun provideLoginUseCase(authRepository: AuthRepository): LoginUseCase =
        LoginUseCase(authRepository)

    @Singleton
    @Provides
    fun provideGetLiveUsersUseCase(liveRepository: LiveRepository): GetLiveUsersUseCase =
        GetLiveUsersUseCase(liveRepository)

    @Singleton
    @Provides
    fun provideGetBoardBriefUseCase(boardRepository: BoardRepository): GetBoardBriefUseCase =
        GetBoardBriefUseCase(boardRepository)
}