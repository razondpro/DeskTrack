package com.wagit.desktrack.ui.user.calendar.view

import androidx.fragment.app.activityViewModels
import com.wagit.desktrack.R
import com.wagit.desktrack.databinding.FragmentCalendarBinding
import com.wagit.desktrack.ui.BaseFragment
import com.wagit.desktrack.ui.user.viewmodel.SharedHomeViewModel

class CalendarFragment : BaseFragment<FragmentCalendarBinding>(R.layout.fragment_calendar) {

    private val viewModel: SharedHomeViewModel by activityViewModels()

    override fun FragmentCalendarBinding.initialize() {
        println("HHOLA FROM CALENDAR")
    }
}