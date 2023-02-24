package com.example.walletconvertation.presentation.fragments.convert

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.lifecycle.*
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.savedstate.SavedStateRegistryOwner
import com.example.backend.common.Resource
import com.example.backend.data.model.CourseModel
import com.example.backend.repository.course.CourseRepository
import com.example.backend.repository.course.CourseRepositoryImpl
import com.example.backend.repository.course.CourseRepositoryImpl_Factory
import com.example.backend.repository.wallets.WalletsRepository
import com.example.backend.repository.wallets.WalletsRepositoryImpl
import com.example.walletconvertation.common.CourseViewState
import com.example.walletconvertation.common.WalletsViewState
import com.example.walletconvertation.di.AppModule
import com.example.walletconvertation.di.RepositoryModule
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConvertViewModel @Inject constructor(
    private val courseRepository: CourseRepository,
    private val walletsRepository: WalletsRepository
) : ViewModel() {

//    private val _course = MutableLiveData<CourseViewState>()
//    val course: LiveData<CourseViewState> = _course

    private val _rate = MutableLiveData<Float?>()
    val rate: LiveData<Float?> = _rate

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage


    fun getGELUSD() {
        getCourse("GEL", "USD")
    }

    private fun getCourse(from: String, to: String) {
        _loading.postValue(true)
        viewModelScope.launch {
            val result = courseRepository.getCourse(from, to).value
            when (result!!.status) {
                Resource.Status.SUCCESS -> {
                    result.data?.rate.let {
                        _rate.postValue(it)
                        _errorMessage.postValue("")
                    }
                    result.data?.rate ?: kotlin.run {
                        _errorMessage.postValue("სერვისი არ არის ხელმისაწვდომი")
                    }
                }
                Resource.Status.ERROR -> {
                    _errorMessage.postValue(result.message.toString())
                }
            }
            _loading.postValue(false)
        }
    }

}
