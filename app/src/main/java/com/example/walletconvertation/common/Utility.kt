package com.example.walletconvertation.common

import java.text.SimpleDateFormat
import java.util.*

interface Utility {

    fun setSymbol(course: String): String {

        return when (course) {
            CourseSymbols.GEL.name -> CourseSymbols.GEL.symbol
            CourseSymbols.USD.name -> CourseSymbols.USD.symbol
            CourseSymbols.EUR.name -> CourseSymbols.EUR.symbol
            else -> CourseSymbols.RUB.symbol
        }
    }

    fun coursesAreEquals(fromCourse: String, toCourse: String) = fromCourse == toCourse

    fun checkFloat(str: String) = str[str.length - 3] == '.'

    fun checkText(text: String) = (text.isNotEmpty() && text.toFloat() > 0)

    fun getData(milliSeconds: Long, dateFormat: String?): String? {
        val formatter = SimpleDateFormat(dateFormat)
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = milliSeconds
        return formatter.format(calendar.time)
    }

}