package com.example.walletconvertation.common

import com.example.backend.data.model.WalletModel

data class WalletsViewState(
    val isLoading: Boolean = false,
    val data: List<WalletModel>? = emptyList(),
    val errorMessage: String = "error",
)
