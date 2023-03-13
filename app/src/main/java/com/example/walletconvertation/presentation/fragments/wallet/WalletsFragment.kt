package com.example.walletconvertation.presentation.fragments.wallet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.backend.data.model.WalletModel
import com.example.walletconvertation.R
import com.example.walletconvertation.common.customs.walletView.WalletCallback
import com.example.walletconvertation.databinding.FragmentWalletsBinding
import com.example.walletconvertation.presentation.adapters.WalletAdapter
import com.example.walletconvertation.presentation.fragments.convert.ConvertViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WalletsFragment : Fragment(), WalletCallback {

    private var _binding: FragmentWalletsBinding? = null
    private val binding get() = _binding!!
    private val walletAdapter: WalletAdapter = WalletAdapter()
    private val args: WalletsFragmentArgs by navArgs()

    private val convertViewModel: ConvertViewModel by hiltNavGraphViewModels(R.id.main_navigation_graph)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWalletsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listeners()
        init()
    }

    private fun init(){
        binding.rvWallets.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = walletAdapter
        }

        var data : List<WalletModel> = emptyList()
        when (args.walletType) {
            "from" -> {
                data = args.fromList.toList()
                data.find { it.id == args.toList.toList().find { wallet -> wallet.is_selected_to }?.id.toString().toInt()}?.enable = false
            }
            "to" -> {
                data = args.toList.toList()
                data = data.filterNot { it.id == args.fromList.find { item -> item.is_selected_from }!!.id }
            }
        }
        walletAdapter.submitList(data)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun listeners() {
        binding.toolbar.getBackIcon().setOnClickListener {
            findNavController().navigateUp()
        }

        when (args.walletType) {
            "from" ->{
                walletAdapter.onWalletClickListener = { selectedFrom ->

                    selectedFrom.is_selected_from = true
                    val data = args.fromList.toList()
                    data.filterNot { item -> item.id == selectedFrom.id }
                        .forEach { el -> el.is_selected_from = false }
                    onWalletsFromChanged(data)
                    setFragmentResult("requestKeyFrom", bundleOf("walletFrom" to selectedFrom))
                    setFragmentResult("requestKeyFromList", bundleOf("walletsFrom" to data))
                    convertViewModel.onSelectedWalletFromChanged(selectedFrom)
                    args.toList.find { it.is_selected_to }
                        ?.let { convertViewModel.onSelectedWalletToChanged(it) }
                        .also {
                        convertViewModel.getCourse(
                            convertViewModel.selectedWalletFrom.value!!.currency.toString(),
                            convertViewModel.selectedWalletTo.value!!.currency.toString()
                        )
                    }
                    findNavController().navigateUp()
                }
            }

            "to" -> walletAdapter.onWalletClickListener = { selectedTo ->
                selectedTo.is_selected_to = true
//                selectedTo.enable = false
                val data = args.toList.toList()
                data.filterNot { item -> item.id == selectedTo.id }
                    .forEach { item ->
                        item.is_selected_to = false
                        item.enable = true
                    }
                args.fromList.toList().forEach { item -> item.enable = true }
                onWalletsToChanged(data)
                convertViewModel.onSelectedWalletToChanged(selectedTo)

                setFragmentResult("requestKeyTo", bundleOf("walletTo" to selectedTo))
                setFragmentResult("requestKeyToList", bundleOf("walletsTo" to data))

                args.fromList.find { it.is_selected_from }
                    ?.let { convertViewModel.onSelectedWalletFromChanged(it) }
                    .also {
                    convertViewModel.getCourse(
                        convertViewModel.selectedWalletFrom.value!!.currency.toString(),
                        convertViewModel.selectedWalletTo.value!!.currency.toString()
                    )
                }
                findNavController().navigateUp()
            }
        }
    }
}