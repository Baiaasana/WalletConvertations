package com.example.walletconvertation.presentation.fragments.convert

import android.util.Log
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

    private val _rate = MutableLiveData<Float?>(1F)
    val rate: LiveData<Float?> = _rate

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _errorMessage = MutableLiveData<String?>(null)
    val errorMessage: LiveData<String?> = _errorMessage

    private val _wallets = MutableLiveData<List<WalletModel>?>()
    val wallets: LiveData<List<WalletModel>?> = _wallets

    private val _walletLoading = MutableLiveData<Boolean>()
    val walletLoading: LiveData<Boolean> = _walletLoading

    private val _walletsErrorMessage = MutableLiveData<String?>()
    val walletsErrorMessage: LiveData<String?> = _walletsErrorMessage

    val amountFrom = MutableLiveData<String>("")
    val amountTo = MutableLiveData<String>("")

    init {
        getWallets()
    }

    private val _selectedWalletFrom =
        MutableLiveData<WalletModel?>(WalletModel())
    val selectedWalletFrom: LiveData<WalletModel?> = _selectedWalletFrom

    private val _selectedWalletTo =
        MutableLiveData<WalletModel?>(WalletModel())
    val selectedWalletTo: LiveData<WalletModel?> = _selectedWalletTo

    init {
//        getWallets()
        getCourse(
            selectedWalletFrom.value!!.currency.toString(),
            selectedWalletTo.value!!.currency.toString()
        )
    }

    fun selectWalletFrom(walletModel: WalletModel) {
        _selectedWalletFrom.value = walletModel
        Log.d("select wallet", "from ".plus(selectedWalletFrom.value))
    }

    fun selectWalletTo(walletModel: WalletModel) {
        _selectedWalletTo.value = walletModel
        Log.d("select wallet", "to ".plus(selectedWalletTo.value))
    }

     fun getCourse(from: String, to: String) {
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
                        val data = it
                        _wallets.value = data
                        _selectedWalletFrom.value =
                            data.findLast { element -> element.is_default == true }
                        _selectedWalletTo.value =
                            data.findLast { element -> element.is_default == true }

                        Log.d(
                            "wallet",
                            "data's wallet ".plus(data.findLast { element -> element.is_default == true })
                        )

                        Log.d(" wallets", "value ".plus(_wallets.value))

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


    fun setFromSymbol(): String {
        return setSymbol(_selectedWalletFrom.value!!.currency.toString())
    }

    fun setToSymbol(): String {
        return setSymbol(_selectedWalletTo.value!!.currency.toString())
    }

    fun setdSymbol(course: String): String {

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
                return true
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
        getCourse(
            selectedWalletFrom.value!!.currency.toString(),
            selectedWalletTo.value!!.currency.toString()
        )
    }

    // tu servisi ar aris edittextebi disable
    // keyboard null hide ?

    fun disable(): Boolean {
        return when (_errorMessage.value.toString()) {
            ErrorEnum.ERROR.error.toString() -> ErrorEnum.ERROR.boolean
            ErrorEnum.NO_ERROR.error.toString() -> ErrorEnum.NO_ERROR.boolean

            else -> {
                ErrorEnum.NO_ERROR.boolean
            }
        }
    }
}
