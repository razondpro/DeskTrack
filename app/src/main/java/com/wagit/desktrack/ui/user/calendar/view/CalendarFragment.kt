package com.wagit.desktrack.ui.user.calendar.view

import android.view.View
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.kizitonwose.calendarview.CalendarView
import com.wagit.desktrack.R
import com.wagit.desktrack.databinding.FragmentCalendarBinding
import com.wagit.desktrack.ui.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import java.time.YearMonth
import java.time.temporal.WeekFields
import java.util.*
import com.wagit.desktrack.ui.calendar.Calendar
import com.wagit.desktrack.ui.user.home.viewmodel.CalendarViewModel
import com.wagit.desktrack.ui.user.viewmodel.SharedHomeViewModel
import androidx.lifecycle.Observer
import com.kizitonwose.calendarview.utils.next
import com.kizitonwose.calendarview.utils.previous
import com.kizitonwose.calendarview.utils.yearMonth
import com.wagit.desktrack.data.entities.Registry

@AndroidEntryPoint
class CalendarFragment : BaseFragment<FragmentCalendarBinding>(R.layout.fragment_calendar){
    private val sharedViewModel: SharedHomeViewModel by activityViewModels()
    private val calendarViewModel: CalendarViewModel by viewModels()
    private var cMonth: YearMonth? = null

    override fun FragmentCalendarBinding.initialize() {
        //println("HHOLA FROM CALENDAR FRAGMENT !!!")
        //Get the registries by employee, month and month
        this.sharedCalendarVM=sharedViewModel
        val monthString: String = convertIntToTwoDigitString(YearMonth.now().monthValue).toString()
        val mreg = calendarViewModel.getAllRegistriesByEmployeeAndMonthAndYear(
            sharedViewModel.employee.value!!.id,monthString,YearMonth.now().year.toString())
        println("MONTHREGISTRY ANTES ${monthString} ${calendarViewModel.monthRegistry.value}")
        val calendar = Calendar()
        this.tvDayRegistries.setText(" ")
        dayMonthBinder(calendarView,calendar,this.tvDayRegistries)
        //calendar.monthFooterBinder(calendarView)
        setCurrentMonth(calendarView)

        //TODO: Pasar a la clase Calendar
        // Calendar -> recibe: month, year, datosMonthRegistries
        // Cambiar de MES -> Calendar recibe el mes, year y se reasignara la variable monthRegistry con los datos actuales

        calendarViewModel.monthRegistry.observe(viewLifecycleOwner, Observer {
            println("Llega al Observer para el livedata monthRegistry")
            //Actualizar el calendario segun los registros
            println("MONTHREGISTRY ${calendarViewModel.monthRegistry.value}")
            this.tvDayRegistries.setText(" ")
            dayMonthBinder(calendarView,calendar,this.tvDayRegistries)
            //calendar.monthFooterBinder(calendarView)
            udpateCalendar(calendarView)

        })

        this.buttonPrevious.setOnClickListener(View.OnClickListener {
            setPreviousMonth(calendarView,calendarViewModel)
            val monthString: String = convertIntToTwoDigitString(cMonth!!.monthValue)

            val mreg = calendarViewModel.getAllRegistriesByEmployeeAndMonthAndYear(
                sharedViewModel.employee.value!!.id,
                monthString,
                cMonth!!.year.toString())

        })

        this.buttonNext.setOnClickListener(View.OnClickListener {
            setNextMonth(calendarView, calendarViewModel)
            val monthString: String = convertIntToTwoDigitString(cMonth!!.monthValue)

            val mreg = calendarViewModel.getAllRegistriesByEmployeeAndMonthAndYear(
                sharedViewModel.employee.value!!.id,
                monthString,
                cMonth!!.year.toString())

        })

    }

    private fun setCurrentMonth(calendarView: CalendarView) {
        val currentMonth = YearMonth.now()
        cMonth=currentMonth
        setupCalendar(calendarView,cMonth!!)

    }
    private fun setPreviousMonth(calendarView: CalendarView, calendarViewModel: CalendarViewModel) {
        val currentMonth = cMonth!!.previous
        cMonth=currentMonth
        //println("{!!} Month y MONTHSCROLLLISTENER: $currentMonth Y ${calendarView.monthScrollListener}")
        //udpateCalendar(currentMonth,calendarView)

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
        //val firstDayOfWeek = WeekFields.of(Locale.getDefault()).firstDayOfWeek

        //calendarView.setup(firstMonth!!, lastMonth!!, firstDayOfWeek)
        calendarView.updateMonthRange(firstMonth!!, lastMonth!!)
        calendarView.scrollToMonth(cMonth!!)
        calendarView.notifyMonthChanged(cMonth!!)
        calendarView.notifyCalendarChanged()
    }

    private fun dayMonthBinder(calendarView: CalendarView, calendar: Calendar, textViewDayRegistry: TextView){
        var aux = listOf<Registry>()

        if (calendarViewModel.monthRegistry.value != null){
            aux=calendarViewModel.monthRegistry.value!!
            println("ENTRA ${calendarViewModel.monthRegistry.value!!}")
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