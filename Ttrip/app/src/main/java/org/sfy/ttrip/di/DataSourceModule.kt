package org.sfy.ttrip.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.sfy.ttrip.data.remote.datasorce.auth.AuthRemoteDataSourceImpl
import org.sfy.ttrip.data.remote.datasorce.board.BoardRemoteDataSourceImpl
import org.sfy.ttrip.data.remote.datasorce.live.LiveRemoteDataSourceImpl
import org.sfy.ttrip.data.remote.service.AuthApiService
import org.sfy.ttrip.data.remote.service.BoardApiService
import org.sfy.ttrip.data.remote.service.LiveApiService
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
    fun provideBoardDataSource(
        boardApiService: BoardApiService
    ): BoardRemoteDataSourceImpl = BoardRemoteDataSourceImpl(boardApiService)
}