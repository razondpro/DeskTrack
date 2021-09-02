package com.wagit.desktrack.ui.admin.home.view

import android.util.Log
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.wagit.desktrack.R
import com.wagit.desktrack.databinding.FragmentAdminHomeBinding
import com.wagit.desktrack.ui.BaseFragment
import com.wagit.desktrack.ui.admin.home.AddEditCompanyFragment
import com.wagit.desktrack.ui.admin.home.AddEditEmployeeFragment
import com.wagit.desktrack.ui.admin.home.ExportDataFragment
import com.wagit.desktrack.ui.admin.home.viewmodel.HomeViewModel
import com.wagit.desktrack.ui.admin.viewmodel.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentAdminHomeBinding>(R.layout.fragment_admin_home) {
    private val shareViewModel: SharedViewModel by activityViewModels()
    private val homeViewModel: HomeViewModel by viewModels()

    override fun FragmentAdminHomeBinding.initialize() {
        println("LLEGA!!!")
        val employeeFragment= AddEditEmployeeFragment()
        val companyFragment= AddEditCompanyFragment()
        val exportDataFragment= ExportDataFragment()

        println("Llega al initialize del home fragment con $employeeFragment, $companyFragment y " +
                "$exportDataFragment")
        Log.d("AdminFragment","Llega al initialize del home fragment con " +
                "$employeeFragment, $companyFragment y $exportDataFragment")

        btnAddEditEmployee.setOnClickListener {
            val fragmentManager = (activity as FragmentActivity).supportFragmentManager
            fragmentManager.beginTransaction().apply {
                replace(R.id.fragmentAdminContainerView,employeeFragment)
                addToBackStack("employeeFragment")
                commit()
            }
        }

        btnAddEditCompany.setOnClickListener {
            val fragmentManager = (activity as FragmentActivity).supportFragmentManager
            fragmentManager.beginTransaction().apply {
                replace(R.id.fragmentAdminContainerView,companyFragment)
                addToBackStack("companyFragment")
                commit()
            }
        }

        btnExportData.setOnClickListener {
            val fragmentManager = (activity as FragmentActivity).supportFragmentManager
            fragmentManager.beginTransaction().apply {
                replace(R.id.fragmentAdminContainerView,exportDataFragment)
                addToBackStack("exportDataFragment")
                commit()
            }
        }
    }
}