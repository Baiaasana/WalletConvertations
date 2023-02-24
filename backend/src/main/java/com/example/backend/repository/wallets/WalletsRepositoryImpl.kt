package com.example.backend.repository.wallets

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.backend.common.Resource
import com.example.backend.common.ResponseHandler
import com.example.backend.data.model.CourseModel
import com.example.backend.data.model.WalletModel
import com.example.backend.data.network.ApiService
import com.example.backend.repository.course.CourseRepository
import javax.inject.Inject

class WalletsRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val responseHandler: ResponseHandler,
) : WalletsRepository {

    private val wallets = MutableLiveData<Resource<List<WalletModel>>>()

    override suspend fun getWallets(): LiveData<Resource<List<WalletModel>>> {

        val result = responseHandler.safeApiCall { apiService.getWallets() }
        wallets.value = result
        return wallets

    }
}