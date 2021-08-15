package com.wagit.desktrack.ui.admin.calendar.view

import com.wagit.desktrack.R
import com.wagit.desktrack.databinding.FragmentAdminCalendarBinding
import com.wagit.desktrack.ui.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CalendarFragment : BaseFragment<FragmentAdminCalendarBinding>(R.layout.fragment_admin_calendar)  {

    override fun FragmentAdminCalendarBinding.initialize() {
        println("LLega al calendar!!!")
    }

}