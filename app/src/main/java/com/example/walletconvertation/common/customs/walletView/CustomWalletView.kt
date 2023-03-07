package com.example.walletconvertation.common.customs.walletView

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.view.doOnAttach
import androidx.databinding.BindingAdapter
import androidx.lifecycle.*
import com.example.backend.data.model.WalletModel
import com.example.walletconvertation.R
import com.example.walletconvertation.common.Utility
import com.example.walletconvertation.databinding.CustomWalletViewBinding
import com.example.walletconvertation.presentation.fragments.convert.ConvertFragment

class CustomWalletView(context: Context, attrs: AttributeSet?) : LinearLayout(context, attrs),
    Utility {

    private val walletImage: AppCompatImageView
    private val walletTitle: AppCompatTextView
    private val accountNumber: AppCompatTextView
    private val amount: AppCompatTextView
    private val currency: AppCompatTextView
    private val endIcon: AppCompatImageView

    private val binding: CustomWalletViewBinding =
        CustomWalletViewBinding.inflate(LayoutInflater.from(context), this, true)

    private val viewModel by lazy {
        ViewModelProvider(findViewTreeViewModelStoreOwner()!!).get<WalletViewModel>()
    }

    init {
        val view = inflate(context, R.layout.custom_wallet_view, this)
        walletImage = view.findViewById(R.id.ivWallet)
        walletTitle = view.findViewById(R.id.tvTitle)
        accountNumber = view.findViewById(R.id.tvAccountNumber)
        amount = binding.tvAmountWallet
        currency = binding.tvCurrencyWallet
        endIcon = view.findViewById(R.id.ivEndIcon)

        getLifeCycleOwner(this)?.let {
            binding.lifecycleOwner = it
        }

        doOnAttach {
            setViewModel(viewModel)
            binding.viewModel = viewModel
        }

    }

    private var callback: WalletCallback? = null
    fun setCallBack(callback: WalletCallback){
        this.callback = callback
    }

    fun setEventListener(callback: WalletCallback?) {
        this.callback = callback
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        viewModel.selectedWalletFrom.observe(findViewTreeLifecycleOwner()!!) { wallet ->
            binding.wallet = wallet
        }

    }

    fun getWalletVIewModel(): WalletViewModel{

        this.viewModel.let { return viewModel }
    }

    fun selectFromWallet(selectedWalletFrom: WalletModel) {
        if (callback != null) {
            viewModel.selectedWalletFrom.observe(findViewTreeLifecycleOwner()!!) { wallet ->
                binding.wallet = wallet
            }
            callback!!.onSelectedWalletFromChanged(selectedWalletFrom)
            viewModel.selectWalletFrom(selectedWalletFrom)
        }
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




