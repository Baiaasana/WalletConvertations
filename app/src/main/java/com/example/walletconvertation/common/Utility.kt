package com.example.walletconvertation.common

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.LifecycleOwner

interface Utility {
    fun setSymbol(course: String): String {

        return when (course) {
            CourseSymbols.RUB.name -> CourseSymbols.RUB.symbol
            CourseSymbols.USD.name -> CourseSymbols.USD.symbol
            CourseSymbols.EUR.name -> CourseSymbols.EUR.symbol
            else -> CourseSymbols.GEL.symbol
        }
    }
    fun Context.hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun getLifeCycleOwner(view: View): LifecycleOwner? {
        var context = view.context

        while (context is ContextWrapper) {
            if (context is LifecycleOwner) {
                return context
            }
            context = context.baseContext
        }

        return null
    }
}