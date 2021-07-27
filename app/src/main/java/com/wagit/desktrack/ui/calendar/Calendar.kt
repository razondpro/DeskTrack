package com.wagit.desktrack.ui.calendar

import android.graphics.Color
import android.view.View
import com.kizitonwose.calendarview.CalendarView
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.CalendarMonth
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.MonthHeaderFooterBinder
import com.wagit.desktrack.R
import java.time.LocalDate

class Calendar () {
    fun dayBinder(calendarView: CalendarView){
        val selectedDate: LocalDate? = null
        calendarView.dayBinder = object : DayBinder<DayViewContainer> {
            override fun create(view: View) = DayViewContainer(view, calendarView)
            override fun bind(container: DayViewContainer, day: CalendarDay) {
                container.day = day
                val textView = container.textView
                textView.text = day.date.dayOfMonth.toString()
                if (day.owner == DayOwner.THIS_MONTH) {
                    // Show the month dates. Remember that views are recycled!
                    textView.visibility = View.VISIBLE
                    if (day.date == selectedDate) {
                        // If this is the selected date, show a round background and change the text color.
                        textView.setTextColor(Color.WHITE)
                        textView.setBackgroundResource(R.drawable.ic_launcher_background)
                    } else {
                        // If this is NOT the selected date, remove the background and reset the text color.
                        textView.setTextColor(Color.BLACK)
                        textView.background = null
                    }
                } else {
                    // Hide in and out dates
                    textView.visibility = View.INVISIBLE
                }
            }
        }
    }

    fun monthHeaderBinder(calendarView: CalendarView){
        calendarView.monthHeaderBinder = object : MonthHeaderFooterBinder<MonthViewContainer> {
            override fun create(view: View) = MonthViewContainer(view)
            override fun bind(container: MonthViewContainer, month: CalendarMonth) {
                container.textView.text = "${month.yearMonth.month.name.toLowerCase().capitalize()} ${month.year}"
            }
        }
    }

}