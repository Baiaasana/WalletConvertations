package com.example.walletconvertation.presentation.fragments.convert

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import com.example.walletconvertation.R
import com.example.walletconvertation.common.Utility
import com.example.walletconvertation.databinding.FragmentConvertBinding
import dagger.hilt.android.AndroidEntryPoint
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent.setEventListener
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener


@AndroidEntryPoint
class ConvertFragment : Fragment(), Utility {

    private var _binding: FragmentConvertBinding? = null
    private val binding get() = _binding!!

    private val convertViewModel: ConvertViewModel by hiltNavGraphViewModels(R.id.main_navigation_graph)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentConvertBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = convertViewModel

        handleKeyboardEvent()

        // keyboard event with high

//        binding.root.viewTreeObserver.addOnGlobalLayoutListener(OnGlobalLayoutListener {
//            val r = Rect()
//            view.getWindowVisibleDisplayFrame(r)
//            val heightDiff: Int = view.rootView.height - (r.bottom - r.top)
//            if (heightDiff < 0) { // if more than 100 pixels, its probably a keyboard...
//                Toast.makeText(context, "hide", Toast.LENGTH_SHORT).show()
//            }
//        })

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
            override fun afterTextChanged(p0: Editable?) {
                p0?.let {
                    convertViewModel.convertFROMTO()
                    binding.btnContinue.isEnabled = convertViewModel.checkAmount()
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
        }

        val amountToWatcher = object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                p0?.let {
                    convertViewModel.convertTOFROM()
                    binding.btnContinue.isEnabled = convertViewModel.checkAmount()
                }
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
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
                            val amount = convertViewModel.amountFrom.value.toString().toDouble()
                            val formattedAmount = kotlinStringFormat(amount, 2)
                            convertViewModel.amountFrom.value = formattedAmount
                        }
                    }
                }
            }
        })
    }
}
