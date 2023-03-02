package com.example.backend.data.network

import com.example.backend.common.Constants
import com.example.backend.data.model.CourseModel
import com.example.backend.data.model.WalletModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET(Constants.WALLETS_END_POINT)
    suspend fun getWallets() : Response<List<WalletModel>>
    @GET(Constants.COURSES_END_POINT)
    suspend fun getCourse(@Query("from") from: String, @Query("to") to: String) : Response<CourseModel>

}