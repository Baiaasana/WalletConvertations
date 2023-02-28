package com.example.walletconvertation.common.customs

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import com.example.walletconvertation.R

class CustomConvertView(context: Context, attrs: AttributeSet?) : LinearLayout(context, attrs) {

    private val amount: AppCompatEditText
    private val currency: AppCompatTextView

    init {
        val view = inflate(context, R.layout.custom_convert_edit_text, this)
        amount = view.findViewById(R.id.etAmount_)
        currency = view.findViewById(R.id.tvCurrency_)
    }

    fun getAmount(): AppCompatEditText {
        return amount
    }

    fun getCurrency(): AppCompatTextView {
        return currency
    }
}

@BindingAdapter("setCurrency")
fun setCurrency(view: CustomConvertView, currency: String) {
    view.getCurrency().text = currency
}

@InverseBindingAdapter(attribute = "setAmount")
fun setAmount(view : CustomConvertView, amount: Float) {
    view.getAmount().setText(amount.toString())
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

