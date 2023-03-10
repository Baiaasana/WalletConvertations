package com.example.walletconvertation.presentation.fragments.convert

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.backend.common.Resource
import com.example.backend.data.model.WalletModel
import com.example.backend.repository.course.CourseRepository
import com.example.backend.repository.wallets.WalletsRepository
import com.example.walletconvertation.common.CourseSymbols
import com.example.walletconvertation.common.ErrorEnum
import com.example.walletconvertation.common.Utility
import com.example.walletconvertation.common.customs.walletView.WalletViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class ConvertViewModel @Inject constructor(
    private val courseRepository: CourseRepository,
    ) : ViewModel(), Utility {

    private val _rate = MutableLiveData<Float>(1F)
    val rate: LiveData<Float?> = _rate

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _errorMessage = MutableLiveData("")
    val errorMessage: LiveData<String?> = _errorMessage

    val amountFrom = MutableLiveData("")
    val amountTo = MutableLiveData("")

    private val _etEnable = MutableLiveData(true)
    val etEnable: LiveData<Boolean> = _etEnable

    private val _walletEnable = MutableLiveData(true)
    val walletEnable: LiveData<Boolean> = _walletEnable

    private val _courseVisibility = MutableLiveData(true)
    val courseVisibility = _courseVisibility

    private val _selectedWalletFrom = MutableLiveData(WalletModel())
    val selectedWalletFrom: LiveData<WalletModel?> = _selectedWalletFrom

    private val _selectedWalletTo = MutableLiveData(WalletModel())
    val selectedWalletTo: LiveData<WalletModel?> = _selectedWalletTo

    init {
        getCourse(selectedWalletFrom.value!!.currency.toString(),selectedWalletTo.value!!.currency.toString() )
    }
    fun getCourse(from: String, to: String) {
        _loading.postValue(true)
        viewModelScope.launch {
            val result = courseRepository.getCourse(from, to).value
            when (result!!.status) {
                Resource.Status.SUCCESS -> {
                    result.data?.rate?.let {
                        _rate.value = it
                        _errorMessage.value = ""
                    }
                    result.data?.rate ?: kotlin.run {
                        _errorMessage.value = "სერვისი არ არის ხელმისაწვდომი"
                    }
                }
                Resource.Status.ERROR -> {
                    _errorMessage.value = result.message.toString()
                }
            }
            disable(errorMessage.value)
            _loading.postValue(false)
        }
    }

    fun selectFromWallet(fromWallet: WalletModel){
        _selectedWalletFrom.value = fromWallet
    }

    fun selectToWallet(toWallet: WalletModel){
        _selectedWalletTo.value = toWallet
    }

    fun setCourseSymbol(course: String): String {

        return when (course) {
            CourseSymbols.RUB.name -> CourseSymbols.RUB.symbol
            CourseSymbols.USD.name -> CourseSymbols.USD.symbol
            CourseSymbols.EUR.name -> CourseSymbols.EUR.symbol
            else -> CourseSymbols.GEL.symbol
        }
    }

    fun convertFROMTO() {
        amountTo.value = rate.value?.toFloat()?.let {
            if (amountFrom.value!!.isNotEmpty()) {
                amountFrom.value!!.toFloat().times(it).toString()
            } else {
                ""
            }
        }.toString()
    }

    fun convertTOFROM() {
        amountFrom.value = rate.value?.toFloat()
            ?.let {
                if (amountTo.value!!.isNotEmpty()) {
                    amountTo.value!!.toFloat().div(it).toString()
                } else {
                    ""
                }
            }.toString()
    }

    fun checkAmount(amount: String, selectedAmount: String): Boolean{
        if (amount.isNotEmpty()){
            if(selectedAmount.toFloat().minus(amount.toFloat())  >= 0){
                _errorMessage.value = ""
                return true
            }else{
                _errorMessage.value = "შეიყვანეთ ვალიდური თანხა"
            }
        }
        return false
    }

    fun clearFields() {
        amountFrom.value = ""
        amountTo.value = ""
    }

     fun disable(error: String?) {
        when (error.toString()) {
            ErrorEnum.ERROR.error.toString() -> {
                _etEnable.value = ErrorEnum.ERROR.boolean
                _courseVisibility.value = ErrorEnum.ERROR.boolean
            }
            ErrorEnum.ERROR_NULL.error.toString() -> {
                _etEnable.value = ErrorEnum.ERROR_NULL.boolean
                _walletEnable.value = ErrorEnum.ERROR_NULL.boolean
                _courseVisibility.value = ErrorEnum.ERROR_NULL.boolean
            }
            ErrorEnum.SERVER_ERROR.error.toString() -> {
                _etEnable.value = ErrorEnum.SERVER_ERROR.boolean
                _walletEnable.value = ErrorEnum.SERVER_ERROR.boolean
                _courseVisibility.value = ErrorEnum.SERVER_ERROR.boolean
            }
            ErrorEnum.NO_ERROR.error.toString() -> {
                _etEnable.value = ErrorEnum.NO_ERROR.boolean
                _walletEnable.value = ErrorEnum.NO_ERROR.boolean
                _courseVisibility.value = ErrorEnum.NO_ERROR.boolean
            }
            else -> {
                _etEnable.value = ErrorEnum.NO_ERROR.boolean
                _walletEnable.value = ErrorEnum.NO_ERROR.boolean
                _courseVisibility.value = ErrorEnum.NO_ERROR.boolean
            }
        }
    }
}
