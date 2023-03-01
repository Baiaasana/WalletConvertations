package com.example.walletconvertation.common

enum class ErrorEnum(val error: String?, val boolean: Boolean) {

    ERROR("სერვისი არ არის ხელმისაწვდომი", false),
    NO_ERROR(null, true),
}