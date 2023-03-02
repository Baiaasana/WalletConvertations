package com.example.walletconvertation.common

enum class ErrorEnum(val error: String?, val boolean: Boolean) {

    ERROR("სერვისი არ არის ხელმისაწვდომი", false),
    ERROR_NULL(null, false),
    NO_ERROR("", true),
    SERVER_ERROR("Unable to resolve host \"dev-t4249f36jq37062.api.raw-labs.com\": No address associated with hostname", false)
}