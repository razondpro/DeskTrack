package com.wagit.desktrack.ui.admin.calendar

import android.util.Log
import android.view.View
import com.wagit.desktrack.R
import com.wagit.desktrack.databinding.FragmentAddRegistryBinding
import com.wagit.desktrack.ui.BaseFragment
import com.wagit.desktrack.ui.admin.viewmodel.SharedViewModel
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.text.format.DateFormat
import android.widget.Button
import android.widget.DatePicker
import android.widget.TextView
import android.widget.TimePicker
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.wagit.desktrack.data.entities.Employee
import com.wagit.desktrack.data.entities.Registry
import com.wagit.desktrack.ui.admin.calendar.viewmodel.CalendarViewModel
import java.time.LocalDateTime
import java.time.Month
import java.util.*

class AddRegistryFragment:
    BaseFragment<FragmentAddRegistryBinding>(R.layout.fragment_add_registry),
    DatePickerDialog.OnDateSetListener,
    TimePickerDialog.OnTimeSetListener {
    lateinit var textViewInTime: TextView
    lateinit var textViewOutTime: TextView
    lateinit var button: Button
    var day = 0
    var month: Int = 0
    var year: Int = 0
    var hour: Int = 0
    var minute: Int = 0
    var myDay = 0
    var myMonth: Int = 0
    var myYear: Int = 0
    var myHour: Int = 0
    var myMinute: Int = 0
    var selectedTime = -1
    var spinnerEmployees = mutableListOf<String>("")
    var spinnerEmplId = mutableListOf<Int>()
    var emplPosition = -1
    var dayCheckIn: Int = -1
    var monthCheckIn: Int = -1
    var yearCheckIn: Int = -1
    var hourCheckIn: Int = -1
    var minuteCheckIn: Int = -1

    var dayCheckOut: Int = -1
    var monthCheckOut: Int = -1
    var yearCheckOut: Int = -1
    var hourCheckOut: Int = -1
    var minuteCheckOut: Int = -1

    private val shareViewModel: SharedViewModel by activityViewModels()

    override fun FragmentAddRegistryBinding.initialize(){
        println("Welcome to AddRegistryFragment!!!!: ${LocalDateTime.now()}")

        this.btnGoBackFromAddReg.setOnClickListener {
            goBack()
        }

        var spin = this.spinEmployeeToRegister
        updateSnipperEmployee(spin,this)

        shareViewModel.employees.observe(viewLifecycleOwner, Observer {
            updateSnipperEmployee(spin,this)
        })

        shareViewModel.registry.observe(viewLifecycleOwner, Observer {
            //Llamar a update data
        })

        initiateInputData()
        textViewInTime = this.tvSelectedInTime
        button = this.btnPickInTime
        button.setOnClickListener {
            val calendar: Calendar = Calendar.getInstance()
            day = calendar.get(Calendar.DAY_OF_MONTH)
            month = calendar.get(Calendar.MONTH)
            year = calendar.get(Calendar.YEAR)
            selectedTime = 1
            val datePickerDialog =
                DatePickerDialog(it.context, this@AddRegistryFragment, year, month,day)
            datePickerDialog.show()
        }

        textViewOutTime = this.tvSelectedOutTime
        button = this.btnPickOutTime
        button.setOnClickListener {
            val calendar: Calendar = Calendar.getInstance()
            day = calendar.get(Calendar.DAY_OF_MONTH)
            month = calendar.get(Calendar.MONTH)
            year = calendar.get(Calendar.YEAR)
            selectedTime = 2
            val datePickerDialog =
                DatePickerDialog(it.context, this@AddRegistryFragment, year, month,day)
            datePickerDialog.show()
        }

        this.btnAddRegistry.setOnClickListener {
            //Validate Input && Insert new register
            validateInputData(it.context)
        }

        var employees = shareViewModel.getAllEmployees().value
    }

    private fun goBack(){
        val fragmentManager = (activity as FragmentActivity).supportFragmentManager
        fragmentManager.popBackStackImmediate()
        println("LLEGA AL GOBACK() DEL COMPANY !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!")
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        myDay = dayOfMonth
        myYear = year
        myMonth = month+1
        val calendar: Calendar = Calendar.getInstance()
        hour = calendar.get(Calendar.HOUR)
        minute = calendar.get(Calendar.MINUTE)
        val timePickerDialog = TimePickerDialog(this.context, this, hour, minute,
            DateFormat.is24HourFormat(this.context))
        timePickerDialog.show()
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        myHour = hourOfDay
        myMinute = minute

        if (selectedTime == 1){
            //val dateTime = LocalDateTime.of(myYear, myMonth, myDay, myHour, myMinute,0,0)
            val dateTime = LocalDateTime.of(myYear, myMonth, myDay, myHour, myMinute,
                0,0)
            println("Check in: " + dateTime)
            dayCheckIn = myDay
            monthCheckIn = myMonth
            yearCheckIn = myYear
            hourCheckIn = myHour
            minuteCheckIn = myMinute
            println("monthCheckIn: $monthCheckIn ++++++++++++++++++++++++++++")
            textViewInTime.text = "Check in: " +
                    "\n" + "Year: " + myYear + "\n" + "Month: " + myMonth +
                    "\n" + "Day: " + myDay +
                    "\n" + "Hour: " + myHour + "\n" + "Minute: " + myMinute
        }
        if (selectedTime == 2){
            val dateTime = LocalDateTime.of(myYear, myMonth, myDay, myHour, myMinute,
                0,0)
            println("Check out: " + dateTime)
            dayCheckOut = myDay
            monthCheckOut = myMonth
            yearCheckOut = myYear
            hourCheckOut = myHour
            minuteCheckOut = myMinute
            println("monthCheckOut: $monthCheckOut ++++++++++++++++++++++++++++")
            textViewOutTime.text = "Check out: " +
                    "\n" + "Year: " + myYear + "\n" + "Month: " + myMonth +
                    "\n" + "Day: " + myDay +
                    "\n" + "Hour: " + myHour + "\n" + "Minute: " + myMinute
        }
    }

    private fun updateSnipperEmployee(spin: Spinner,
                                      fragmentAddRegistryBinding: FragmentAddRegistryBinding){

        var employeesAux = listOf<Employee>()
        if (shareViewModel.employees.value != null){
            employeesAux = shareViewModel.employees.value!!

            println("Entra en updateSnipperEmployee con ${shareViewModel.employees.value!!}")
        }
        setSnipperEmployees(employeesAux, spin,fragmentAddRegistryBinding)
    }

    private fun setSnipperEmployees(employees: List<Employee>, spin: Spinner,
                                    fragmentAddRegistryBinding: FragmentAddRegistryBinding){

        spinnerEmployees = mutableListOf<String>("")
        spinnerEmplId = mutableListOf<Int>()
        // Spinner Drop down elements
        employees?.forEach {
            if (it.isAdmin == false){
                spinnerEmployees.add(it.firstName + " " + it.lastName)
                spinnerEmplId.add(it.id.toInt())
            }
        }
        setSnipperItemSelector(spin,fragmentAddRegistryBinding)
    }

    private fun setSnipperItemSelector(spin: Spinner,
                                       fragmentAddRegistryBinding: FragmentAddRegistryBinding){
        // Creating adapter for spinner
        val adapter = ArrayAdapter<String>(
            spin.context,
            R.layout.support_simple_spinner_dropdown_item, spinnerEmployees,
        )
        spin.adapter = adapter

        //Setting the item selected listener
        spin.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                Toast.makeText(
                    spin?.context,
                    "Selected Employee: " + spinnerEmployees.get(position),
                    Toast.LENGTH_SHORT
                ).show()
                if (position != 0){
                    //Set the employees data
                    emplPosition = spinnerEmplId.get(position-1)
                    println("Selected Employees Id is: " + spinnerEmplId.get(position-1))
                } else{
                    emplPosition = -1
                }
            }

            override fun onNothingSelected(arg0: AdapterView<*>?) {
                // TODO - Custom Code
            }
        }

    }

    private fun validateInputData(context: Context){
        if(emplPosition != -1 && hasInputCheckInData() && hasInputCheckOutData()){
            insertRegistry(context)
        }else{
            Toast.makeText(
                context,
                "Please select every field",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun insertRegistry(context: Context){
        val checkInDateTime = LocalDateTime.of(yearCheckIn, monthCheckIn, dayCheckIn, hourCheckIn,
            minuteCheckIn,0, 0)
        println("Check in on insertRegistry: " + checkInDateTime)

        val checkOutDateTime = LocalDateTime.of(yearCheckOut, monthCheckOut, dayCheckOut,
            hourCheckOut, minuteCheckOut,0, 0)
        println("Check out on insertRegistry: " + checkOutDateTime)

        if (checkInDateTime != null && checkOutDateTime != null && emplPosition != -1){
            //Create a new registry for today calling the checkIn method
            val reg= Registry(employeeId = emplPosition.toLong(),
                startedAt = checkInDateTime, endedAt = checkOutDateTime)
            shareViewModel.insertRegistry(reg)
            goBack()
        }else{
            Toast.makeText(
                context,
                "Please select every field",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun convertIntToTwoDigitString(value: Int) :String{
        var result: String=value.toString()
        if (value < 10){
            result="0${result}"
        }
        return result
    }

    private fun hasInputCheckInData(): Boolean{
        var result = false
        if (dayCheckIn != -1 && monthCheckIn != -1 && yearCheckIn != -1 && hourCheckIn != -1 &&
            minuteCheckIn != -1){
            result = true
        }
        return result
    }

    private fun hasInputCheckOutData(): Boolean{
        var result = false
        if (dayCheckOut != -1 && monthCheckOut != -1 && yearCheckOut != -1 && hourCheckOut != -1 &&
            minuteCheckOut != -1){
            result = true
        }
        return result
    }

    private fun initiateInputData(){
        selectedTime = -1

        dayCheckIn = -1
        monthCheckIn = -1
        yearCheckIn = -1
        hourCheckIn = -1
        minuteCheckIn = -1

        dayCheckOut = -1
        monthCheckOut = -1
        yearCheckOut = -1
        hourCheckOut = -1
        minuteCheckOut = -1
    }

}