package com.wagit.desktrack.ui.admin.home

import androidx.fragment.app.FragmentActivity
import com.wagit.desktrack.R
import com.wagit.desktrack.databinding.FragmentExportDataBinding
import com.wagit.desktrack.ui.BaseFragment

class ExportDataFragment: BaseFragment<FragmentExportDataBinding>(R.layout.fragment_export_data){
    override fun FragmentExportDataBinding.initialize() {
        println("WELCOME TO THE EXPORT DATA FRAGMENT!!!!!!!!!")

        btnGoBackHome.setOnClickListener {
            goBack()
        }
    }

    private fun goBack(){
        val fragmentManager = (activity as FragmentActivity).supportFragmentManager
        fragmentManager.popBackStackImmediate()
        println("LLEGA AL GOBACK() !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!")
    }
}