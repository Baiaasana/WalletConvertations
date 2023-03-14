package com.example.walletconvertation.common.customs.walletView

import com.example.backend.data.model.WalletModel

interface WalletCallback {
    fun onWalletsFromChanged(walletsFromList: List<WalletModel>){}
    fun onWalletsToChanged(walletsToList: List<WalletModel>){}
    fun onSelectedWalletFromChanged(selectedWalletFrom: WalletModel){}
    fun onSelectedWalletToChanged(selectedWalletTo: WalletModel){}
    fun onLoadingStateChanged(loading: Boolean){}
    fun onError(errorMessage: String){}
    fun onReverse(selectedWalletFrom: WalletModel, selectedWalletTo: WalletModel){}

}