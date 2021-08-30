package com.wagit.desktrack.ui.admin.home

import android.content.Context
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
import com.wagit.desktrack.data.entities.Registry
import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import androidx.core.content.ContextCompat
import com.itextpdf.text.Document
import com.itextpdf.text.Paragraph
import com.itextpdf.text.pdf.PdfWriter
import java.io.FileOutputStream
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class ExportDataFragment: BaseFragment<FragmentExportDataBinding>(R.layout.fragment_export_data){
    private val shareViewModel: SharedViewModel by activityViewModels()
    private val STORAGE_CODE: Int = 100;
    var spinnerCompanies = mutableListOf<String>("")
    var spinnerComplId = mutableListOf<Int>()
    var complPosition = -1

    var spinnerEmployees = mutableListOf<String>("")
    var spinnerEmplId = mutableListOf<Int>()
    var emplPosition = -1

    var spinnerMonths =  mutableListOf<String>()
    var spinnerMonthsInNumber =  mutableListOf<String>()
    var spinnerMonthId = mutableListOf<Int>()
    var monthPosition = -1

    var registries = listOf<Registry>()

    override fun FragmentExportDataBinding.initialize() {
        println("WELCOME TO THE EXPORT DATA FRAGMENT!!!!!!!!!")

        var spinCompany = this.spinCompanyExp
        var spinEmployee = this.spinEmployeeExp
        var spinMonth = this.spinMonthExp

        var companies = shareViewModel.getAllCompanies().value

        setMonthsArray()
        setMonthsInNumbersArray()
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

        shareViewModel.monthRegistry.observe(viewLifecycleOwner, Observer {
            updateRegistriesDocument(this)
        })

        btnGoBackHome.setOnClickListener {
            goBack()
        }

        btnExport.setOnClickListener {
            //we need to handle runtime permission for devices with marshmallow and above
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M){
                //system OS >= Marshmallow(6.0), check permission is enabled or not
                if (this@ExportDataFragment.context?.let { it1 ->
                        ContextCompat.checkSelfPermission(
                            it1,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    }
                    == PackageManager.PERMISSION_DENIED){
                    //permission was not granted, request it
                    val permissions = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    requestPermissions(permissions, STORAGE_CODE)
                }
                else{
                    //permission already granted, call savePdf() method
                    getRegistriesByEmployeeAndMonth(it.context)
                }
            }
            else{
                //system OS < marshmallow, call savePdf() method
                getRegistriesByEmployeeAndMonth(it.context)
            }
        }

    }

    private fun goBack(){
        val fragmentManager = (activity as FragmentActivity).supportFragmentManager
        fragmentManager.popBackStackImmediate()
        println("LLEGA AL GOBACK() !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!")
    }

    private fun setMonthsArray(){
        spinnerMonths.add("JANUARY")
        spinnerMonths.add("FEBRUARY")
        spinnerMonths.add("MARCH")
        spinnerMonths.add("APRIL")
        spinnerMonths.add("MAY")
        spinnerMonths.add("JUNE")
        spinnerMonths.add("JULY")
        spinnerMonths.add("AGOST")
        spinnerMonths.add("SEPTEMBER")
        spinnerMonths.add("OCTOBER")
        spinnerMonths.add("NOVEMBER")
        spinnerMonths.add("DECEMBER")
    }

    private fun setMonthsInNumbersArray(){
        spinnerMonthsInNumber.add("01")
        spinnerMonthsInNumber.add("02")
        spinnerMonthsInNumber.add("03")
        spinnerMonthsInNumber.add("04")
        spinnerMonthsInNumber.add("05")
        spinnerMonthsInNumber.add("06")
        spinnerMonthsInNumber.add("07")
        spinnerMonthsInNumber.add("08")
        spinnerMonthsInNumber.add("09")
        spinnerMonthsInNumber.add("10")
        spinnerMonthsInNumber.add("11")
        spinnerMonthsInNumber.add("12")
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

    private fun getRegistriesByEmployeeAndMonth(context: Context){
        if (complPosition != -1 && emplPosition != -1 && monthPosition != -1){
            var registriesAux = listOf<Registry>()
            var month = spinnerMonthsInNumber.get(monthPosition)
            if (shareViewModel.getRegistriesByEmployeeAndMonth(complPosition.toLong(),
                month) != null){
                if (shareViewModel.monthRegistry.value != null)
                    registriesAux = shareViewModel.monthRegistry.value!!
                //println("Registry: $registriesAux ------------------------------------------")
            }
        }else{
            Toast.makeText(context, "Please, choose all three elements",
                Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateRegistriesDocument(fragmentExportDataBinding:
                                      FragmentExportDataBinding
    ){
        //var registriesAux = listOf<Registry>()
        registries = listOf<Registry>()
        if (shareViewModel.monthRegistry.value != null){
            //registriesAux = shareViewModel.monthRegistry.value!!
            registries = shareViewModel.monthRegistry.value!!

            println("Entra en updateRegistriesDocument con " +
                    "${shareViewModel.monthRegistry.value!!}")
        }
        setRegistriesDocument(fragmentExportDataBinding)
    }

    private fun setRegistriesDocument(fragmentExportDataBinding: FragmentExportDataBinding){
        registries.forEach {
            println("REGISTRY STARTED AT: ${it.startedAt}, " +
                    "ENDED AT: ${it.endedAt} AND EMPLOYEE ID: ${it.employeeId}")
        }

        savePdf()
    }

    private fun savePdf() {
        //create object of Document class
        val mDoc = Document()
        //pdf file name
        val mFileName = SimpleDateFormat("yyyyMMdd_HHmmss",
            Locale.getDefault()).format(System.currentTimeMillis())
        //pdf file path
        val mFilePath = this.context?.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).
        toString() + "/" + mFileName +".pdf"
        try {
            //create instance of PdfWriter class
            PdfWriter.getInstance(mDoc, FileOutputStream(mFilePath))

            //open the document for writing
            mDoc.open()

            //get text from registries list
            registries.forEach {
                val mText = "REGISTRY STARTED AT: ${it.startedAt}, " +
                        "ENDED AT: ${it.endedAt} AND EMPLOYEE ID: ${it.employeeId}"

                //add paragraph to the document
                mDoc.add(Paragraph(mText))
            }

            //add author of the document (metadata)
            mDoc.addAuthor("Admin")

            //close document
            mDoc.close()

            //show file saved message with file name and path
            Toast.makeText(this.context, "$mFileName.pdf\nis saved to\n$mFilePath",
                Toast.LENGTH_SHORT).show()

            println("$mFileName.pdf\n" + "is saved to\n" + "$mFilePath")
        }
        catch (e: Exception){
            //if anything goes wrong causing exception, get and show exception message
            Toast.makeText(this.context, e.message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>,
                                            grantResults: IntArray) {
        when(requestCode){
            STORAGE_CODE -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    //permission from popup was granted, call savePdf() method
                    context?.let { getRegistriesByEmployeeAndMonth(it) }
                    Toast.makeText(this.context,
                        "Permission granted...!", Toast.LENGTH_SHORT).show()

                }
                else{
                    //permission from popup was denied, show error message
                    Toast.makeText(this.context,
                        "Permission denied...!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    




}