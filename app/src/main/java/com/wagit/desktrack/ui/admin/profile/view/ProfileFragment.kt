package com.wagit.desktrack.ui.admin.profile.view

import com.wagit.desktrack.R
import com.wagit.desktrack.databinding.FragmentAdminProfileBinding
import com.wagit.desktrack.ui.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentAdminProfileBinding>(R.layout.fragment_admin_profile) {
    override fun FragmentAdminProfileBinding.initialize() {
        println("Llega al profile")
    }
}