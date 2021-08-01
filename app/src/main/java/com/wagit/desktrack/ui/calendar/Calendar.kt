package com.wagit.desktrack.ui.calendar

import android.graphics.Color
import android.view.View
import android.widget.TextView
import androidx.lifecycle.LiveData
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
                //Esto es para dar estilo a los dias del mas anterior y posterior (estan en un color mas claro)
                if (day.owner == DayOwner.THIS_MONTH) {
                    //Set the alpha value
                    textView.alpha = 1f
                    // Show the month dates. Remember that views are recycled!
                    container.textView.visibility = View.VISIBLE
                    container.textView.setTextColor(Color.WHITE)
                    if (day.date == selectedDate) {
                        // If this is the selected date, show a round background and change the text color.
                        container.textView.setTextColor(Color.WHITE)
                        container.textView.setBackgroundResource(R.drawable.ic_launcher_background)

                    } else {
                        // If this is NOT the selected date, remove the background and reset the text color.
                        container.textView.setTextColor(Color.BLACK)
                        container.textView.background = null
                    }

                } else {
                    //Set the alpha value
                    textView.alpha = 0.3f
                    // Hide in and out dates
                    container.textView.visibility = View.INVISIBLE
                    container.textView.setTextColor(Color.GRAY)
                }

                container.textView.setTextColor(Color.BLACK)

                //Set the text color of the days worked as CYAN
                //println("LEGA * ANTES")
                mreg.forEach {
                    println("LEGA *")
                    if (day.date.month === it.startedAt.month && day.date.dayOfWeek === it.startedAt.dayOfWeek && day.date.dayOfYear === it.startedAt.dayOfYear){
                        println("HOUR: ${it.endedAt!!.hour.toString()} ++++++++++++++++++++++++++++++++++++++++++++")
                        if (it.endedAt!!.hour === 0){
                            container.textView.setBackgroundColor(Color.YELLOW)
                        }else{
                            container.textView.setBackgroundColor(Color.CYAN)
                        }
                    }
                }


            }
        }
    }

    fun monthHeaderBinder(calendarView: CalendarView){
        calendarView.monthHeaderBinder = object : MonthHeaderFooterBinder<MonthViewContainer> {
            override fun create(view: View) = MonthViewContainer(view)
            override fun bind(container: MonthViewContainer, month: CalendarMonth) {
                //Setting the months
                container.textViewHeaderMonth.text = "${month.yearMonth.month.name.toLowerCase().capitalize()} ${month.year}"

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

    fun monthFooterBinder(calendarView: CalendarView){
        calendarView.monthFooterBinder = object : MonthHeaderFooterBinder<MonthViewContainer> {
            override fun create(view: View) = MonthViewContainer(view)
            override fun bind(container: MonthViewContainer, month: CalendarMonth) {
                //TODO: añadir las funcionalidades de rellenar el textview del Footer con datos de los registros
                val daysOfWeek = arrayOf(
                    DayOfWeek.SUNDAY,
                    DayOfWeek.MONDAY,
                    DayOfWeek.TUESDAY,
                    DayOfWeek.WEDNESDAY,
                    DayOfWeek.THURSDAY,
                    DayOfWeek.FRIDAY,
                    DayOfWeek.SATURDAY
                )
                var days: String = " "
                daysOfWeek.forEach { days = days + it.value.toString() }
            }
        }
    }

}