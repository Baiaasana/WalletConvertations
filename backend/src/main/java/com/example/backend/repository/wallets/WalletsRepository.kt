package com.example.backend.repository.wallets

import androidx.lifecycle.LiveData
import com.example.backend.common.Resource
import com.example.backend.data.model.WalletModel
import kotlinx.coroutines.flow.Flow

interface WalletsRepository {

    suspend fun getWallets() : LiveData<Resource<List<WalletModel>>>

}