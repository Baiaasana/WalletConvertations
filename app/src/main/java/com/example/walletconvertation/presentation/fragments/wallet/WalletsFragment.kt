package com.example.walletconvertation.presentation.fragments.wallet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
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
        // aq und ashemowmo romeli aris selected da is ar gamoachino
        walletAdapter.submitList(convertViewModel.wallets.value)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun listeners() {
        binding.toolbar.getBackIcon().setOnClickListener {
            findNavController().navigateUp()
        }
        walletAdapter.onWalletClickListener = {
            convertViewModel.selectWallet(it)
        }
    }
}