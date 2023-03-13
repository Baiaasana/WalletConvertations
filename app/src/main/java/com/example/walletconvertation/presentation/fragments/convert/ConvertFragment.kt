package com.example.walletconvertation.presentation.fragments.convert

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.DigitsKeyListener
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnAttach
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import com.example.backend.data.model.WalletModel
import com.example.walletconvertation.R
import com.example.walletconvertation.common.Utility
import com.example.walletconvertation.common.customs.walletView.WalletCallback
import com.example.walletconvertation.databinding.FragmentConvertBinding
import dagger.hilt.android.AndroidEntryPoint
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent.setEventListener
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener


@AndroidEntryPoint
class ConvertFragment : Fragment(), Utility, WalletCallback {

    private var _binding: FragmentConvertBinding? = null
    val binding get() = _binding!!

    private val viewModel: ConvertViewModel by hiltNavGraphViewModels(R.id.main_navigation_graph)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding =
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_convert, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            convertViewModel = viewModel
        }
        binding.walletFrom.setCallBack(this)
        binding.walletTo.setCallBack(this)
        handleKeyboardEvent()
        init()
        listeners()
    }

    private fun init() {

        binding.walletFrom.let { wallet ->
            setFragmentResultListener("requestKeyFrom") { _, bundle ->
                val walletFrom = bundle.getParcelable<WalletModel>("walletFrom")
                walletFrom.let {
                    wallet.updateWalletFrom(it!!)
                }
            }
            setFragmentResultListener("requestKeyFromList") { _, bundle ->
                val newList = bundle.getParcelableArrayList<WalletModel>("walletsFrom")
                newList?.let { wallet.updateWalletsFrom(it) }
            }
            wallet.doOnAttach {
                wallet.setData("from")
            }

//            wallet.setCallBack(object : WalletCallback{
//                override fun onSelectedWalletFromChanged(selectedWalletFrom: WalletModel) {
//                    super.onSelectedWalletFromChanged(selectedWalletFrom)
//                    viewModel.selectFromWallet(selectedWalletFrom)
//                    viewModel.getCourse(viewModel.selectedWalletFrom.value!!.currency.toString(),viewModel.selectedWalletTo.value!!.currency.toString() )
//                }
//            })
        }
        binding.walletTo.let { wallet ->
            setFragmentResultListener("requestKeyTo") { _, bundle ->
                val walletTo = bundle.getParcelable<WalletModel>("walletTo")
                walletTo.let {
                    wallet.updateWalletTo(it!!)
                }
            }

            setFragmentResultListener("requestKeyToList") { _, bundle ->
                val newList = bundle.getParcelableArrayList<WalletModel>("walletsTo")
                newList?.let { wallet.updateWalletsTo(it) }
            }

            wallet.doOnAttach {
                wallet.setData("to")
//                onSelectedWalletToChanged(binding.walletTo.getWalletViewModel().selectedWalletTo.value!!)
//                onSelectedWalletFromChanged(binding.walletFrom.getWalletViewModel().selectedWalletFrom.value!!)

            }
        }
    }

    override fun onSelectedWalletFromChanged(selectedWalletFrom: WalletModel) {
        super.onSelectedWalletFromChanged(selectedWalletFrom)
        viewModel.selectFromWallet(selectedWalletFrom)
    }

    override fun onSelectedWalletToChanged(selectedWalletTo: WalletModel) {
        super.onSelectedWalletToChanged(selectedWalletTo)
        viewModel.selectToWallet(selectedWalletTo)
       getCourses()
    }

    private fun getCourses() {
        viewModel.getCourse(
            viewModel.selectedWalletFrom.value!!.currency.toString(),
            viewModel.selectedWalletTo.value!!.currency.toString()
        )
    }

    private fun listeners() {

        binding.etAmountFrom.getAmount().setOnFocusChangeListener { _, hasFocused ->
            if (hasFocused) {
                binding.etAmountFrom.getAmount().addTextChangedListener(amountFromWatcher)
                binding.etAmountTo.getAmount().removeTextChangedListener(amountToWatcher)
            }
        }

        binding.etAmountTo.getAmount().setOnFocusChangeListener { _, hasFocused ->
            if (hasFocused) {
                binding.etAmountTo.getAmount().addTextChangedListener(amountToWatcher)
                binding.etAmountFrom.getAmount().removeTextChangedListener(amountFromWatcher)
            }
        }

        binding.btnReverse.setOnClickListener {
            binding.walletFrom.getWalletViewModel().reverseWallets()
            viewModel.reverseCourses()
            viewModel.clearFields()
            binding.etAmountTo.getAmount().isClickable = false
            binding.etAmountFrom.getAmount().isClickable = false
        }

        binding.root.setOnClickListener {
            it?.let { activity?.hideKeyboard(it) }
        }
    }

    private val amountFromWatcher = object : TextWatcher {
        override fun afterTextChanged(p0: Editable?) {}
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            p0?.let {
                viewModel.amountFrom.value = p0.toString().replace(",", ".")

                if (viewModel.amountFrom.value!!.contains(".")) {
                    binding.etAmountFrom.getAmount().keyListener =
                        DigitsKeyListener.getInstance("0123456789")
                } else {
                    binding.etAmountFrom.getAmount().keyListener =
                        DigitsKeyListener.getInstance("0123456789.,")
                }
                viewModel.convertFROMTO()
                binding.btnContinue.isEnabled = (viewModel.checkAmount(
                    viewModel.amountTo.value.toString(),
                    viewModel.selectedWalletTo.value?.balance.toString()
                ) || (viewModel.checkAmount(
                    viewModel.amountFrom.value.toString(),
                    viewModel.selectedWalletFrom.value?.balance.toString()
                )))
            }
        }
    }

    private val amountToWatcher = object : TextWatcher {
        override fun afterTextChanged(p0: Editable?) {}
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            p0?.let {
                viewModel.amountTo.value = p0.toString().replace(",", ".")
                if (viewModel.amountTo.value!!.contains(".")) {
                    binding.etAmountTo.getAmount().keyListener =
                        DigitsKeyListener.getInstance("0123456789")
                } else {
                    binding.etAmountTo.getAmount().keyListener =
                        DigitsKeyListener.getInstance("0123456789.,")
                }
                viewModel.convertTOFROM()
                binding.btnContinue.isEnabled = (viewModel.checkAmount(
                    viewModel.amountTo.value.toString(),
                    viewModel.selectedWalletTo.value?.balance.toString()
                ) || (viewModel.checkAmount(
                    viewModel.amountFrom.value.toString(),
                    viewModel.selectedWalletFrom.value?.balance.toString()
                )))
            }
        }
    }

    fun kotlinStringFormat(input: Double, scale: Int) = "%.${scale}f".format(input)

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun handleKeyboardEvent(){
        setEventListener(requireActivity(), object : KeyboardVisibilityEventListener {
            override fun onVisibilityChanged(isOpen: Boolean) {
                if (isOpen) {}
                else {
                    viewModel.amountFrom.value.let {
                        if (it!!.isNotEmpty()) {
                            val amount =
                                viewModel.amountFrom.value.toString().replace(",", ".")
                                    .toDouble()
                            val formattedAmount = kotlinStringFormat(amount, 2)
                            viewModel.amountFrom.value = formattedAmount
                        }
                    }
                    viewModel.amountTo.value.let {
                        if (it!!.isNotEmpty()) {
                            val amount =
                                viewModel.amountTo.value.toString().replace(",", ".")
                                    .toDouble()
                            val formattedAmount = kotlinStringFormat(amount, 2)
                            viewModel.amountTo.value = formattedAmount
                        }
                    }
                }
            }
        })
    }

    override fun onPause() {
        super.onPause()
        viewModel.clearFields()
    }
}
