package com.wagit.desktrack.ui.admin.calendar.view

import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.kizitonwose.calendarview.CalendarView
import com.kizitonwose.calendarview.utils.next
import com.kizitonwose.calendarview.utils.previous
import com.wagit.desktrack.R
import com.wagit.desktrack.data.entities.Registry
import com.wagit.desktrack.databinding.FragmentAdminCalendarBinding
import com.wagit.desktrack.ui.BaseFragment
import com.wagit.desktrack.ui.admin.calendar.viewmodel.CalendarViewModel
import com.wagit.desktrack.ui.admin.viewmodel.SharedViewModel
import com.wagit.desktrack.ui.calendar.Calendar
import java.time.YearMonth
import java.time.temporal.WeekFields
import java.util.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CalendarFragment : BaseFragment<FragmentAdminCalendarBinding>(R.layout.fragment_admin_calendar)  {
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val calendarViewModel: CalendarViewModel by viewModels()
    private var cMonth: YearMonth? = null

    override fun FragmentAdminCalendarBinding.initialize() {
        println("LLega al calendar!!!")
        this.sharedCalendarVM = sharedCalendarVM

        val calendar = Calendar()
        dayMonthBinder(adminCalendarView,calendar,this.tvEditDayRegistries)
        //calendar.monthFooterBinder(calendarView)
        setCurrentMonth(adminCalendarView)

        this.btnNext.setOnClickListener {
            setNextMonth(adminCalendarView, calendarViewModel)
            val monthString: String = convertIntToTwoDigitString(cMonth!!.monthValue)
            dayMonthBinder(adminCalendarView,calendar,this.tvEditDayRegistries)
            //calendar.monthFooterBinder(calendarView)
            udpateCalendar(adminCalendarView)

        }

        this.btnPrevious.setOnClickListener {
            setPreviousMonth(adminCalendarView,calendarViewModel)
            val monthString: String = convertIntToTwoDigitString(cMonth!!.monthValue)
            dayMonthBinder(adminCalendarView,calendar,this.tvEditDayRegistries)
            //calendar.monthFooterBinder(calendarView)
            udpateCalendar(adminCalendarView)
        }

    }

    private fun setCurrentMonth(calendarView: CalendarView) {
        val currentMonth = YearMonth.now()
        cMonth=currentMonth
        setupCalendar(calendarView,cMonth!!)

    }

    private fun setPreviousMonth(calendarView: CalendarView, calendarViewModel: CalendarViewModel) {
        val currentMonth = cMonth!!.previous
        cMonth=currentMonth
    }

    private fun setNextMonth(calendarView: CalendarView, calendarViewModel: CalendarViewModel) {
        val currentMonth = cMonth!!.next
        cMonth=currentMonth
    }

    private fun setupCalendar(calendarView: CalendarView, currentMonth: YearMonth){
        val firstMonth = currentMonth.minusMonths(10)
        val lastMonth = currentMonth.plusMonths(10)
        val firstDayOfWeek = WeekFields.of(Locale.getDefault()).firstDayOfWeek

        calendarView.setup(firstMonth!!, lastMonth!!, firstDayOfWeek)
        calendarView.scrollToMonth(currentMonth)
        calendarView.notifyMonthChanged(currentMonth)
    }

    private fun udpateCalendar(calendarView: CalendarView){
        val firstMonth = cMonth!!.minusMonths(10)
        val lastMonth = cMonth!!.plusMonths(10)

        calendarView.updateMonthRange(firstMonth!!, lastMonth!!)
        calendarView.scrollToMonth(cMonth!!)
        calendarView.notifyMonthChanged(cMonth!!)
        calendarView.notifyCalendarChanged()
    }

    private fun dayMonthBinder(calendarView: CalendarView, calendar: Calendar,
                               textViewDayRegistry: TextView
    ){
        var aux = listOf<Registry>()

        if (calendarViewModel.allMonthRegistry.value != null){
            aux=calendarViewModel.allMonthRegistry.value!!
            println("ENTRA ${calendarViewModel.allMonthRegistry.value!!}")
        }

        calendar.dayBinder(calendarView,aux,textViewDayRegistry)
        calendar.monthHeaderBinder(calendarView)

    }

    private fun convertIntToTwoDigitString(month: Int) :String{
        var result: String=month.toString()
        if (month < 10){
            result="0${result}"
        }
        return result
    }

}