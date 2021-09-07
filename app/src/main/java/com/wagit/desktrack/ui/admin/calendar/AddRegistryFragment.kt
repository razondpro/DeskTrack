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
import android.os.Bundle
import android.text.format.DateFormat
import android.widget.Button
import android.widget.DatePicker
import android.widget.TextView
import android.widget.TimePicker
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.wagit.desktrack.data.entities.Employee
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
    var IdBtnInTime = 0
    var IdBtnOutTime = 0
    var selectedTime = -1
    var spinnerEmployees = mutableListOf<String>("")
    var spinnerEmplId = mutableListOf<Int>()
    var emplPosition = -1

    private val shareViewModel: SharedViewModel by activityViewModels()

    override fun FragmentAddRegistryBinding.initialize(){
        println("Welcome to AddRegistryFragment!!!!")
        this.btnGoBackFromAddReg.setOnClickListener {
            goBack()
        }
        /*
        var myDayIn = 0
        var myMonthIn: Int = 0
        var myYearIn: Int = 0
        var myHourIn: Int = 0
        var myMinuteIn: Int = 0

        var myDayOut = 0
        var myMonthOut: Int = 0
        var myYearOut: Int = 0
        var myHourOut: Int = 0
        var myMinuteOut: Int = 0
         */
        var spin = this.spinEmployeeToRegister
        updateSnipperEmployee(spin,this)

        shareViewModel.employees.observe(viewLifecycleOwner, Observer {
            updateSnipperEmployee(spin,this)
        })

        selectedTime = -1
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

        var employees = shareViewModel.getAllEmployees().value
    }

    private fun goBack(){
        val fragmentManager = (activity as FragmentActivity).supportFragmentManager
        fragmentManager.popBackStackImmediate()
        println("LLEGA AL GOBACK() DEL COMPANY !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!")
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        myDay = day
        myYear = year
        myMonth = month
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
            textViewInTime.text = "Year: " + myYear + "\n" + "Month: " + myMonth +
                    "\n" + "Day: " + myDay +
                    "\n" + "Hour: " + myHour + "\n" + "Minute: " + myMinute
        }
        if (selectedTime == 2){
            textViewOutTime.text = "Year: " + myYear + "\n" + "Month: " + myMonth +
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
                    //Todo: Got the employee's Id
                } else{
                    emplPosition = -1
                }
            }

            override fun onNothingSelected(arg0: AdapterView<*>?) {
                // TODO - Custom Code
            }
        }

    }


}