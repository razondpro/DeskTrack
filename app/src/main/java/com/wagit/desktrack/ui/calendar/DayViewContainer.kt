package com.wagit.desktrack.ui.calendar

import android.icu.util.Calendar.HOUR_OF_DAY
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.ui.ViewContainer
import com.kizitonwose.calendarview.CalendarView
import com.wagit.desktrack.R
import com.wagit.desktrack.data.entities.Registry
import com.wagit.desktrack.ui.helpers.TimePickerHelper
import com.wagit.desktrack.ui.user.viewmodel.SharedHomeViewModel
import java.time.LocalDate
import java.util.Calendar.HOUR_OF_DAY
import java.util.Calendar.MINUTE

//Provided a DayBinder for the CalendarView using our DayViewContainer type.
class DayViewContainer(view: View, calendarView: CalendarView, mreg: List<Registry>,
                       sharedHomeViewModel: SharedHomeViewModel,
                       textViewDayRegistry: TextView) : ViewContainer(view) {

    lateinit var timePicker: TimePickerHelper

    var textView = view.findViewById<TextView>(R.id.calendarDayText)
    var textViewRegistry = textViewDayRegistry
    var selectedDate: LocalDate? = null
    // Will be set when this container is bound
    lateinit var day: CalendarDay

    init {
        disableScrollOnCalendar(calendarView)

        view.setOnClickListener {
            Toast.makeText(view.context, "${day.date} selected", Toast.LENGTH_SHORT).show()
            //Set the selected day's background to green
            //it.setBackgroundColor(Color.GREEN)
            // Check the day owner as we do not want to select in or out dates.

            timePicker = TimePickerHelper(it.context, false, false)
            if (day.owner == DayOwner.THIS_MONTH) {
                // Keep a reference to any previous selection
                // in case we overwrite it and need to reload it.
                setSelectedDay(calendarView)
                if (sharedHomeViewModel.employee.value != null){
                    println("USER IS ADMIN?: ${sharedHomeViewModel.employee.value!!.isAdmin} +++++++++++++++++++++++++++++++++++++++++++++++++++¡¡¡¡¡¡¡¡¡¡")
                    setSelectedDayReg(mreg)
                }else{
                    //User is admin
                    setAdminSelectedDayReg(mreg)
                }

                println("${textViewRegistry.text} ªªªªªªªªªªªªªªªªªªªªªªªªªªªªªªªªªª")
            }
        }
    }

    private fun setSelectedDay(calendarView: CalendarView){
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

    private fun setSelectedDayReg(mreg: List<Registry>){
        if (mreg.isNotEmpty()){
            var containsSelectedDay = false
            mreg.forEach {
                if (day.date.month === it.startedAt.month &&
                    day.date.dayOfWeek === it.startedAt.dayOfWeek &&
                    day.date.dayOfYear === it.startedAt.dayOfYear){
                    textViewRegistry.text="Day ${day!!.date}, started working at: " +
                            "${it.startedAt.hour}:${it.startedAt.minute}:${it.startedAt.second}, " +
                            "and finished working at ${it.endedAt!!.hour.toString()}:" +
                            "${it.endedAt!!.minute.toString()}:${it.endedAt!!.second.toString()}"
                    containsSelectedDay = true
                }
            }
            if (containsSelectedDay == false){
                textViewRegistry.setText(" ")
            }
        }
    }

    private fun setAdminSelectedDayReg(mreg: List<Registry>){
        if (mreg.isNotEmpty()){
            var containsSelectedDay = false
            mreg.forEach {
                if (day.date.month === it.startedAt.month &&
                    day.date.dayOfWeek === it.startedAt.dayOfWeek &&
                    day.date.dayOfYear === it.startedAt.dayOfYear){
                    textViewRegistry.text="Employee: ${it.employeeId}" +
                            ", Day ${day!!.date}, started working at: " +
                            "${it.startedAt.hour}:${it.startedAt.minute}:${it.startedAt.second}, " +
                            "and finished working at ${it.endedAt!!.hour.toString()}:" +
                            "${it.endedAt!!.minute.toString()}:${it.endedAt!!.second.toString()}"
                    containsSelectedDay = true
                    showTimePickerDialog()
                }
            }
            if (containsSelectedDay == false){
                textViewRegistry.setText(" ")
            }
        }
    }

    private fun disableScrollOnCalendar(calendarView: CalendarView){
        //Evitar que el usuario pueda hacer Scroll sobre los meses
        calendarView.setOnTouchListener { _, event ->
            event.action == MotionEvent.ACTION_MOVE
        }
    }

    private fun showTimePickerDialog() {
        val cal: java.util.Calendar = java.util.Calendar.getInstance()
        val h = cal.get(java.util.Calendar.HOUR)
        val m = cal.get(java.util.Calendar.MINUTE)

        timePicker.showDialog(h, m, object : TimePickerHelper.Callback {
            override fun onTimeSelected(hourOfDay: Int, minute: Int) {
                val hourStr = if (hourOfDay < 10) "0${hourOfDay}" else "${hourOfDay}"
                val minuteStr = if (minute < 10) "0${minute}" else "${minute}"
                val text = "${hourOfDay}:${minuteStr}"
                println("TEXTO: $text ´´´´´´´´´´´´´´´´´´´´´´´´´´´")
           }
        })
    }
}

