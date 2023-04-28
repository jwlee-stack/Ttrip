package org.sfy.ttrip.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.sfy.ttrip.domain.repository.auth.AuthRepository
import org.sfy.ttrip.domain.usecase.auth.SignUpUseCase
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object UseCaseModule {

    @Singleton
    @Provides
    fun provideSignUpUseCase(authRepository: AuthRepository): SignUpUseCase =
        SignUpUseCase(authRepository)
}