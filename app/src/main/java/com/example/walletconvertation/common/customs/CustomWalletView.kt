package com.example.walletconvertation.common.customs

import android.content.Context
import android.opengl.Visibility
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
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

    fun getCurrency(): AppCompatTextView {
        return currency
    }

    fun getEndIcon() : AppCompatImageView{
        return endIcon
    }

}

@BindingAdapter("currency")
fun setCurrency(wallet: CustomWalletView, currency: String) {
    wallet.getCurrency().text = currency
}

@BindingAdapter("amount")
fun setAmount(wallet: CustomWalletView, amount: String) {
    wallet.getAmount().text = amount
}

@BindingAdapter("visibility")
fun visibility(wallet: CustomWalletView, visibility: Int){
     wallet.getEndIcon().visibility = visibility
}




//@InverseBindingAdapter(attribute = "currency")
//fun getCurrency(view :CustomConvertView) : String {
//    return view.getCurrency()
//}
//
//@InverseBindingAdapter(attribute = "amount")
//fun getaAmount(view :CustomConvertView) : Float {
//    return view.getAmount()
//}


