package com.example.walletconvertation.presentation.fragments.convert

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.backend.common.Resource
import com.example.backend.data.model.ListOfWallets
import com.example.backend.data.model.WalletModel
import com.example.backend.repository.course.CourseRepository
import com.example.backend.repository.wallets.WalletsRepository
import com.example.walletconvertation.common.CourseSymbols
import com.example.walletconvertation.common.ErrorEnum
import com.example.walletconvertation.common.Utility
import com.google.gson.Gson
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

    private val _wallets_from = MutableLiveData<List<WalletModel>?>()
    val wallets_from: LiveData<List<WalletModel>?> = _wallets_from

    private val _wallets_to = MutableLiveData<List<WalletModel>?>()
    val wallets_to: LiveData<List<WalletModel>?> = _wallets_to

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

                        val dataFrom = ArrayList(it.map { it.copy() })
                        val dataTo = ArrayList(it.map { it.copy() })

                        val firstWallet = dataFrom.findLast { it.is_default == true }
                        val secondWallet = dataTo.findLast { it.id == (firstWallet!!.id!!.plus(1)) }
                        _selectedWalletFrom.value = firstWallet
                        _selectedWalletTo.value = secondWallet

                        dataFrom.find { it.id == firstWallet!!.id }!!.is_selected_from = true
                        dataTo.find { it.id == secondWallet!!.id }!!.is_selected_to = true


                        _wallets_from.value = dataFrom
                        _wallets_to.value = dataTo

                        getCourse(
                            _selectedWalletFrom.value!!.currency.toString(),
                            _selectedWalletTo.value!!.currency.toString()
                        )
                    } ?: kotlin.run {
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

    fun updateFromData(updatedData: List<WalletModel>) {
        _wallets_from.value = updatedData
    }

    fun updateToData(updatedData: List<WalletModel>) {
        _wallets_to.value = updatedData
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
