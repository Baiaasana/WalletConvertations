package com.example.walletconvertation.common.customs.walletView

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.view.doOnAttach
import androidx.databinding.BindingAdapter
import androidx.lifecycle.*
import androidx.navigation.findNavController
import com.example.backend.data.model.WalletModel
import com.example.walletconvertation.R
import com.example.walletconvertation.common.Utility
import com.example.walletconvertation.databinding.CustomWalletViewBinding
import com.example.walletconvertation.presentation.fragments.convert.ConvertFragmentDirections

class CustomWalletView(context: Context, attrs: AttributeSet?) : LinearLayout(context, attrs),
    Utility, WalletCallback {

    private val walletImage: AppCompatImageView
    private val walletTitle: AppCompatTextView
    private val accountNumber: AppCompatTextView
    private val amount: AppCompatTextView
    private val currency: AppCompatTextView
    private val endIcon: AppCompatImageView
    private val walletView: LinearLayoutCompat

    private val binding: CustomWalletViewBinding =
        CustomWalletViewBinding.inflate(LayoutInflater.from(context), this, true)

    //        private val viewModel =
//        ViewModelProvider(findViewTreeViewModelStoreOwner()!!).get<WalletViewModel>()

//    private val viewModel by lazy(LazyThreadSafetyMode.NONE) {
//        ViewModelProvider(findViewTreeViewModelStoreOwner()!!)[WalletViewModel::class.java]
//    }

    private val viewModel by lazy {
        val viewModelStoreOwner = checkNotNull(findViewTreeViewModelStoreOwner())
        ViewModelProvider(viewModelStoreOwner).get<WalletViewModel>()
    }


    init {
        val view = inflate(context, R.layout.custom_wallet_view, this)
        walletImage = binding.ivWallet
        walletTitle = binding.tvTitle
        accountNumber = binding.tvAccountNumber
        amount = binding.tvAmountWallet
        currency = binding.tvCurrencyWallet
        endIcon = binding.ivEndIcon
        walletView = binding.walletLayout

        getLifeCycleOwner(this)?.let {
            binding.lifecycleOwner = it
        }

        doOnAttach {
            setViewModel(viewModel)
            binding.viewModel = viewModel
        }

        walletView.setOnClickListener {
            val currency = getCurrency().text.toString()
            if (currency == setSymbol(viewModel.selectedWalletFrom.value!!.currency.toString())) {
                onWalletClick("from")
            }
            if (currency == setSymbol(viewModel.selectedWalletTo.value!!.currency.toString())) {
                onWalletClick("to")
            }
        }
    }

    private var callback: WalletCallback? = null
    fun setCallBack(callback: WalletCallback) {
        this.callback = callback
    }

    private fun onWalletClick(walletType: String) {
        if (walletType == "from") {
            findNavController().navigate(
                ConvertFragmentDirections.actionConvertFragmentToWalletsFragment(
                    walletType = walletType,
                    fromList = viewModel.walletsFrom.value!!.toTypedArray(),
                    toList = viewModel.walletsTo.value!!.toTypedArray()
                )
            )
        }
        if (walletType == "to") {
            findNavController().navigate(
                ConvertFragmentDirections.actionConvertFragmentToWalletsFragment(
                    walletType = walletType,
                    fromList = viewModel.walletsFrom.value!!.toTypedArray(),
                    toList = viewModel.walletsTo.value!!.toTypedArray()
                )
            )
        }
    }

    fun setData(walletType: String) {
        if (walletType == "from") {
            binding.wallet = viewModel.selectedWalletFrom.value
        }
        if (walletType == "to") {
            binding.wallet = viewModel.selectedWalletTo.value
        }
    }

    fun selectFromWallet(walletType: String, callback: WalletCallback, wallet: WalletModel) {
        if (walletType == "from") {
            callback.onSelectedWalletFromChanged(wallet)
        }
        if (walletType == "to") {
            callback.onSelectedWalletToChanged(wallet)
            viewModel.selectWalletTo(wallet)
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

    }

//    fun getWalletViewModel(): WalletViewModel {
//        this.viewModel.let { return viewModel }
//    }

    override fun onSelectedWalletFromChanged(wallet: WalletModel) {
        viewModel.selectWalletFrom(wallet)
    }

    fun selectToWallet(selectedWalletTo: WalletModel) {

        if (callback != null) {
            viewModel.selectedWalletTo.observe(findViewTreeLifecycleOwner()!!) { wallet ->
                binding.wallet = wallet
            }
            callback!!.onSelectedWalletToChanged(selectedWalletTo)
            viewModel.selectWalletTo(selectedWalletTo)
            binding.wallet = selectedWalletTo
        }
    }

    private fun setViewModel(convertViewModel: WalletViewModel) {
        binding.viewModel = convertViewModel
    }

    fun getAmount(): AppCompatTextView {
        return amount
    }

    fun getTitle(): AppCompatTextView {
        return walletTitle
    }

    fun getAccount(): AppCompatTextView {
        return accountNumber
    }

    fun getCurrency(): AppCompatTextView {
        return currency
    }

    fun getEndIcon() : AppCompatImageView{
        return endIcon
    }
}

@BindingAdapter("walletCurrency")
fun setCurrency(wallet: CustomWalletView, currency: String?) {
    currency.let {
        wallet.getCurrency().text = currency
    }
}

@BindingAdapter("walletAmount")
fun setAmount(wallet: CustomWalletView, amount: String?) {
    amount.let {
        wallet.getAmount().text = amount
    }
}

@BindingAdapter("walletTitle")
fun setTitle(view: CustomWalletView, title: String?) {
    title.let {
        view.getTitle().text = title
    }
}

@BindingAdapter("accountNumber")
fun setAccountNumber(view: CustomWalletView, accountNumber: String?) {
    accountNumber.let {
        view.getAccount().text = accountNumber
    }
}

@BindingAdapter("wallet_enabled")
fun setDisable(view: CustomWalletView, boolean: Boolean) {
    view.isEnabled = boolean
}




