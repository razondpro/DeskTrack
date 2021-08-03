package com.wagit.desktrack.ui.calendar

import android.view.View
import android.widget.TextView
import com.kizitonwose.calendarview.ui.ViewContainer
import com.wagit.desktrack.R

class MonthViewContainer(view: View) : ViewContainer(view) {
    val textViewHeaderMonth = view.findViewById<TextView>(R.id.headerTextViewMonth)
    val textViewHeaderDay = view.findViewById<TextView>(R.id.headerTextViewDay)
}