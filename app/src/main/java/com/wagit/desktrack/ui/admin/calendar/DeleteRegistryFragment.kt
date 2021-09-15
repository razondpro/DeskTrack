package com.wagit.desktrack.ui.admin.calendar

import android.app.AlertDialog
import android.view.View
import com.wagit.desktrack.R
import com.wagit.desktrack.databinding.FragmentDeleteRegistryBinding
import com.wagit.desktrack.ui.BaseFragment
import com.wagit.desktrack.ui.admin.viewmodel.SharedViewModel
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import android.app.DatePickerDialog
import android.content.Context
import android.widget.DatePicker
import android.widget.TextView
import androidx.lifecycle.Observer
import com.wagit.desktrack.data.entities.Company
import com.wagit.desktrack.data.entities.Employee
import com.wagit.desktrack.data.entities.Registry
import java.util.*

class DeleteRegistryFragment: BaseFragment<FragmentDeleteRegistryBinding>
    (R.layout.fragment_delete_registry),
    DatePickerDialog.OnDateSetListener {

    private val shareViewModel: SharedViewModel by activityViewModels()
    lateinit var textView: TextView
    lateinit var fragmentDeleteRegistryBindingGlobal: FragmentDeleteRegistryBinding
    var day = 0
    var month: Int = 0
    var year: Int = 0

    var myDay: Int = 0
    var myMonth: Int = 0
    var myYear: Int = 0

    var spinnerCompanies = mutableListOf<String>("")
    var spinnerComplId = mutableListOf<Int>()
    var complPosition = -1

    var spinnerEmployees = mutableListOf<String>("")
    var spinnerEmplId = mutableListOf<Int>()
    var emplPosition = -1

    var employee = listOf<Employee>()
    var company = listOf<Company>()
    var registry = listOf<Registry>()

    override fun FragmentDeleteRegistryBinding.initialize() {
        println("Welcome to DeleteRegistryFragment!!!")
        fragmentDeleteRegistryBindingGlobal = this

        this.btnGoBackFromDelReg.setOnClickListener {
            goBack()
        }

        var spin = this.spinEmployeeToDelRegister
        var spinCompany = this.spinCompanyToDelRegister
        textView = this.tvSelectedRegTimeToDelete
        myDay = 0
        myMonth = 0
        myYear = 0

        updateSnipperEmployee(spin, this)

        shareViewModel.companies.observe(viewLifecycleOwner, Observer {
            updateSnipperCompany(spinCompany, this)
        })

        shareViewModel.company.observe(viewLifecycleOwner, Observer {
            getCompanysEmployees(this)
            updateCompanyData()
        })

        shareViewModel.employee.observe(viewLifecycleOwner, Observer {
            updateEmployeeData()
        })

        shareViewModel.employees.observe(viewLifecycleOwner, Observer {
            updateSnipperEmployee(spin, this)
        })

        shareViewModel.registry.observe(viewLifecycleOwner, Observer {
            //Llamar a update data
            updateRegistry(this)
        })

        this.btnPickRegTimeToDel.setOnClickListener {
            val calendar: Calendar = Calendar.getInstance()
            day = calendar.get(Calendar.DAY_OF_MONTH)
            month = calendar.get(Calendar.MONTH)
            year = calendar.get(Calendar.YEAR)
            val datePickerDialog =
                DatePickerDialog(it.context, this@DeleteRegistryFragment, year, month, day)
            datePickerDialog.show()
        }

        this.btnDeleteRegistry.setOnClickListener {
            //Validate Input && Delete register if exists
            context?.let { it1 -> validateInputData(it1,this) }
        }

        var companies = shareViewModel.getAllCompanies().value

    }

    private fun goBack() {
        val fragmentManager = (activity as FragmentActivity).supportFragmentManager
        fragmentManager.popBackStackImmediate()
        println("LLEGA AL GOBACK() DEL COMPANY !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!")
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        myDay = dayOfMonth
        myYear = year
        myMonth = month + 1
        textView.setText("Selected Date: Day: $myDay, Month: $myMonth and Year: $myYear")

        context?.let { getRegistry(it,fragmentDeleteRegistryBindingGlobal) }
    }

    private fun updateSnipperEmployee(spin: Spinner,
        fragmentAddRegistryBinding: FragmentDeleteRegistryBinding) {
        var employeesAux = listOf<Employee>()
        if (shareViewModel.employees.value != null) {
            employeesAux = shareViewModel.employees.value!!

            println("Entra en updateSnipperEmployee con ${shareViewModel.employees.value!!}")
        }
        setSnipperEmployees(employeesAux, spin, fragmentAddRegistryBinding)
    }

    private fun setSnipperEmployees(
        employees: List<Employee>, spin: Spinner,
        fragmentDeleteRegistryBinding: FragmentDeleteRegistryBinding
    ) {
        spinnerEmployees = mutableListOf<String>("")
        spinnerEmplId = mutableListOf<Int>()
        // Spinner Drop down elements
        employees?.forEach {
            if (it.isAdmin == false) {
                spinnerEmployees.add(it.firstName + " " + it.lastName)
                spinnerEmplId.add(it.id.toInt())
            }
        }
        setSnipperItemSelector(spin, fragmentDeleteRegistryBinding)
    }

    private fun setSnipperItemSelector(
        spin: Spinner,
        fragmentDeleteRegistryBinding:
        FragmentDeleteRegistryBinding
    ) {
        // Creating adapter for spinner
        val adapter = ArrayAdapter<String>(
            spin.context,
            R.layout.support_simple_spinner_dropdown_item, spinnerEmployees,
        )
        spin.adapter = adapter

        //Setting the item selected listener
        spin.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (position != 0) {
                    //Set the employees data
                    emplPosition = spinnerEmplId.get(position - 1)
                } else {
                    emplPosition = -1
                }
            }

            override fun onNothingSelected(arg0: AdapterView<*>?) {
                // TODO - Custom Code
            }
        }
    }

    private fun updateSnipperCompany(
        spin: Spinner, fragmentDeleteRegistryBinding:
        FragmentDeleteRegistryBinding
    ) {
        var companiesAux = listOf<Company>()
        if (shareViewModel.companies.value != null) {
            companiesAux = shareViewModel.companies.value!!

            println("Entra en updateSnipperCompany con ${shareViewModel.companies.value!!}")
        }
        setSnipperCompanies(companiesAux, spin, fragmentDeleteRegistryBinding)
    }

    private fun setSnipperCompanies(companies: List<Company>, spin: Spinner,
        fragmentDeleteRegistryBinding: FragmentDeleteRegistryBinding) {
        spinnerCompanies = mutableListOf<String>("")
        spinnerComplId = mutableListOf<Int>()

        // Spinner Drop down elements
        companies?.forEach {
            spinnerCompanies.add(it.name)
            spinnerComplId.add(it.id.toInt())
        }
        setCompSnipperItemSelector(spin, fragmentDeleteRegistryBinding)
    }

    private fun setCompSnipperItemSelector(
        spin: Spinner, fragmentDeleteRegistryBinding:
        FragmentDeleteRegistryBinding
    ) {
        // Creating adapter for spinner
        val adapter = ArrayAdapter<String>(
            spin.context,
            R.layout.support_simple_spinner_dropdown_item, spinnerCompanies,
        )
        spin.adapter = adapter

        //Setting the item selected listener
        spin.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (position != 0) {
                    //Set the employees data
                    complPosition = spinnerComplId.get(position - 1)
                    var companyAux = listOf<Company>()
                    if (shareViewModel.getCompany(complPosition.toLong()).value != null) {
                        companyAux = shareViewModel.company.value!!
                    }
                } else {
                    complPosition = -1
                    var employeesAux = listOf<Employee>()
                    setSnipperEmployees(
                        employeesAux,
                        fragmentDeleteRegistryBinding.spinEmployeeToDelRegister,
                        fragmentDeleteRegistryBinding
                    )
                }
            }

            override fun onNothingSelected(arg0: AdapterView<*>?) {
                // TODO - Custom Code
            }
        }
    }

    private fun getCompanysEmployees(fragmentDeleteRegistryBinding:
                                     FragmentDeleteRegistryBinding){
        var employeesAux = listOf<Employee>()
        if (complPosition != -1){
            if (shareViewModel.getEmployeesByComp(complPosition.toLong()).value != null){
                employeesAux = shareViewModel.employees.value!!
                println("getCompanysEmployees ------------------------ " +
                        "${employeesAux.first().firstName}")
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

    // When User cilcks on dialog button, call this method
    private fun onRegistryDeleteAlertDialog(fragmentDeleteRegistryBinding:
                                            FragmentDeleteRegistryBinding) {
        //Instantiate builder variable
        val builder = AlertDialog.Builder(this@DeleteRegistryFragment.view?.context)

        // set title
        builder.setTitle("Delete Registry")

        //set content area
        builder.setMessage("Do you want to delete the registry with date: day $myDay, " +
                "month $myMonth " + "and year $myYear?")

        //set positive button
        builder.setPositiveButton(
            "Yes") { dialog, id ->
            // User clicked Update Now button
            Toast.makeText(this.context, "Deleting the registry",Toast.LENGTH_SHORT).show()
            //shareViewModel.deleteEmployee(emplPosition.toLong())
            deleteRegistry(fragmentDeleteRegistryBinding,registry)
            //fragmentAddEditEmployeeBinding.initialize()
        }

        //set negative button

        builder.setNegativeButton(
            "No") { dialog, id ->
            // User cancelled the dialog
        }

        //set neutral button
        /*
        builder.setNeutralButton("Reminder me latter") {dialog, id->
            // User Click on reminder me latter
        }
         */
        builder.show()
    }

    private fun validateInputData(context: Context, fragmentDeleteRegistryBinding:
    FragmentDeleteRegistryBinding){
        if(complPosition!=-1 && emplPosition != -1 && myDay != 0 && myMonth != 0 && myYear != 0){
            //deleteRegistry(context)
            onRegistryDeleteAlertDialog(fragmentDeleteRegistryBinding)
        }else{
            Toast.makeText(
                context,
                "Please select every field",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun getRegistry(context: Context, fragmentDeleteRegistryBinding:
    FragmentDeleteRegistryBinding){
        var regAux = listOf<Registry>()
        if (shareViewModel.getRegByEmployeeMonthYearAndDay(emplPosition.toLong(),
                myYear.toString(),myMonth.twoDigits(),myDay.twoDigits()).value != null &&
                shareViewModel.registry.value != null){
            regAux = shareViewModel.registry.value!!
        }

    }

    private fun deleteRegistry(fragmentDeleteRegistryBinding: FragmentDeleteRegistryBinding,
                               registry: List<Registry>){
        if (registry.isNotEmpty() && registry.first() != null){
            //Delete a registry
            val reg= Registry(id = registry.first().id, employeeId = emplPosition.toLong(),
                startedAt = registry.first().startedAt, endedAt = registry.first().endedAt)
            shareViewModel.deleteRegistry(reg)
            goBack()
        }else{
            Toast.makeText(
                context,
                "There is no registry no delete",
                Toast.LENGTH_SHORT
            ).show()
            textView.setText("There is no registry for date: Day: $myDay, Month: $myMonth and" +
                    "Year: $myYear")
        }
    }

    private fun updateRegistry(fragmentDeleteRegistryBinding: FragmentDeleteRegistryBinding){
        registry = listOf<Registry>()
        if (shareViewModel.registry.value != null){
            registry = shareViewModel.registry.value!!
        }
    }

    fun Int.twoDigits() =
        if (this <= 9) "0$this" else this.toString()
}