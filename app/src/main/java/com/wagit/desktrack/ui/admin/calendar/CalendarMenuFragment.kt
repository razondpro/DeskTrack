package com.wagit.desktrack.ui.admin.calendar

import android.util.Log
import android.view.View
import com.wagit.desktrack.R
import com.wagit.desktrack.databinding.FragmentAdminCalendarMenuBinding
import com.wagit.desktrack.ui.BaseFragment
import com.wagit.desktrack.ui.admin.viewmodel.SharedViewModel
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import com.wagit.desktrack.ui.admin.calendar.view.CalendarFragment

class CalendarMenuFragment:
    BaseFragment<FragmentAdminCalendarMenuBinding>(R.layout.fragment_admin_calendar_menu){
    private val shareViewModel: SharedViewModel by activityViewModels()

    override fun FragmentAdminCalendarMenuBinding.initialize() {
        println("Welcome to Calendar's Menu")
        val selectEmployeeCalendar = SelectEmployeeCalendar()
        val addRegistryFragment = AddRegistryFragment()

        btnCalendarFragment.setOnClickListener {
            val fragmentManager = (activity as FragmentActivity).supportFragmentManager
            fragmentManager.beginTransaction().apply {
                replace(R.id.fragmentAdminContainerView,selectEmployeeCalendar)
                addToBackStack("selectEmployeeCalendar")
                commit()
            }
        }

        btnAddRegistryFragment.setOnClickListener {
            val fragmentManager = (activity as FragmentActivity).supportFragmentManager
            fragmentManager.beginTransaction().apply {
                replace(R.id.fragmentAdminContainerView,addRegistryFragment)
                addToBackStack("addRegistryFragment")
                commit()
            }
        }
    }
}