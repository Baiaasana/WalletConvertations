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
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
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

    private val walletsViewModel : WalletViewModel by activityViewModels()
    private val convertsViewModel: ConvertViewModel by hiltNavGraphViewModels(R.id.main_navigation_graph)

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
            convertViewModel = convertsViewModel
            walletViewModel = walletsViewModel
        }
        handleKeyboardEvent()
        listeners()

    }

    @SuppressLint("ClickableViewAccessibility")
    private fun listeners() {
        binding.walletFrom.setOnClickListener {
            convertsViewModel.clearFields()
            findNavController().navigate(
                ConvertFragmentDirections.actionConvertFragmentToWalletsFragment(
                    "from"
                )
            )
        }

        binding.walletTo.setOnClickListener {
            convertsViewModel.clearFields()
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
                    convertsViewModel.amountFrom.value = p0.toString().replace(",", ".")

                    if (convertsViewModel.amountFrom.value!!.contains(".")) {
                        binding.etAmountFrom.getAmount().keyListener =
                            DigitsKeyListener.getInstance("0123456789")
                    } else {
                        binding.etAmountFrom.getAmount().keyListener =
                            DigitsKeyListener.getInstance("0123456789.,")
                    }
                    convertsViewModel.convertFROMTO()
                    binding.btnContinue.isEnabled = convertsViewModel.checkAmount(convertsViewModel.amountFrom.value.toString(),walletsViewModel.selectedWalletFrom.value?.balance.toString())
                }
            }
        }

        val amountToWatcher = object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                p0?.let {
                    convertsViewModel.amountTo.value = p0.toString().replace(",", ".")
                    if (convertsViewModel.amountTo.value!!.contains(".")) {
                        binding.etAmountTo.getAmount().keyListener =
                            DigitsKeyListener.getInstance("0123456789")
                    } else {
                        binding.etAmountTo.getAmount().keyListener =
                            DigitsKeyListener.getInstance("0123456789.,")
                    }
                    convertsViewModel.convertTOFROM()
                    binding.btnContinue.isEnabled = convertsViewModel.checkAmount(convertsViewModel.amountTo.value.toString(),walletsViewModel.selectedWalletTo.value?.balance.toString())
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
            walletsViewModel.reverseWallets()
            convertsViewModel.clearFields()
            binding.etAmountTo.getAmount().isClickable = false
            binding.etAmountFrom.getAmount().isClickable = false
            convertsViewModel.getCourse(
                walletsViewModel.selectedWalletFrom.value!!.currency.toString(),
                walletsViewModel.selectedWalletTo.value!!.currency.toString()
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
                    convertsViewModel.amountFrom.value.let {
                        if (it!!.isNotEmpty()) {
                            val amount =
                                convertsViewModel.amountFrom.value.toString().replace(",", ".")
                                    .toDouble()
                            val formattedAmount = kotlinStringFormat(amount, 2)
                            convertsViewModel.amountFrom.value = formattedAmount
                        }
                    }
                    convertsViewModel.amountTo.value.let {
                        if (it!!.isNotEmpty()) {
                            val amount =
                                convertsViewModel.amountTo.value.toString().replace(",", ".")
                                    .toDouble()
                            val formattedAmount = kotlinStringFormat(amount, 2)
                            convertsViewModel.amountTo.value = formattedAmount
                        }
                    }
                }
            }
        })
    }
}
