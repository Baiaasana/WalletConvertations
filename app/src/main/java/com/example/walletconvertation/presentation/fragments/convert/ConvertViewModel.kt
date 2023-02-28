package com.example.walletconvertation.presentation.fragments.convert

import android.util.Log
import androidx.lifecycle.*
import com.example.backend.common.Resource
import com.example.backend.data.model.WalletModel
import com.example.backend.repository.course.CourseRepository
import com.example.backend.repository.wallets.WalletsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConvertViewModel @Inject constructor(
    private val courseRepository: CourseRepository,
    private val walletsRepository: WalletsRepository
) : ViewModel() {

    private val _rate = MutableLiveData<Float?>()
    val rate: LiveData<Float?> = _rate

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    private val _wallets = MutableLiveData<List<WalletModel>?>()
    val wallets: LiveData<List<WalletModel>?> = _wallets

    private val _walletLoading = MutableLiveData<Boolean>()
    val walletLoading: LiveData<Boolean> = _walletLoading

    private val _walletsErrorMessage = MutableLiveData<String?>()
    val walletsErrorMessage: LiveData<String?> = _walletsErrorMessage

//    wallets.value!!.findLast { it.is_default == true }

    init {
        getWallets()
    }


    private val _selectedWalletFrom = MutableLiveData<WalletModel>(WalletModel(9, "dsds", 34.00F, "ds", true, 4343L))
    val selectedWalletFrom: LiveData<WalletModel> = _selectedWalletFrom

    private val _selectedWalletTo = MutableLiveData<WalletModel>(WalletModel(9, "dsds", 34.00F, "ds", true, 4343L))
    val selectedWalletTo: LiveData<WalletModel> = _selectedWalletTo

    fun selectWalletFrom(walletModel: WalletModel){
        _selectedWalletFrom.postValue(walletModel)
        Log.d("select wallet", "from ".plus(selectedWalletFrom.value))
    }

    fun selectWalletTo(walletModel: WalletModel){
        _selectedWalletTo.postValue(walletModel)
        Log.d("select wallet", "to ".plus(selectedWalletTo.value))
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

    private fun getWallets() {
        _walletLoading.postValue(true)
        viewModelScope.launch {
            val result = walletsRepository.getWallets().value
            when (result!!.status) {
                Resource.Status.SUCCESS -> {
                    result.data?.let {
                        _wallets.postValue(it)
                    }
                    result.data ?: kotlin.run {
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
