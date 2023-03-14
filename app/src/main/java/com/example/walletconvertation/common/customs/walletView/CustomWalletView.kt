package com.example.walletconvertation.common.customs.walletView

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.databinding.BindingAdapter
import androidx.lifecycle.*
import androidx.navigation.findNavController
import com.example.backend.data.model.WalletModel
import com.example.walletconvertation.common.CourseSymbols
import com.example.walletconvertation.databinding.CustomWalletViewBinding
import com.example.walletconvertation.presentation.fragments.convert.ConvertFragmentDirections

class CustomWalletView(context: Context, attrs: AttributeSet?) : LinearLayout(context, attrs) {

    private val binding = CustomWalletViewBinding.inflate(LayoutInflater.from(context), this, true)
    private val viewModel by lazy {
        ViewModelProvider(findViewTreeViewModelStoreOwner()!!).get<WalletViewModel>()
    }

    private var callback: WalletCallback? = null
    fun setCallBack(listener: WalletCallback) {
        callback = listener
    }
    init {
        binding.lifecycleOwner = findViewTreeLifecycleOwner()

        getWalletView().setOnClickListener {
            val currency = getCurrency().text.toString()
            if (currency == setSymbol(viewModel.selectedWalletFrom.value!!.currency.toString())) {
                onWalletClick("from")
            }
            if (currency == setSymbol(viewModel.selectedWalletTo.value!!.currency.toString())) {
                onWalletClick("to")
            }
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        findViewTreeLifecycleOwner()?.lifecycle?.addObserver(object : DefaultLifecycleObserver {
            override fun onCreate(owner: LifecycleOwner) {
                super.onCreate(owner)
                binding.viewModel = viewModel
            }
        })
    }

    private fun setSymbol(course: String): String {

        return when (course) {
            CourseSymbols.RUB.name -> CourseSymbols.RUB.symbol
            CourseSymbols.USD.name -> CourseSymbols.USD.symbol
            CourseSymbols.EUR.name -> CourseSymbols.EUR.symbol
            else -> CourseSymbols.GEL.symbol
        }
    }

    fun getWalletViewModel() : WalletViewModel {
        return viewModel
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
        when (walletType) {
            "from" -> {
                viewModel.selectedWalletFrom.observe(findViewTreeLifecycleOwner()!!) { wallet ->
                    binding.wallet = wallet
                    wallet?.let { callback!!.onSelectedWalletFromChanged(it) }
                }
            }
            "to" -> {
                viewModel.selectedWalletTo.observe(findViewTreeLifecycleOwner()!!) { wallet ->
                    binding.wallet = wallet
                    wallet?.let { callback!!.onSelectedWalletToChanged(it) }
                }
            }
        }
    }

    fun updateWalletFrom(wallet: WalletModel){
        viewModel.onSelectedWalletFromChanged(wallet)
    }

    fun firstUpdate(){
        viewModel.onSelectedWalletFromChanged(viewModel.selectedWalletFrom.value!!)
        viewModel.onSelectedWalletToChanged(viewModel.selectedWalletTo.value!!)
    }

    fun updateWalletsFrom(walletList: List<WalletModel>){
        viewModel.onWalletsFromChanged(walletList)
    }

    fun updateWalletsTo(walletList: List<WalletModel>){
        viewModel.onWalletsToChanged(walletList)
    }

    fun updateWalletTo(wallet: WalletModel){
        viewModel.onSelectedWalletToChanged(wallet)
    }

    private fun getWalletView(): LinearLayoutCompat {
        return binding.walletLayout
    }

    private fun getCurrency(): AppCompatTextView {
        return binding.tvCurrencyWallet
    }
}

@BindingAdapter("wallet_enabled")
fun setDisable(view: CustomWalletView, boolean: Boolean) {
    view.isEnabled = boolean
}

@BindingAdapter("wallet_data")
fun setData(view: CustomWalletView, boolean: Boolean){
    if(boolean){
        view.setData("from")
    }else{
        view.setData("to")
    }
}





