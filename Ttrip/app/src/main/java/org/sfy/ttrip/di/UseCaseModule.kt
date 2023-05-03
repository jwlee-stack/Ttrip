package org.sfy.ttrip.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.sfy.ttrip.domain.repository.auth.AuthRepository
import org.sfy.ttrip.domain.repository.board.BoardRepository
import org.sfy.ttrip.domain.repository.live.LiveRepository
import org.sfy.ttrip.domain.repository.mypage.MyPageRepository
import org.sfy.ttrip.domain.repository.user.UserRepository
import org.sfy.ttrip.domain.usecase.auth.LoginUseCase
import org.sfy.ttrip.domain.usecase.auth.SignUpUseCase
import org.sfy.ttrip.domain.usecase.board.DeleteBoardUseCase
import org.sfy.ttrip.domain.usecase.board.GetBoardBriefUseCase
import org.sfy.ttrip.domain.usecase.board.GetBoardDetailUseCase
import org.sfy.ttrip.domain.usecase.live.GetLiveUsersUseCase
import org.sfy.ttrip.domain.usecase.mypage.UpdateUserInfoUseCase
import org.sfy.ttrip.domain.usecase.user.CheckDuplicationUseCase
import org.sfy.ttrip.domain.usecase.user.PostUserInfoUseCase
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
    fun provideUpdateUserInfoUSeCase(myPageRepository: MyPageRepository): UpdateUserInfoUseCase =
        UpdateUserInfoUseCase(myPageRepository)

    @Singleton
    @Provides
    fun provideCheckDuplicationUseCase(userRepository: UserRepository): CheckDuplicationUseCase =
        CheckDuplicationUseCase(userRepository)

    @Singleton
    @Provides
    fun providePostUserInfoUseCase(userRepository: UserRepository): PostUserInfoUseCase =
        PostUserInfoUseCase(userRepository)

    @Singleton
    @Provides
    fun provideGetBoardBriefUseCase(boardRepository: BoardRepository): GetBoardBriefUseCase =
        GetBoardBriefUseCase(boardRepository)

    @Singleton
    @Provides
    fun provideGetBoardDetailUseCase(boardRepository: BoardRepository): GetBoardDetailUseCase =
        GetBoardDetailUseCase(boardRepository)

    @Singleton
    @Provides
    fun provideDeleteBoardUseCase(boardRepository: BoardRepository): DeleteBoardUseCase =
        DeleteBoardUseCase(boardRepository)
}