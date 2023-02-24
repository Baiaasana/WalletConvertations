package com.example.walletconvertation.di

import com.example.backend.data.network.ApiService
import com.example.backend.common.Constants
import com.example.backend.common.ResponseHandler
import com.example.backend.repository.course.CourseRepository
import com.example.backend.repository.course.CourseRepositoryImpl
import com.example.backend.repository.wallets.WalletsRepository
import com.example.backend.repository.wallets.WalletsRepositoryImpl
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun providesHttpLoggingInterceptor() = HttpLoggingInterceptor()
        .apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

    @Singleton
    @Provides
    fun providesOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient =
        OkHttpClient
            .Builder()
            .addInterceptor(httpLoggingInterceptor)
            .build()

    @Singleton
    @Provides
    fun apiService(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl(Constants.BASE)
        .addConverterFactory(
            MoshiConverterFactory.create(
                Moshi.Builder()
                    .addLast(KotlinJsonAdapterFactory())
                    .build()
            )
        )
        .client(okHttpClient)
        .build()

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun responseHandler(): ResponseHandler {
        return ResponseHandler()
    }

    @Binds
    @Singleton
    fun bindWalletsRepository(
        walletsRepositoryImpl: WalletsRepositoryImpl
    ): WalletsRepository {
        return walletsRepositoryImpl
    }

    @Binds
    @Singleton
    fun bindCourseRepository(
        courseRepositoryImpl: CourseRepositoryImpl
    ): CourseRepository {
        return courseRepositoryImpl
    }

}