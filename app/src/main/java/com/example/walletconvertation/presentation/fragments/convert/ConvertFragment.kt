package com.example.walletconvertation.presentation.fragments.convert

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.navGraphViewModels
import com.example.backend.repository.course.CourseRepository
import com.example.backend.repository.course.CourseRepositoryImpl
import com.example.backend.repository.course.CourseRepositoryImpl_Factory
import com.example.walletconvertation.R
import com.example.walletconvertation.databinding.FragmentConvertBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ConvertFragment : Fragment() {

    private var _binding: FragmentConvertBinding? = null
    private val binding get() = _binding!!

    private val convertViewModel: ConvertViewModel by navGraphViewModels(R.id.main_navigation_graph)

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
        binding.viewModel = convertViewModel
        binding.lifecycleOwner = viewLifecycleOwner

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}