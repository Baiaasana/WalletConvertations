package com.example.backend.repository.course

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.backend.common.Resource
import com.example.backend.common.ResponseHandler
import com.example.backend.data.model.CourseModel
import com.example.backend.data.network.ApiService
import javax.inject.Inject

class CourseRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val responseHandler: ResponseHandler,
) : CourseRepository {

    private val courses = MutableLiveData<Resource<CourseModel>>()
    override suspend fun getCourse(from: String, to: String): LiveData<Resource<CourseModel>> {

        val result = responseHandler.safeApiCall { apiService.getCourse(from = from, to = to) }
        courses.value = result
        return courses
    }
}
