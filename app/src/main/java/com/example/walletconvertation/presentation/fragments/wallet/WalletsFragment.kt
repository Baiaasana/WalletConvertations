package com.example.walletconvertation.presentation.fragments.wallet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.backend.data.model.WalletModel
import com.example.walletconvertation.R
import com.example.walletconvertation.databinding.FragmentWalletsBinding
import com.example.walletconvertation.presentation.adapters.WalletAdapter
import com.example.walletconvertation.presentation.fragments.convert.ConvertViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WalletsFragment : Fragment() {

    private var _binding: FragmentWalletsBinding? = null
    private val binding get() = _binding!!

    private val walletAdapter: WalletAdapter = WalletAdapter()
    private val args : WalletsFragmentArgs by navArgs()

    private val convertViewModel: ConvertViewModel by hiltNavGraphViewModels(R.id.main_navigation_graph)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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
        val data = convertViewModel.wallets.value
        when (args.walletType) {
            "from" -> data!!.filterNot { it.id == convertViewModel.selectedWalletTo.value?.id }
            "to" -> data!!.filterNot { it.id == convertViewModel.selectedWalletFrom.value?.id }
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
                convertViewModel.selectWalletFrom(it)
                findNavController().navigateUp()
            }
            "to" -> walletAdapter.onWalletClickListener = {
                convertViewModel.selectWalletTo(it)
                findNavController().navigateUp()
            }
        }
    }
}