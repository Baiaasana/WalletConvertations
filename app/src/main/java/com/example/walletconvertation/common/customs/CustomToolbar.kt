package com.example.walletconvertation.common.customs

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.databinding.BindingAdapter
import com.example.walletconvertation.R

class CustomToolbar(context: Context, attrs: AttributeSet?) : LinearLayout(context, attrs) {

    private val title: AppCompatTextView
    private val back: AppCompatImageView

    init {
        val view = inflate(context, R.layout.custom_toolbar, this)
        title = view.findViewById(R.id.tvToolbarTitle)
        back = view.findViewById(R.id.ivToolbarBack)
    }

    fun getTextView(): AppCompatTextView {
        return title
    }

    fun getBackIcon(): AppCompatImageView {
        return back
    }
}

@BindingAdapter("title")
fun setTitle(view: CustomToolbar, title: String) {
    view.getTextView().text = title
}


