package com.example.walletconvertation.presentation.fragments.convert

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.DigitsKeyListener
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.walletconvertation.R
import com.example.walletconvertation.common.Utility
import com.example.walletconvertation.common.customs.walletView.WalletViewModel
import com.example.walletconvertation.databinding.FragmentConvertBinding
import dagger.hilt.android.AndroidEntryPoint
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent.setEventListener
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener


@AndroidEntryPoint
class ConvertFragment : Fragment(), Utility {

    private var _binding: FragmentConvertBinding? = null
    private val binding get() = _binding!!

    private val convertViewModel: ConvertViewModel by hiltNavGraphViewModels(R.id.main_navigation_graph)
//    private val walletViewModel = ViewModelProvider(requireActivity())[WalletViewModel::class.java]

    private val walletViewModel : WalletViewModel by activityViewModels()

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
            convertViewModel = convertViewModel
            walletViewModel = walletViewModel
        }

        handleKeyboardEvent()
        listeners()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun listeners() {
        binding.walletFrom.setOnClickListener {
            convertViewModel.clearFields()
            findNavController().navigate(
                ConvertFragmentDirections.actionConvertFragmentToWalletsFragment(
                    "from"
                )
            )
        }

        binding.walletTo.setOnClickListener {
            convertViewModel.clearFields()
            findNavController().navigate(
                ConvertFragmentDirections.actionConvertFragmentToWalletsFragment(
                    "to"
                )
            )
        }

        val amountFromWatcher = object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                p0?.let {
                    convertViewModel.amountFrom.value = p0.toString().replace(",", ".")

                    if (convertViewModel.amountFrom.value!!.contains(".")) {
                        binding.etAmountFrom.getAmount().keyListener =
                            DigitsKeyListener.getInstance("0123456789")
                    } else {
                        binding.etAmountFrom.getAmount().keyListener =
                            DigitsKeyListener.getInstance("0123456789.,")
                    }
                    convertViewModel.convertFROMTO()
                    binding.btnContinue.isEnabled = convertViewModel.checkAmount(convertViewModel.amountFrom.value.toString(),walletViewModel.selectedWalletFrom.value.toString())
                }
            }
        }

        val amountToWatcher = object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                p0?.let {
                    convertViewModel.amountTo.value = p0.toString().replace(",", ".")
                    if (convertViewModel.amountTo.value!!.contains(".")) {
                        binding.etAmountTo.getAmount().keyListener =
                            DigitsKeyListener.getInstance("0123456789")
                    } else {
                        binding.etAmountTo.getAmount().keyListener =
                            DigitsKeyListener.getInstance("0123456789.,")
                    }
                    convertViewModel.convertTOFROM()
                    binding.btnContinue.isEnabled = convertViewModel.checkAmount(convertViewModel.amountTo.value.toString(),walletViewModel.selectedWalletTo.value.toString())
                }
            }
        }

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

        binding.btnReverse.setOnClickListener {
            walletViewModel.reverseWallets()
            convertViewModel.clearFields()
            binding.etAmountTo.getAmount().isClickable = false
            binding.etAmountFrom.getAmount().isClickable = false
            convertViewModel.getCourse(
                walletViewModel.selectedWalletFrom.value!!.currency.toString(),
                walletViewModel.selectedWalletTo.value!!.currency.toString()
            )
        }


        binding.root.setOnClickListener {
            it?.let { activity?.hideKeyboard(it) }

            // hide manually

//            val view = requireActivity().currentFocus
//            if (view != null) {
//                val inputMethodManager =
//                    requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//                inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
//                Toast.makeText(context, "Key board hidden", Toast.LENGTH_SHORT).show()
//            }
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
                    convertViewModel.amountFrom.value.let {
                        if (it!!.isNotEmpty()) {
                            val amount =
                                convertViewModel.amountFrom.value.toString().replace(",", ".")
                                    .toDouble()
                            val formattedAmount = kotlinStringFormat(amount, 2)
                            convertViewModel.amountFrom.value = formattedAmount
                        }
                    }
                    convertViewModel.amountTo.value.let {
                        if (it!!.isNotEmpty()) {
                            val amount =
                                convertViewModel.amountTo.value.toString().replace(",", ".")
                                    .toDouble()
                            val formattedAmount = kotlinStringFormat(amount, 2)
                            convertViewModel.amountTo.value = formattedAmount
                        }
                    }
                }
            }
        })
    }
}
