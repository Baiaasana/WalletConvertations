package com.example.walletconvertation.common.customs

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import com.example.walletconvertation.R

class CustomTextView(context: Context, attrs: AttributeSet?) : LinearLayout(context, attrs) {

    private val amount: AppCompatTextView
    private val currency: AppCompatTextView

    init {
        val view = inflate(context, R.layout.custom_text_view, this)
        amount = view.findViewById(R.id.customAmount)
        currency = view.findViewById(R.id.customCurrency)
    }

    fun getAmount(): AppCompatTextView {
        return amount
    }

    fun getCurrency(): AppCompatTextView {
        return currency
    }
}

@BindingAdapter("setCurrency")
fun setCurrency(view: CustomTextView, currency: String) {
    view.getCurrency().text = currency
}

@BindingAdapter("setAmount")
fun setAmount(view : CustomTextView, amount: Float) {
    view.getAmount().text = amount.toString()
}

@BindingAdapter("visible")
fun setVisible(view: CustomTextView, bool: Boolean){
    if(bool) {
        view.visibility = View.VISIBLE
    } else {
        view.visibility = View.INVISIBLE
    }
}


