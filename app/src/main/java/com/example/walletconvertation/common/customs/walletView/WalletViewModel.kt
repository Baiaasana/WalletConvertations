package com.example.walletconvertation.common.customs.walletView

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.backend.common.Resource
import com.example.backend.data.model.WalletModel
import com.example.backend.repository.wallets.WalletsRepository
import com.example.walletconvertation.common.Utility
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WalletViewModel @Inject constructor(
    private val walletsRepository: WalletsRepository) : Utility, ViewModel(), WalletCallback {

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _errorMessage = MutableLiveData("")
    val errorMessage: LiveData<String?> = _errorMessage

    private val _walletsFrom = MutableLiveData<List<WalletModel>?>()
    val walletsFrom: LiveData<List<WalletModel>?> = _walletsFrom

    private val _walletsTo = MutableLiveData<List<WalletModel>?>()
    val walletsTo: LiveData<List<WalletModel>?> = _walletsTo

    private val _selectedWalletFrom = MutableLiveData(WalletModel())
    val selectedWalletFrom: LiveData<WalletModel?> = _selectedWalletFrom

    private val _selectedWalletTo = MutableLiveData(WalletModel())
    val selectedWalletTo: LiveData<WalletModel?> = _selectedWalletTo

    init {
        getWallets()
    }

    private fun getWallets() {
        _loading.postValue(true)
        onLoadingStateChanged(true)
        viewModelScope.launch {
            val result = walletsRepository.getWallets().value
            when (result!!.status) {
                Resource.Status.SUCCESS -> {
                    result.data?.let {walletList ->

                        val dataFrom = ArrayList(walletList.map { it.copy() })
                        val dataTo = ArrayList(walletList.map { it.copy() })
                        val firstWallet = dataFrom.findLast { it.is_default == true }
                        val secondWallet = dataTo.findLast { it.id == (firstWallet!!.id!!.plus(1)) }

                        onSelectedWalletFromChanged(firstWallet!!)
                        onSelectedWalletToChanged(secondWallet!!)

                        dataFrom.find { it.id == firstWallet!!.id }!!.is_selected_from = true
                        dataTo.find { it.id == secondWallet!!.id }!!.is_selected_to = true

                        onWalletsFromChanged(dataFrom)
                        onWalletsToChanged(dataTo)

                    } ?: kotlin.run {
                        _errorMessage.postValue("სერვისი არ არის ხელმისაწვდომი")
                    }
                }
                Resource.Status.ERROR -> {
                    _errorMessage.value = result.message.toString()
                }
            }
            _loading.postValue(false)
            onLoadingStateChanged(false)
        }
    }

    fun setCourseSymbol(course: String) = setSymbol(course)

    fun reverseWallets() {
        val from = _selectedWalletFrom.value
        val to = _selectedWalletTo.value
        onSelectedWalletFromChanged(to!!)
        onSelectedWalletToChanged(from!!)
    }

    override fun onSelectedWalletFromChanged(selectedWalletFrom: WalletModel) {
        super.onSelectedWalletFromChanged(selectedWalletFrom)
        _selectedWalletFrom.value = selectedWalletFrom
    }

    override fun onSelectedWalletToChanged(selectedWalletTo: WalletModel) {
        super.onSelectedWalletToChanged(selectedWalletTo)
        _selectedWalletTo.value = selectedWalletTo
    }

    override fun onWalletsFromChanged(walletsFromList: List<WalletModel>) {
        super.onWalletsFromChanged(walletsFromList)
        _walletsFrom.value = walletsFromList
    }

    override fun onWalletsToChanged(walletsToList: List<WalletModel>) {
        super.onWalletsToChanged(walletsToList)
        _walletsTo.value = walletsToList
    }

    override fun onLoadingStateChanged(loading: Boolean) {
        super.onLoadingStateChanged(loading)
        _loading.value = loading
    }
}
