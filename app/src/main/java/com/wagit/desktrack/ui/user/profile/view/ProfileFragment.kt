package com.wagit.desktrack.ui.user.profile.view

import androidx.fragment.app.activityViewModels
import com.wagit.desktrack.R
import com.wagit.desktrack.databinding.FragmentProfileBinding
import com.wagit.desktrack.ui.BaseFragment
import com.wagit.desktrack.ui.user.viewmodel.SharedHomeViewModel

class ProfileFragment : BaseFragment<FragmentProfileBinding>(R.layout.fragment_profile) {

    private val viewModel: SharedHomeViewModel by activityViewModels()

    override fun FragmentProfileBinding.initialize() {
        println("HHOLA FROM PROILE")
    }
}