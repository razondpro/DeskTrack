package com.wagit.desktrack.ui.calendar

import android.graphics.Color
import android.view.View
import android.widget.TextView
import com.kizitonwose.calendarview.CalendarView
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.CalendarMonth
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.MonthHeaderFooterBinder
import com.wagit.desktrack.R
import com.wagit.desktrack.data.entities.Registry
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.*

class Calendar () {
    //En esta variable nos guardamos el día actual para los botones de detras y delante
    public var dayViewContainer: DayViewContainer?=null
    fun dayBinder(calendarView: CalendarView, mreg: List<Registry>, textViewDayRegistry: TextView){
        val selectedDate: LocalDate? = null
        calendarView.dayBinder = object : DayBinder<DayViewContainer> {
            override fun create(view: View) = DayViewContainer(view, calendarView,mreg,textViewDayRegistry)
            override fun bind(container: DayViewContainer, day: CalendarDay) {
                dayViewContainer=container
                container.day = day
                val textView = container.textView
                textView.text = day.date.dayOfMonth.toString()
                container.textView.setTextColor(Color.BLACK)

                //Set the text color of the days worked as CYAN
                //println("LEGA * ANTES")
                setSelectedDayColor(mreg,day,container)
            }
        }
    }

    fun monthHeaderBinder(calendarView: CalendarView){
        calendarView.monthHeaderBinder = object : MonthHeaderFooterBinder<MonthViewContainer> {
            override fun create(view: View) = MonthViewContainer(view)
            override fun bind(container: MonthViewContainer, month: CalendarMonth) {
                //Setting the months
                container.textViewHeaderMonth.text = "${month.yearMonth.month.name.lowercase().uppercase()} " +
                        "${month.year}"

                //Setting the days of the week
                val daysOfWeek = arrayOf(
                    DayOfWeek.SUNDAY,
                    DayOfWeek.MONDAY,
                    DayOfWeek.TUESDAY,
                    DayOfWeek.WEDNESDAY,
                    DayOfWeek.THURSDAY,
                    DayOfWeek.FRIDAY,
                    DayOfWeek.SATURDAY
                )
                var days: String = "  "
                daysOfWeek.forEach { days = days +"  "+ it.getDisplayName(TextStyle.SHORT, Locale.getDefault()) }

                container.textViewHeaderDay.text = "${days}"
            }
        }
    }

    private fun setSelectedDayColor(mreg: List<Registry>,day: CalendarDay,container: DayViewContainer){
        mreg.forEach {
            println("LEGA *")
            if (day.date.month === it.startedAt.month &&
                day.date.dayOfWeek === it.startedAt.dayOfWeek &&
                day.date.dayOfYear === it.startedAt.dayOfYear){
                if (it.endedAt!!.hour === 0){
                    container.textView.setBackgroundColor(Color.YELLOW)
                }else{
                    container.textView.setBackgroundColor(Color.CYAN)
                }
            }
        }
    }

}