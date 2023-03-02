package com.example.backend.common

import retrofit2.Response

class ResponseHandler {
    suspend fun <T> safeApiCall(request: suspend () -> Response<T>): Resource<T> =

        try {
            val response = request.invoke()
            val result = response.body()!!
            if (response.isSuccessful) {
                Resource.success(result)
            } else {
                Resource.error(response.message())
            }
        } catch (e: Exception) {
            Resource.error(e.message.toString())
        }
}