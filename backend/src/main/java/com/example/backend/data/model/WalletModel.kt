package com.example.backend.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class WalletModel(
    val id: Int? = 0,
    val title: String? = "",
    val balance: Float? = 0F,
    val currency: String? = "",
    val is_default: Boolean? = false,
    val account_number: Long? = 0L,
    var is_selected_from: Boolean = false,
    var is_selected_to: Boolean = false,
    var enable: Boolean = true
): Parcelable
