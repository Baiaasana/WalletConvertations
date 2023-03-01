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
import com.example.walletconvertation.databinding.FragmentConvertBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ConvertFragment : Fragment() {

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

        listeners()

    }

    @SuppressLint("ClickableViewAccessibility")
    private fun listeners() {
        binding.walletFrom.setOnClickListener {
            findNavController().navigate(
                ConvertFragmentDirections.actionConvertFragmentToWalletsFragment(
                    "from"
                )
            )
        }

        binding.walletTo.setOnClickListener {
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
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}