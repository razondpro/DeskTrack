package com.wagit.desktrack.ui.admin.calendar

import android.content.Context
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import com.wagit.desktrack.R
import com.wagit.desktrack.databinding.FragmentAdminSelectEmployeeCalendarBinding
import com.wagit.desktrack.ui.BaseFragment
import com.wagit.desktrack.ui.admin.viewmodel.SharedViewModel
import androidx.lifecycle.Observer
import com.wagit.desktrack.data.entities.Company
import com.wagit.desktrack.data.entities.Employee
import com.wagit.desktrack.data.entities.Registry
import com.wagit.desktrack.databinding.FragmentExportDataBinding
import com.wagit.desktrack.ui.admin.calendar.view.CalendarFragment


class SelectEmployeeCalendar: BaseFragment<FragmentAdminSelectEmployeeCalendarBinding>
    (R.layout.fragment_admin_select_employee_calendar) {
    private val shareViewModel: SharedViewModel by activityViewModels()
    val calendarFragment = CalendarFragment()

    var spinnerCompanies = mutableListOf<String>("")
    var spinnerComplId = mutableListOf<Int>()
    var complPosition = -1

    var spinnerEmployees = mutableListOf<String>("")
    var spinnerEmplId = mutableListOf<Int>()
    var emplPosition = -1

    var registries = listOf<Registry>()
    var employee = listOf<Employee>()
    var company = listOf<Company>()

    override fun FragmentAdminSelectEmployeeCalendarBinding.initialize() {
        println("Welcome to SelectEmployeeCalendar")
        var spinCompany = this.spinCompanyCal
        var spinEmployee = this.spinEmployeeCal

        shareViewModel.companies.observe(viewLifecycleOwner, Observer {
            updateSnipperCompany(spinCompany,this)
        })

        shareViewModel.company.observe(viewLifecycleOwner, Observer {
            getCompanysEmployees(this)
            updateCompanyData()
        })

        shareViewModel.employees.observe(viewLifecycleOwner, Observer {
            updateSnipperEmployee(spinEmployee,this)
        })

        shareViewModel.employee.observe(viewLifecycleOwner, Observer {
            updateEmployeeData()
        })

        this.btnGoBackHomeFromSelEmplCal.setOnClickListener {
            goBack()
        }

        this.btnLoadCalendar.setOnClickListener {
            validateInputData(it.context)
        }

        var companies = shareViewModel.getAllCompanies().value

    }

    private fun goBack(){
        val fragmentManager = (activity as FragmentActivity).supportFragmentManager
        fragmentManager.popBackStackImmediate()
        println("LLEGA AL GOBACK() !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!")
    }

    private fun updateSnipperCompany(spin: Spinner,
                                     fragmentAdminSelectEmployeeCalendarBinding:
                                     FragmentAdminSelectEmployeeCalendarBinding
    ){
        var companiesAux = listOf<Company>()
        if (shareViewModel.companies.value != null){
            companiesAux = shareViewModel.companies.value!!

            println("Entra en updateSnipperCompany con ${shareViewModel.companies.value!!}")
        }
        setSnipperCompanies(companiesAux, spin, fragmentAdminSelectEmployeeCalendarBinding)
    }

    private fun setSnipperCompanies(companies: List<Company>, spin: Spinner,
                                    fragmentAdminSelectEmployeeCalendarBinding:
                                    FragmentAdminSelectEmployeeCalendarBinding){
        spinnerCompanies = mutableListOf<String>("")
        spinnerComplId = mutableListOf<Int>()

        // Spinner Drop down elements
        companies?.forEach {
            spinnerCompanies.add(it.name)
            spinnerComplId.add(it.id.toInt())
        }
        setCompSnipperItemSelector(spin,fragmentAdminSelectEmployeeCalendarBinding)
    }

    private fun setCompSnipperItemSelector(spin: Spinner,
                                           fragmentAdminSelectEmployeeCalendarBinding:
                                           FragmentAdminSelectEmployeeCalendarBinding){
        // Creating adapter for spinner
        val adapter = ArrayAdapter<String>(
            spin.context,
            R.layout.support_simple_spinner_dropdown_item, spinnerCompanies,
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
                    "Selected Company: " + spinnerCompanies.get(position),
                    Toast.LENGTH_SHORT
                ).show()
                if (position != 0){
                    //Set the employees data
                    complPosition = spinnerComplId.get(position-1)
                    println("Selected Company's Id is: " + spinnerComplId.get(position-1))
                    var companyAux = listOf<Company>()
                    if (shareViewModel.getCompany(complPosition.toLong()).value != null){
                        companyAux = shareViewModel.company.value!!
                        println("Company: $companyAux ------------------------------------------")
                    }
                } else{
                    complPosition = -1
                    var employeesAux = listOf<Employee>()
                    setSnipperEmployees(employeesAux,
                        fragmentAdminSelectEmployeeCalendarBinding.spinEmployeeCal,
                        fragmentAdminSelectEmployeeCalendarBinding)
                }
            }

            override fun onNothingSelected(arg0: AdapterView<*>?) {
                // TODO - Custom Code
            }
        }
    }

    private fun getCompanysEmployees(fragmentAdminSelectEmployeeCalendarBinding:
                                     FragmentAdminSelectEmployeeCalendarBinding){
        var employeesAux = listOf<Employee>()
        if (complPosition != -1){
            if (shareViewModel.getEmployeesByComp(complPosition.toLong()).value != null){
                employeesAux = shareViewModel.employees.value!!
                println("getCompanysEmployees ------------------------ " +
                        "${employeesAux.first().firstName}")
            }
        }

    }

    private fun updateSnipperEmployee(spin: Spinner,
                                      fragmentAdminSelectEmployeeCalendarBinding:
                                      FragmentAdminSelectEmployeeCalendarBinding
    ){
        var employeesAux = listOf<Employee>()
        if (shareViewModel.employees.value != null){
            employeesAux = shareViewModel.employees.value!!

            println("Entra en updateSnipperEmployee con ${shareViewModel.employees.value!!}")
        }
        setSnipperEmployees(employeesAux, spin, fragmentAdminSelectEmployeeCalendarBinding)
    }

    private fun setSnipperEmployees(employees: List<Employee>, spin: Spinner,
                                    fragmentAdminSelectEmployeeCalendarBinding:
                                    FragmentAdminSelectEmployeeCalendarBinding){
        spinnerEmployees = mutableListOf<String>("")
        spinnerEmplId = mutableListOf<Int>()

        // Spinner Drop down elements
        employees?.forEach {
            if (it.isAdmin == false){
                spinnerEmployees.add(it.firstName + " " + it.lastName)
                spinnerEmplId.add(it.id.toInt())
            }
        }
        setEmpSnipperItemSelector(spin,fragmentAdminSelectEmployeeCalendarBinding)
    }

    private fun setEmpSnipperItemSelector(spin: Spinner,
                                          fragmentAdminSelectEmployeeCalendarBinding:
                                          FragmentAdminSelectEmployeeCalendarBinding){
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
                    println("Selected Employee's Id is: " + spinnerEmplId.get(position-1))
                    getEmployeeData()
                } else{
                    emplPosition = -1
                    //boton save (a delete) y añadirle el listener para que añada
                }
            }

            override fun onNothingSelected(arg0: AdapterView<*>?) {
                // TODO - Custom Code
            }
        }
    }

    private fun getEmployeeData(){
        var employeeAux = listOf<Employee>()
        if (emplPosition != -1){
            if (shareViewModel.getEmployee(emplPosition).value != null){
                if (shareViewModel.employees.value != null){
                    employeeAux = shareViewModel.employees.value!!
                    println("Employee's selected: ${employeeAux.first()}")
                }
            }
        }
    }

    private fun updateEmployeeData(){
        employee = listOf<Employee>()
        if (shareViewModel.employee.value != null){
            employee = shareViewModel.employee.value!!
        }
    }

    private fun updateCompanyData(){
        company = listOf<Company>()
        if (shareViewModel.company.value != null){
            company = shareViewModel.company.value!!
        }
    }

    private fun validateInputData(context: Context){
       if (complPosition != -1 && emplPosition != -1){
           goToCalendar()
       }else{
           Toast.makeText(this.context, "Please select all fields",
               Toast.LENGTH_SHORT).show()
       }
    }

    private fun goToCalendar(){
        val fragmentManager = (activity as FragmentActivity).supportFragmentManager
        fragmentManager.beginTransaction().apply {
            replace(R.id.fragmentAdminContainerView,calendarFragment)
            addToBackStack("calendarFragment")
            commit()
        }
    }

}