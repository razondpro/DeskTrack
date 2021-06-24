package com.wagit.desktrack.ui.user.home.view

import androidx.fragment.app.activityViewModels
import com.wagit.desktrack.R
import com.wagit.desktrack.databinding.FragmentHomeBinding
import com.wagit.desktrack.ui.BaseFragment
import com.wagit.desktrack.ui.user.viewmodel.SharedHomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {

    private val viewModel: SharedHomeViewModel by activityViewModels()

    override fun FragmentHomeBinding.initialize() {
        println("HHOLA FROM HOME")
        println(viewModel.employee.value)
        this.sharedVM = viewModel
    }

}