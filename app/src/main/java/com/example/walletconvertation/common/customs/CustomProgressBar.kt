package com.example.walletconvertation.common.customs

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.databinding.BindingAdapter
import com.example.walletconvertation.R

class CustomProgressBar(context: Context, attrs: AttributeSet?) : LinearLayout(context, attrs) {

    private val progressBar: ProgressBar

    init {
        val view = inflate(context, R.layout.custom_progress_bar, this)
        progressBar = view.findViewById(R.id.customProgressBar)
    }

    fun getProgressBar(): ProgressBar {
        return progressBar
    }
}

@BindingAdapter("isVisible")
fun setVisibility(progressBar: CustomProgressBar, loading: Boolean) {
    if (loading) progressBar.getProgressBar().visibility = View.VISIBLE else progressBar.getProgressBar().visibility = View.INVISIBLE
}

