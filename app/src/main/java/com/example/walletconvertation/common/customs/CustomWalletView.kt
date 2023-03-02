package com.example.walletconvertation.common.customs

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.databinding.BindingAdapter
import com.example.walletconvertation.R

class CustomWalletView(context: Context, attrs: AttributeSet?) : LinearLayout(context, attrs) {

    private val walletImage: AppCompatImageView
    private val walletTitle: AppCompatTextView
    private val accountNumber: AppCompatTextView
    private val amount: AppCompatTextView
    private val currency: AppCompatTextView
    private val endIcon: AppCompatImageView

    init {
        val view = inflate(context, R.layout.custom_wallet_view, this)
        walletImage = view.findViewById(R.id.ivWallet)
        walletTitle = view.findViewById(R.id.tvTitle)
        accountNumber = view.findViewById(R.id.tvAccountNumber)
        amount = view.findViewById(R.id.tvAmount)
        currency = view.findViewById(R.id.tvCurrency)
        endIcon = view.findViewById(R.id.ivEndIcon)
    }

    fun getAmount(): AppCompatTextView {
        return amount
    }

    fun getTitle(): AppCompatTextView{
        return walletTitle
    }

    fun getAccount(): AppCompatTextView{
        return accountNumber
    }

    fun getCurrency(): AppCompatTextView {
        return currency
    }

    fun getEndIcon() : AppCompatImageView{
        return endIcon
    }

}

@BindingAdapter("walletCurrency")
fun setCurrency(wallet: CustomWalletView, currency: String) {
    wallet.getCurrency().text = currency
}

@BindingAdapter("walletAmount")
fun setAmount(wallet: CustomWalletView, amount: String) {
    wallet.getAmount().text = amount
}

@BindingAdapter("visibility")
fun visibility(wallet: CustomWalletView, visibility: Boolean){
    if(visibility){
        wallet.getEndIcon().visibility = View.VISIBLE
    }else {
        wallet.getEndIcon().visibility = View.INVISIBLE
    }
}

@BindingAdapter("walletTitle")
fun setTitle(view: CustomWalletView, title: String) {
    view.getTitle().text = title
}

@BindingAdapter("accountNumber")
fun setAccountNumber(view: CustomWalletView, title: String) {
    view.getAccount().text = title
}

@BindingAdapter("wallet_enabled")
fun setDisable(view: CustomWalletView, boolean: Boolean) {
    view.isEnabled  = boolean
}

