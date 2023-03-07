package com.example.walletconvertation.presentation.fragments.wallet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.backend.data.model.WalletModel
import com.example.walletconvertation.R
import com.example.walletconvertation.common.customs.walletView.WalletCallback
import com.example.walletconvertation.common.customs.walletView.WalletViewModel
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

//    private val walletViewModel : WalletViewModel by activityViewModels()

    var listFrom: List<WalletModel> = emptyList()
    var listTo: List<WalletModel> = emptyList()
    var selectedFrom: WalletModel = WalletModel()
    var selectedTo: WalletModel = WalletModel()

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
                data = listFrom
                data.find { it.id == listTo.find { !it.enable }?.id.toString().toInt()}?.enable = false
            }
            "to" -> {
                data = listTo
                data = data.filterNot { it.id == selectedFrom.id }
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
            "from" -> walletAdapter.onWalletClickListener = {

                it.is_selected_from = true
                val data = listFrom
                data.filterNot { item -> item.id == it.id }
                    .forEach { el -> el.is_selected_from = false }
                onWalletsFromChanged(data)
                onSelectedWalletFromChanged(it).also {
                    convertViewModel.getCourse(
                        convertViewModel.selectedWalletFrom.value!!.currency.toString(),
                        convertViewModel.selectedWalletTo.value!!.currency.toString()
                    )
                }

                findNavController().navigateUp()
            }
            "to" -> walletAdapter.onWalletClickListener = {
                it.is_selected_to = true
                it.enable = false
                val data = listTo
                data.filterNot { item -> item.id == it.id }
                    .forEach { item ->
                        item.is_selected_to = false
                        item.enable = true
                    }
                listFrom.forEach { item -> item.enable = true }
                onWalletsToChanged(data)
                onSelectedWalletToChanged(it).also {
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