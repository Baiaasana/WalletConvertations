package com.example.walletconvertation.common

import com.example.backend.data.model.CourseModel

data class CourseViewState(
    val isLoading: Boolean? = false,
    val data: CourseModel? = CourseModel(0F),
    val errorMessage: String? = "",
)