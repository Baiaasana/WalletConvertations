package com.example.walletconvertation.presentation.fragments.convert

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.DigitsKeyListener
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnAttach
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
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

//    private val walletsViewModel : WalletViewModel by activityViewModels()
    private val viewModel: ConvertViewModel by hiltNavGraphViewModels(R.id.main_navigation_graph)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentConvertBinding.inflate(inflater, container, false)
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
        listeners()
        init()
    }

    private fun init(){
        binding.walletFrom.doOnAttach { binding.walletFrom.setData("from") }
        binding.walletTo.doOnAttach { binding.walletTo.setData("to") }
    }

    private fun listeners() {

        binding.etAmountFrom.getAmount().setOnFocusChangeListener { view, hasFocused ->
            if (hasFocused) {
                binding.etAmountFrom.getAmount().addTextChangedListener(amountFromWatcher)
                binding.etAmountTo.getAmount().removeTextChangedListener(amountToWatcher)
            }
        }

        binding.etAmountTo.getAmount().setOnFocusChangeListener { view, hasFocused ->
            if (hasFocused) {
                binding.etAmountTo.getAmount().addTextChangedListener(amountToWatcher)
                binding.etAmountFrom.getAmount().removeTextChangedListener(amountFromWatcher)
            }
        }
//
//        binding.btnReverse.setOnClickListener {
//            walletsViewModel.reverseWallets()
//            viewModel.clearFields()
//            binding.etAmountTo.getAmount().isClickable = false
//            binding.etAmountFrom.getAmount().isClickable = false
//            viewModel.getCourse(
//                walletsViewModel.selectedWalletFrom.value!!.currency.toString(),
//                walletsViewModel.selectedWalletTo.value!!.currency.toString()
//            )
//        }

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
//                    binding.btnContinue.isEnabled = viewModel.checkAmount(viewModel.amountFrom.value.toString(),walletsViewModel.selectedWalletFrom.value?.balance.toString())
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
//                    binding.btnContinue.isEnabled = viewModel.checkAmount(viewModel.amountTo.value.toString(),walletsViewModel.selectedWalletTo.value?.balance.toString())
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
}
