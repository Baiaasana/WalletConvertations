package com.example.backend.data.model

data class WalletModel(
    val id: Int? = 0,
    val title: String? = "",
    val balance: Float? = 0F,
    val currency: String? = "",
    val is_default: Boolean? = false,
    val account_number: Long? = 0L
)