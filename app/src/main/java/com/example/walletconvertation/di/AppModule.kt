package com.example.walletconvertation.di

import com.example.backend.common.Constants
import com.example.backend.common.ResponseHandler
import com.example.backend.data.model.WalletModel
import com.example.backend.data.network.ApiService
import com.example.walletconvertation.common.customs.walletView.WalletCallback
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
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

    @Provides
    @Singleton
    fun provideWalletCallback(): WalletCallback{
        return object : WalletCallback {
            override fun onWalletsFromChanged(walletsFromList: List<WalletModel>) {}
            override fun onWalletsToChanged(walletsToList: List<WalletModel>) {}
            override fun onSelectedWalletFromChanged(selectedWalletFrom: WalletModel) {}
            override fun onSelectedWalletToChanged(selectedWalletTo: WalletModel) {}
            override fun onLoadingStateChanged(loading: Boolean) {}
            override fun onError(errorMessage: String) {}
        }
    }
}