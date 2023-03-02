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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConvertViewModel @Inject constructor(
    private val courseRepository: CourseRepository,
    private val walletsRepository: WalletsRepository
) : ViewModel(), Utility {

    private val _rate = MutableLiveData(1F)
    val rate: LiveData<Float?> = _rate

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _errorMessage = MutableLiveData("")
    val errorMessage: LiveData<String?> = _errorMessage

    private val _wallets = MutableLiveData<List<WalletModel>?>()
    val wallets: LiveData<List<WalletModel>?> = _wallets

    private val _walletLoading = MutableLiveData<Boolean>()

    val amountFrom = MutableLiveData("")
    val amountTo = MutableLiveData("")

    private val _etEnable = MutableLiveData(true)
    val etEnable: LiveData<Boolean> = _etEnable

    private val _walletEnable = MutableLiveData(false)
    val walletEnable: LiveData<Boolean> = _walletEnable

    private val _selectedWalletFrom = MutableLiveData(WalletModel())
    val selectedWalletFrom: LiveData<WalletModel?> = _selectedWalletFrom

    private val _selectedWalletTo =
        MutableLiveData(WalletModel())
    val selectedWalletTo: LiveData<WalletModel?> = _selectedWalletTo

    init {
        getWallets()
    }

    fun selectWalletFrom(walletModel: WalletModel) {
        _selectedWalletFrom.value = walletModel
    }

    fun selectWalletTo(walletModel: WalletModel) {
        _selectedWalletTo.value = walletModel
    }

     fun getCourse(from: String, to: String) {
        _loading.postValue(true)
        viewModelScope.launch {
            val result = courseRepository.getCourse(from, to).value
            when (result!!.status) {
                Resource.Status.SUCCESS -> {
                    result.data?.rate.let {
                        _rate.postValue(it)
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
            disable()
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
                        val data = it
                        _wallets.value = data
                        _selectedWalletFrom.value =
                            data.findLast { element -> element.is_default == true }
                        _selectedWalletTo.value =
                            data.findLast { element ->
                                element.id == (_selectedWalletFrom.value!!.id!!.plus(
                                    1
                                ))
                            }
                        getCourse(
                            _selectedWalletFrom.value!!.currency.toString(),
                            _selectedWalletTo.value!!.currency.toString()
                        )
                    }
                    result.data ?: kotlin.run {
                        _errorMessage.postValue("სერვისი არ არის ხელმისაწვდომი")
                    }
                }
                Resource.Status.ERROR -> {
                    _errorMessage.value = result.message.toString()
                }
            }
            disable()
            _loading.postValue(false)
        }
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

    fun checkAmount(): Boolean{
        if (amountFrom.value!!.isNotEmpty()){
            if(selectedWalletFrom.value!!.balance!!.toFloat().minus(amountFrom.value!!.toFloat())  >= 0){
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

    fun reverseWallets() {
        val from = _selectedWalletFrom.value
        val to = _selectedWalletTo.value
        _selectedWalletFrom.value = to
        _selectedWalletTo.value = from
        clearFields()
        getCourse(
            selectedWalletFrom.value!!.currency.toString(),
            selectedWalletTo.value!!.currency.toString()
        )
    }

    private fun disable() {
        when (_errorMessage.value.toString()) {
            ErrorEnum.ERROR.error.toString() -> _etEnable.value = ErrorEnum.ERROR.boolean
            ErrorEnum.ERROR_NULL.error.toString() -> {
                _etEnable.value = ErrorEnum.ERROR_NULL.boolean
                _walletEnable.value = ErrorEnum.ERROR_NULL.boolean
            }
            ErrorEnum.SERVER_ERROR.error.toString() -> {
                _etEnable.value = ErrorEnum.SERVER_ERROR.boolean
                _walletEnable.value = ErrorEnum.SERVER_ERROR.boolean
            }
            ErrorEnum.NO_ERROR.error.toString() -> {
                _etEnable.value = ErrorEnum.NO_ERROR.boolean
                _walletEnable.value = ErrorEnum.NO_ERROR.boolean
            }
            else -> {
                _etEnable.value = ErrorEnum.NO_ERROR.boolean
                _walletEnable.value = ErrorEnum.NO_ERROR.boolean
            }
        }
    }
}
