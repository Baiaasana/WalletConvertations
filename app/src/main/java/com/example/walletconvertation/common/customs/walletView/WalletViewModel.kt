package com.example.walletconvertation.common.customs.walletView

import android.database.CursorWindowAllocationException
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.*
import com.example.backend.common.Resource
import com.example.backend.data.model.WalletModel
import com.example.backend.repository.course.CourseRepository
import com.example.backend.repository.wallets.WalletsRepository
import com.example.walletconvertation.R
import com.example.walletconvertation.common.Utility
import com.example.walletconvertation.presentation.fragments.convert.ConvertViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.launch
import java.util.Currency
import javax.inject.Inject

@HiltViewModel
class WalletViewModel @Inject constructor(
    private val walletsRepository: WalletsRepository, courseRepository: CourseRepository,
) : Utility, ConvertViewModel(courseRepository) {

    private val _loading = MutableLiveData<Boolean>()
    val loading1: LiveData<Boolean> = _loading

    private val _errorMessage = MutableLiveData("")
    val errorMessage1: LiveData<String?> = _errorMessage

    private val _walletsFrom = MutableLiveData<List<WalletModel>?>()
    val walletFrom: LiveData<List<WalletModel>?> = _walletsFrom

    private val _walletsTo = MutableLiveData<List<WalletModel>?>()
    val walletsTo: LiveData<List<WalletModel>?> = _walletsTo

    private val _selectedWalletFrom = MutableLiveData(WalletModel())
    val selectedWalletFrom: LiveData<WalletModel?> = _selectedWalletFrom

    private val _selectedWalletTo = MutableLiveData(WalletModel())
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

    private fun getWallets() {
        _loading.postValue(true)
        viewModelScope.launch {
            val result = walletsRepository.getWallets().value
            when (result!!.status) {
                Resource.Status.SUCCESS -> {
                    result.data?.let {walletList ->

                        val dataFrom = ArrayList(walletList.map { it.copy() })
                        val dataTo = ArrayList(walletList.map { it.copy() })

                        val firstWallet = dataFrom.findLast { it.is_default == true }
                        val secondWallet = dataTo.findLast { it.id == (firstWallet!!.id!!.plus(1)) }
                        secondWallet?.let { it.enable = false }
                        _selectedWalletFrom.value = firstWallet
                        _selectedWalletTo.value = secondWallet

                        dataFrom.find { it.id == firstWallet!!.id }!!.is_selected_from = true
                        dataTo.find { it.id == secondWallet!!.id }!!.is_selected_to = true

                        _walletsFrom.value = dataFrom
                        _walletsTo.value = dataTo

                        getCourse(
                            selectedWalletFrom.value!!.currency.toString(),
                            selectedWalletTo.value!!.currency.toString()
                        )

                    } ?: kotlin.run {
                        _errorMessage.postValue("სერვისი არ არის ხელმისაწვდომი")
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

    fun updateFromData(updatedData: List<WalletModel>) {
        _walletsFrom.value = updatedData
    }

    fun updateToData(updatedData: List<WalletModel>) {
        _walletsTo.value = updatedData
    }

    fun reverseWallets() {
        val from = _selectedWalletFrom.value
        val to = _selectedWalletTo.value
        _selectedWalletFrom.value = to
        _selectedWalletTo.value = from
    }
}
