package org.sfy.ttrip.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.sfy.ttrip.data.remote.datasorce.auth.AuthRemoteDataSourceImpl
import org.sfy.ttrip.data.remote.datasorce.live.LiveRemoteDataSourceImpl
import org.sfy.ttrip.data.remote.datasorce.mypage.MyPageRemoteDataSourceImpl
import org.sfy.ttrip.data.remote.repository.AuthRepositoryImpl
import org.sfy.ttrip.data.remote.repository.LiveRepositoryImpl
import org.sfy.ttrip.data.remote.repository.MyPageRepositoryImpl
import org.sfy.ttrip.domain.repository.auth.AuthRepository
import org.sfy.ttrip.domain.repository.live.LiveRepository
import org.sfy.ttrip.domain.repository.mypage.MyPageRepository
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
}