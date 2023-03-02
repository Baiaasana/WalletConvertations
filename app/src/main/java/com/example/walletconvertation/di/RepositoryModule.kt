package com.example.walletconvertation.di

import com.example.backend.repository.course.CourseRepository
import com.example.backend.repository.course.CourseRepositoryImpl
import com.example.backend.repository.wallets.WalletsRepository
import com.example.backend.repository.wallets.WalletsRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindWalletsRepository(
        walletsRepositoryImpl: WalletsRepositoryImpl
    ): WalletsRepository

    @Binds
    @Singleton
    abstract fun bindCourseRepository(
        courseRepositoryImpl: CourseRepositoryImpl
    ): CourseRepository
}