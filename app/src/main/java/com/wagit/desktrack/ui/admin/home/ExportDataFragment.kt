package com.wagit.desktrack.ui.admin.home

import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import com.wagit.desktrack.R
import com.wagit.desktrack.data.entities.Company
import com.wagit.desktrack.databinding.FragmentExportDataBinding
import com.wagit.desktrack.ui.BaseFragment
import com.wagit.desktrack.ui.admin.viewmodel.SharedViewModel
import androidx.lifecycle.Observer
import com.wagit.desktrack.data.entities.Employee

class ExportDataFragment: BaseFragment<FragmentExportDataBinding>(R.layout.fragment_export_data){
    private val shareViewModel: SharedViewModel by activityViewModels()
    var spinnerCompanies = mutableListOf<String>("")
    var spinnerComplId = mutableListOf<Int>()
    var complPosition = -1

    var spinnerEmployees = mutableListOf<String>("")
    var spinnerEmplId = mutableListOf<Int>()
    var emplPosition = -1

    var spinnerMonths =  mutableListOf<String>()
    var spinnerMonthId = mutableListOf<Int>()
    var monthPosition = -1

    override fun FragmentExportDataBinding.initialize() {
        println("WELCOME TO THE EXPORT DATA FRAGMENT!!!!!!!!!")

        var spinCompany = this.spinCompanyExp
        var spinEmployee = this.spinEmployeeExp
        var spinMonth = this.spinMonthExp

        var companies = shareViewModel.getAllCompanies().value
        spinnerMonths.add("JANUARY")
        spinnerMonths.add("FEBRUARY")
        spinnerMonths.add("APRIL")
        spinnerMonths.add("MAY")
        spinnerMonths.add("JUNE")
        spinnerMonths.add("JULY")
        spinnerMonths.add("AGOST")
        spinnerMonths.add("SEPTEMBER")
        spinnerMonths.add("OCTOBER")
        spinnerMonths.add("NOVEMBER")
        spinnerMonths.add("DECEMBER")

        updateSnipperMonth(spinMonth,this)

        shareViewModel.companies.observe(viewLifecycleOwner, Observer {
            updateSnipperCompany(spinCompany,this)
        })

        shareViewModel.company.observe(viewLifecycleOwner, Observer {
            getCompanysEmployees(this)
        })

        shareViewModel.employees.observe(viewLifecycleOwner, Observer {
            updateSnipperEmployee(spinEmployee,this)
        })

        btnGoBackHome.setOnClickListener {
            goBack()
        }
    }

    private fun goBack(){
        val fragmentManager = (activity as FragmentActivity).supportFragmentManager
        fragmentManager.popBackStackImmediate()
        println("LLEGA AL GOBACK() !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!")
    }

    private fun updateSnipperCompany(spin: Spinner,
                                     fragmentExportDataBinding:
                                      FragmentExportDataBinding
    ){
        var companiesAux = listOf<Company>()
        if (shareViewModel.companies.value != null){
            companiesAux = shareViewModel.companies.value!!

            println("Entra en updateSnipperCompany con ${shareViewModel.companies.value!!}")
        }
        setSnipperCompanies(companiesAux, spin,fragmentExportDataBinding)
    }

    private fun setSnipperCompanies(companies: List<Company>, spin: Spinner,
                                    fragmentExportDataBinding:
                                    FragmentExportDataBinding){
        spinnerCompanies = mutableListOf<String>("")
        spinnerComplId = mutableListOf<Int>()

        // Spinner Drop down elements
        companies?.forEach {
            spinnerCompanies.add(it.name)
            spinnerComplId.add(it.id.toInt())
        }
        setCompSnipperItemSelector(spin,fragmentExportDataBinding)
    }

    private fun setCompSnipperItemSelector(spin: Spinner,
                                           fragmentExportDataBinding:
                                           FragmentExportDataBinding){
        // Creating adapter for spinner
        val adapter = ArrayAdapter<String>(spin.context,
            R.layout.support_simple_spinner_dropdown_item,spinnerCompanies,)
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
                    setSnipperEmployees(employeesAux,fragmentExportDataBinding.spinEmployeeExp,
                    fragmentExportDataBinding)
                }
            }

            override fun onNothingSelected(arg0: AdapterView<*>?) {
                // TODO - Custom Code
            }
        }
    }

    private fun getCompanysEmployees(fragmentExportDataBinding: FragmentExportDataBinding){
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
                                     fragmentExportDataBinding:
                                     FragmentExportDataBinding
    ){
        var employeesAux = listOf<Employee>()
        if (shareViewModel.employees.value != null){
            employeesAux = shareViewModel.employees.value!!

            println("Entra en updateSnipperEmployee con ${shareViewModel.employees.value!!}")
        }
        setSnipperEmployees(employeesAux, spin, fragmentExportDataBinding)
    }

    private fun setSnipperEmployees(employees: List<Employee>, spin: Spinner,
                                    fragmentExportDataBinding:
                                    FragmentExportDataBinding){
        spinnerEmployees = mutableListOf<String>("")
        spinnerEmplId = mutableListOf<Int>()

        // Spinner Drop down elements
        employees?.forEach {
            if (it.isAdmin == false){
                spinnerEmployees.add(it.firstName + " " + it.lastName)
                spinnerEmplId.add(it.id.toInt())
            }
        }
        setEmpSnipperItemSelector(spin,fragmentExportDataBinding)
    }

    private fun setEmpSnipperItemSelector(spin: Spinner,
                                           fragmentExportDataBinding:
                                           FragmentExportDataBinding){
        // Creating adapter for spinner
        val adapter = ArrayAdapter<String>(spin.context,
            R.layout.support_simple_spinner_dropdown_item,spinnerEmployees,)
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

    private fun updateSnipperMonth(spin: Spinner,
                                      fragmentExportDataBinding:
                                      FragmentExportDataBinding
    ){
        var monthAux = listOf<String>()
        if (spinnerMonths.isNotEmpty()){
            monthAux = spinnerMonths
            println("Entra en updateSnipperMonth con ${spinnerMonths.first()}")
        }
        setSnipperMonths(monthAux, spin, fragmentExportDataBinding)
    }

    private fun setSnipperMonths(months: List<String>, spin: Spinner,
                                    fragmentExportDataBinding:
                                    FragmentExportDataBinding){
        spinnerMonths = mutableListOf<String>("")
        spinnerMonthId = mutableListOf<Int>()

        // Spinner Drop down elements
        var i: Int
        i=0
        months?.forEach {
            if(it != " "){
                spinnerMonths.add(it)
                spinnerMonthId.add(i)
                i+=1
            }

        }
        setMonthSnipperItemSelector(spin,fragmentExportDataBinding)
    }

    private fun setMonthSnipperItemSelector(spin: Spinner,
                                          fragmentExportDataBinding:
                                          FragmentExportDataBinding){
        // Creating adapter for spinner
        val adapter = ArrayAdapter<String>(spin.context,
            R.layout.support_simple_spinner_dropdown_item,spinnerMonths,)
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
                    "Selected Month: " + spinnerMonths.get(position),
                    Toast.LENGTH_SHORT
                ).show()
                if (position != 0){
                    //Set the employees data
                    monthPosition = spinnerMonthId.get(position-1)
                    println("Selected Month's Id is: " + spinnerMonthId.get(position-1))
                } else{
                    monthPosition = -1
                    //boton save (a delete) y añadirle el listener para que añada
                }
            }

            override fun onNothingSelected(arg0: AdapterView<*>?) {
                // TODO - Custom Code
            }
        }
    }
}