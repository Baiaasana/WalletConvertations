package com.example.backend.repository.course

import androidx.lifecycle.LiveData
import com.example.backend.common.Resource
import com.example.backend.data.model.CourseModel

interface CourseRepository {

    suspend fun getCourse(from: String, to: String): LiveData<Resource<CourseModel>>

}