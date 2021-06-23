package com.wagit.desktrack.ui.user.calendar.view

import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.wagit.desktrack.R
import com.wagit.desktrack.databinding.FragmentCalendarBinding
import com.wagit.desktrack.ui.BaseFragment
import com.wagit.desktrack.ui.user.viewmodel.SharedHomeViewModel

class CalendarFragment : BaseFragment<FragmentCalendarBinding>(R.layout.fragment_calendar) {

    private val viewModel: SharedHomeViewModel by activityViewModels()

    override fun FragmentCalendarBinding.initialize() {
        println("HHOLA FROM CALENDAR")
        println(viewModel.account.value)

        val textView: TextView = this.textDashboard
        viewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
    }
}