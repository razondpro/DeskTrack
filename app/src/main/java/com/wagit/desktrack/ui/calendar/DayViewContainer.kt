package com.wagit.desktrack.ui.calendar

import android.view.View
import android.widget.TextView
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.ui.ViewContainer
import com.kizitonwose.calendarview.CalendarView
import com.wagit.desktrack.R
import java.time.LocalDate

//Provided a DayBinder for the CalendarView using our DayViewContainer type.
class DayViewContainer(view: View, calendarView: CalendarView) : ViewContainer(view) {
    val textView = view.findViewById<TextView>(R.id.calendarDayText)
    private var selectedDate: LocalDate? = null
    // Will be set when this container is bound
    lateinit var day: CalendarDay

    init {
        view.setOnClickListener {
            // Check the day owner as we do not want to select in or out dates.
            if (day.owner == DayOwner.THIS_MONTH) {
                // Keep a reference to any previous selection
                // in case we overwrite it and need to reload it.
                val currentSelection = selectedDate
                if (currentSelection == day.date) {
                    // If the user clicks the same date, clear selection.
                    selectedDate = null
                    // Reload this date so the dayBinder is called
                    // and we can REMOVE the selection background.
                    calendarView.notifyDateChanged(currentSelection)
                } else {
                    selectedDate = day.date
                    // Reload the newly selected date so the dayBinder is
                    // called and we can ADD the selection background.
                    calendarView.notifyDateChanged(day.date)
                    if (currentSelection != null) {
                        // We need to also reload the previously selected
                        // date so we can REMOVE the selection background.
                        calendarView.notifyDateChanged(currentSelection)
                    }
                }
            }
        }
    }
}