package com.example.walletconvertation.presentation.fragments.wallet

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.navGraphViewModels
import com.example.backend.repository.course.CourseRepository
import com.example.backend.repository.course.CourseRepositoryImpl
import com.example.walletconvertation.R
import com.example.walletconvertation.databinding.FragmentWalletsBinding
import com.example.walletconvertation.di.RepositoryModule
import com.example.walletconvertation.presentation.fragments.convert.ConvertViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WalletsFragment : Fragment() {

    private var _binding: FragmentWalletsBinding? = null
    private val binding get() = _binding!!

    private val convertViewModel: ConvertViewModel by navGraphViewModels(R.id.main_navigation_graph){ viewModelFactory {  }}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWalletsBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}