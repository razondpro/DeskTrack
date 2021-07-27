package com.wagit.desktrack.ui.user.calendar.view

import android.view.View
import android.widget.TextView
import com.kizitonwose.calendarview.CalendarView
import com.kizitonwose.calendarview.model.CalendarMonth
import com.kizitonwose.calendarview.ui.MonthHeaderFooterBinder
import com.kizitonwose.calendarview.ui.ViewContainer
import com.wagit.desktrack.R
import com.wagit.desktrack.databinding.FragmentCalendarBinding
import com.wagit.desktrack.ui.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import java.time.DayOfWeek
import java.time.YearMonth
import java.time.temporal.WeekFields
import java.util.*
import com.wagit.desktrack.ui.calendar.Calendar
import com.wagit.desktrack.ui.calendar.MonthViewContainer

@AndroidEntryPoint
class CalendarFragment : BaseFragment<FragmentCalendarBinding>(R.layout.fragment_calendar){
    override fun FragmentCalendarBinding.initialize() {
        println("HHOLA FROM CALENDAR FRAGMENT !!!")

        val calendar = Calendar()
        calendar.dayBinder(calendarView)
        calendar.monthHeaderBinder(calendarView)

        setCurrentMonth(calendarView)

    }
    private fun setCurrentMonth(calendarView: CalendarView) {
        val currentMonth = YearMonth.now()
        val firstMonth = currentMonth.minusMonths(10)
        val lastMonth = currentMonth.plusMonths(10)
        val firstDayOfWeek = WeekFields.of(Locale.getDefault()).firstDayOfWeek
        calendarView.setup(firstMonth, lastMonth, firstDayOfWeek)
        calendarView.scrollToMonth(currentMonth)
    }

}