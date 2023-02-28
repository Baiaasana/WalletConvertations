package com.example.walletconvertation.common.customs

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.widget.doOnTextChanged
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import androidx.databinding.InverseMethod
import com.example.walletconvertation.R

class CustomEditText(context: Context, attrs: AttributeSet?) : LinearLayout(context, attrs) {

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
fun setCurrency(view :CustomEditText, currency: String) {
    view.getCurrency().text = currency
}

//@BindingAdapter("setAmount")
//fun setAmount(view :CustomEditText, amount: Float) {
//    view.getAmount().setText(amount.toString())
//}
//
//@InverseBindingAdapter(attribute = "setAmount")
//fun getAmount(view :CustomEditText) : Float {
//    return view.getAmount().text.toString().toFloat()
//}


//@BindingAdapter(value = ["setValue", "custom:AttrChanged"], requireAll = false)
//fun setValue(view :CustomEditText, text: String?, listener: InverseBindingListener){
//    if (view.getAmount().text.toString() != text) {
//        view.getAmount().setText(text)
//    }
//    view.getAmount().doOnTextChanged { _: CharSequence?, _: Int?, _: Int?, _: Int? ->
//        listener.onChange()
//    }
//}
//
//@InverseBindingAdapter(attribute = "setValue", event = "custom:AttrChanged")
//fun getValue(view :CustomEditText): String? {
//    return view.getAmount().text.toString().uppercase()
//}

@BindingAdapter("editTextValueAttrChanged")
fun setListener(amountInput: CustomEditText, listener: InverseBindingListener) {
    amountInput.getAmount().addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(p0: Editable?) {
            listener.onChange()
        }
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
    })
}

@BindingAdapter("editTextValue")
fun setTextValue(amountInput: CustomEditText, value: String?) {
    if (value != null) {
        if (value != amountInput.getAmount().text.toString()) amountInput.getAmount()
            .setText(value)
    }
}

@InverseBindingAdapter(attribute = "editTextValue")
fun getTextValue(amountInput: CustomEditText): String {
    return amountInput.getAmount().text.toString()
}

