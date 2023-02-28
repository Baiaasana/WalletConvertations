package com.example.backend.data.model

data class WalletModel(
    val id: Int?,
    val title: String?,
    val balance: Float?,
    val currency: String?,
    val is_default: Boolean? = false,
    val account_number: Long?
) {
    fun UIModel():UIModel{
        return UIModel(
            id = id,
            title = title,
            balance = balance,
            currency = currency,
            is_default = is_default,
            account_number = account_number,
            is_selected = false
        )
    }
}

data class UIModel(
    val id: Int?,
    val title: String?,
    val balance: Float?,
    val currency: String?,
    val is_default: Boolean? = false,
    val account_number: Long?,
    val is_selected: Boolean? = false
)