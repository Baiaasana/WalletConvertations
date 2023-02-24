package com.example.backend.data.model

data class WalletModel(
    val id: Int?,
    val title: String?,
    val balance: Float?,
    val currency: String?,
    val is_default: Boolean? = false,
    val account_number: Long?
)