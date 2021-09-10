package com.wagit.desktrack.ui.admin.calendar.view

import android.widget.TextView
import androidx.fragment.app.FragmentActivity
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
import com.wagit.desktrack.ui.user.viewmodel.SharedHomeViewModel
import com.wagit.desktrack.ui.calendar.Calendar
import java.time.YearMonth
import java.time.temporal.WeekFields
import androidx.lifecycle.Observer
import com.wagit.desktrack.ui.admin.calendar.AddRegistryFragment
import java.util.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CalendarFragment :
    BaseFragment<FragmentAdminCalendarBinding>(R.layout.fragment_admin_calendar)  {
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val sharedHomeViewModel: SharedHomeViewModel by activityViewModels()
    private val calendarViewModel: CalendarViewModel by viewModels()
    private var cMonth: YearMonth? = null

    override fun FragmentAdminCalendarBinding.initialize() {
        println("LLega al calendar!!!")
        this.sharedCalendarVM = sharedCalendarVM
        if (sharedViewModel.employee.value != null){
            println("ID: ---------------------------------------------------------------->" +
                    "${sharedViewModel.employee.value!!.first().id}")
        }

        val monthString: String = convertIntToTwoDigitString(YearMonth.now().monthValue).toString()
        //val mreg = calendarViewModel.getAllRegistriesByMonthAndYear(monthString,
        //    YearMonth.now().year.toString())

        val mreg = calendarViewModel.getAllRegistriesByEmployeeAndMonthAndYear(
            sharedViewModel.employee.value!!.first().id,
            monthString,YearMonth.now().year.toString())

        println("MONTHREGISTRY ANTES En El init." +
                "${monthString} ${calendarViewModel.allMonthRegistry.value}")

        val calendar = Calendar()
        this.tvEditDayRegistries.setText(" ")
        dayMonthBinder(adminCalendarView,calendar,this.tvEditDayRegistries)
        setCurrentMonth(adminCalendarView)

        calendarViewModel.allMonthRegistry.observe(viewLifecycleOwner, Observer {
            println("Llega al Observer para el livedata allMonthRegistry")
            println("allMonthRegistry: ${calendarViewModel.allMonthRegistry.value}")
            this.tvEditDayRegistries.setText(" ")
            dayMonthBinder(adminCalendarView,calendar,this.tvEditDayRegistries)
            udpateCalendar(adminCalendarView)

        })

        this.btnNext.setOnClickListener {
            setNextMonth(adminCalendarView, calendarViewModel)
            val monthString: String = convertIntToTwoDigitString(cMonth!!.monthValue)

            //val mreg = calendarViewModel.getAllRegistriesByMonthAndYear(monthString,
            //    cMonth!!.year.toString())

            val mreg = calendarViewModel.getAllRegistriesByEmployeeAndMonthAndYear(
                sharedViewModel.employee.value!!.first().id,
                monthString,cMonth!!.year.toString())
        }

        this.btnPrevious.setOnClickListener {
            setPreviousMonth(adminCalendarView, calendarViewModel)
            val monthString: String = convertIntToTwoDigitString(cMonth!!.monthValue)

            //val mreg = calendarViewModel.getAllRegistriesByMonthAndYear(monthString,
            //    cMonth!!.year.toString())

            val mreg = calendarViewModel.getAllRegistriesByEmployeeAndMonthAndYear(
                sharedViewModel.employee.value!!.first().id,
                monthString,cMonth!!.year.toString())
        }

        this.btnGoBackFromCalendar.setOnClickListener {
            goBack()
        }

    }

    private fun goBack(){
        val fragmentManager = (activity as FragmentActivity).supportFragmentManager
        fragmentManager.popBackStackImmediate()
        fragmentManager.popBackStackImmediate()
        println("LLEGA AL GOBACK() DEL Calendar !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!")
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
        val fragmentManager = (activity as FragmentActivity).supportFragmentManager
        calendar.dayBinder(calendarView,aux,textViewDayRegistry,sharedHomeViewModel,
            sharedViewModel,fragmentManager)
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