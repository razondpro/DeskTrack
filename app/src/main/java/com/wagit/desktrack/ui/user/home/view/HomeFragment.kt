package com.wagit.desktrack.ui.user.home.view

import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.wagit.desktrack.R
import com.wagit.desktrack.databinding.FragmentHomeBinding
import com.wagit.desktrack.ui.BaseFragment
import com.wagit.desktrack.ui.user.home.viewmodel.HomeViewModel
import com.wagit.desktrack.ui.user.viewmodel.SharedHomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {

    private val sharedViewModel: SharedHomeViewModel by activityViewModels()
    private val homeViewModel: HomeViewModel by viewModels()

    override fun FragmentHomeBinding.initialize() {
        println("HHOLA FROM HOME")
        println(sharedViewModel.employee.value)
        this.sharedVM = sharedViewModel
        val treg = homeViewModel.getTodaysRegistry(sharedViewModel.employee.value!!.id)

        homeViewModel.tRegistry.observe(viewLifecycleOwner, Observer {
            println("--------")
            println(it.first())
            println("--------")
        })



    }

}