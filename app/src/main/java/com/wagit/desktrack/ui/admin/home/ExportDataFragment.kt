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
import android.os.Environment
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.element.Table
import com.itextpdf.layout.property.TextAlignment
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Cell
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.property.UnitValue
import java.lang.Exception
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*
import com.wagit.desktrack.ui.helpers.MonthYearPickerDialog


class ExportDataFragment: BaseFragment<FragmentExportDataBinding>(R.layout.fragment_export_data){
    private val shareViewModel: SharedViewModel by activityViewModels()
    private var STORAGE_CODE: Int = 100
    var spinnerCompanies = mutableListOf<String>("")
    var spinnerComplId = mutableListOf<Int>()
    var complPosition = -1

    var spinnerEmployees = mutableListOf<String>("")
    var spinnerEmplId = mutableListOf<Int>()
    var emplPosition = -1

    var myMonth = -1
    var myYear = -1

    var registries = listOf<Registry>()
    var employee = listOf<Employee>()
    var company = listOf<Company>()

    override fun FragmentExportDataBinding.initialize() {
        println("WELCOME TO THE EXPORT DATA FRAGMENT!!!!!!!!!")

        var spinCompany = this.spinCompanyExp
        var spinEmployee = this.spinEmployeeExp

        myMonth = -1
        myYear = -1

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

        shareViewModel.monthRegistry.observe(viewLifecycleOwner, Observer {
            updateRegistriesDocument(this)
        })

        btnGoBackHome.setOnClickListener {
            goBack()
        }

        btnExport.setOnClickListener {
            //we need to handle runtime permission for devices with marshmallow and above
            if (complPosition != -1 && emplPosition != -1 && myMonth != -1 && myYear != -1){
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M){
                    //system OS >= Marshmallow(6.0), check permission is enabled or not

                    if (this@ExportDataFragment.context?.let { it1 ->
                            ContextCompat.checkSelfPermission(
                                it1,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        }
                        == PackageManager.PERMISSION_DENIED){


                     //if (!!hasWriteExternalStoragePermission()){
                        //permission was not granted, request it
                        val permissions = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        requestPermissions(permissions, STORAGE_CODE)
                        //requestPermissions()
                    }
                    else{
                        //permission already granted, call savePdf() method

                        getRegistriesByEmployeeAndMonth(it.context)
                        //initiateData()
                        //this.initialize()
                        //goBack()
                    }
                }
                else{
                    //system OS < marshmallow, call savePdf() method
                    getRegistriesByEmployeeAndMonth(it.context)
                    //initiateData()
                    //this.initialize()
                    //goBack()
                }
            }else{
                Toast.makeText(context, "Please, choose all three elements",
                    Toast.LENGTH_SHORT).show()
            }
        }

        btnSelectMonthAndYear.setOnClickListener {
            MonthYearPickerDialog().apply {
                setListener { view, year, month, dayOfMonth ->
                    myMonth = month + 1
                    myYear = year
                    Toast.makeText(requireContext(), "Set date: $myYear/$myMonth",
                        Toast.LENGTH_LONG).show()
                    if (myYear != -1 && myMonth != -1){
                        tvSelectedMonthYear.setText("The selected Month and Year are " +
                                "$myMonth and $myYear")
                    }
                }
                show(this@ExportDataFragment.parentFragmentManager, "MonthYearPickerDialog")
            }
        }

        var companies = shareViewModel.getAllCompanies().value

    }

    private fun requestPermissions(){
        var permissionsToRequest = mutableListOf<String>()
        if (!!hasWriteExternalStoragePermission()){
            permissionsToRequest.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        if (permissionsToRequest.isNotEmpty()){
            ActivityCompat.requestPermissions(this.requireActivity(),
                permissionsToRequest.toTypedArray(),STORAGE_CODE)
        }
    }

    private fun hasWriteExternalStoragePermission() =
        ActivityCompat.checkSelfPermission(this.requireContext(),
            Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED


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

    private fun getRegistriesByEmployeeAndMonth(context: Context){
        println("LLEGA AL getRegistriesByEmployeeAndMonth con cp: $complPosition, " +
                "ep: $emplPosition, Mymonth: $myMonth y MyYear: $myYear")
        if (complPosition != -1 && emplPosition != -1 && myMonth != -1 && myYear != -1){
            var registriesAux = listOf<Registry>()
            var month = myMonth
            println("Month: $month ------------------------------------------------------------")
            if (shareViewModel.getAllRegistriesByEmployeeAndMonthAndYear(emplPosition.toLong(),
                    convertIntToTwoDigitString(month),
                    myYear.toString()).value != null){
                if (shareViewModel.monthRegistry.value != null)
                    registriesAux = shareViewModel.monthRegistry.value!!
                    println("Registry at getRegistriesByEmployeeAndMonth" +
                            ": $registriesAux ------------------------------------------")
            }
        }else{
            Toast.makeText(context, "Please, choose all three elements",
                Toast.LENGTH_SHORT).show()
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

    private fun setRegistriesDocument(fragmentExportDataBinding: FragmentExportDataBinding){
        registries.forEach {
            println("setRegistriesDocument: REGISTRY STARTED AT: ${it.startedAt}, " +
                    "ENDED AT: ${it.endedAt} AND EMPLOYEE ID: ${it.employeeId}")
            println("setRegistriesDocument:  con cp: $complPosition, " +
                    "ep: $emplPosition, Mymonth: $myMonth y MyYear: $myYear")
        }
        if (complPosition != -1 && emplPosition != -1 && myMonth != -1 && myYear != -1){
            savePdf(fragmentExportDataBinding)
            //fragmentExportDataBinding.initialize()
            goBack()
        }

    }

    private fun savePdf(fragmentExportDataBinding: FragmentExportDataBinding) {
        //create object of Document class

        //pdf file name
        val mFileName = SimpleDateFormat("yyyyMMdd_HHmmss",
            Locale.getDefault()).format(System.currentTimeMillis())
        //pdf file path
        val mFilePath = this.context?.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).
        toString() + "/" + mFileName +".pdf"
        val pdfDocument = PdfDocument(PdfWriter("$mFilePath"))
        val mDoc = Document(pdfDocument)
        try {
            //create instance of PdfWriter class
            //PdfWriter.getInstance(mDoc, FileOutputStream(mFilePath))

            //open the document for writing
            //mDoc.open()

            //get text from company list
            setCompaniesDataOnPDF(mDoc)

            //get text from employee list
            setEmployeesDataOnPDF(mDoc)

            //get text from registries list
            setRegistriesDataOnPDF(mDoc)

            //add author of the document (metadata)
            //mDoc.addAuthor("Admin")

            //close document
            mDoc.close()

            complPosition = -1
            emplPosition = -1
            myMonth = -1
            myYear = -1

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

    private fun setCompaniesDataOnPDF(mDoc: Document){
        //val font = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14f, BaseColor.BLACK);
        val mCompTitletext = "Registro de jornada -> Trabajadores"
        mDoc.add(Paragraph(mCompTitletext).setBold())
        mDoc.add(Paragraph(" "))

        if (company.isNotEmpty()){
            company.forEach {
                val mCompanyNameText = "Razón social: ${it.name}"
                mDoc.add(Paragraph(mCompanyNameText).setUnderline())
                val mCompanyText = "CIF: ${it.nif}                                " +
                        "CCC: ${it.ccc}"
                mDoc.add(Paragraph(mCompanyText))
                mDoc.add(Paragraph(" "))
            }
        }
    }

    private fun setEmployeesDataOnPDF(mDoc: Document){
        val mEmpTitletext = "Datos del trabajador"
        mDoc.add(Paragraph(mEmpTitletext).setUnderline())

        if (employee.isNotEmpty()){
            employee.forEach {
                var mEmpText="Nombre y apellidos: ${it.firstName} ${it.lastName}"
                mDoc.add(Paragraph(mEmpText))

                mEmpText = "CIF: ${it.cif}                                " +
                        "NSS: ${it.nss}"
                mDoc.add(Paragraph(mEmpText))
                mDoc.add(Paragraph(" "))
            }
        }

    }

    private fun setRegistriesDataOnPDF(mDoc: Document){

        var mRegTitleText = "Fecha: ${LocalDateTime.now()}"
        mDoc.add(Paragraph(mRegTitleText))
        mDoc.add(Paragraph(" "))

        if (registries.isNotEmpty()){
            // Creating a table
            val table = Table(UnitValue.createPercentArray(5)).useAllAvailableWidth()
            table.addHeaderCell(Cell().add(
                Paragraph("Día del mes").setTextAlignment(TextAlignment.CENTER)))

            table.addHeaderCell(Cell().add(
                Paragraph("Mes").setTextAlignment(TextAlignment.CENTER)))

            table.addHeaderCell(Cell().add(
                Paragraph("Hora de entrada").setTextAlignment(TextAlignment.CENTER)))

            table.addHeaderCell(Cell().add(
                Paragraph("Hora de salida").setTextAlignment(TextAlignment.CENTER)))

            table.addHeaderCell(Cell().add(
                Paragraph("Año").setTextAlignment(TextAlignment.CENTER)))

            registries.forEach {
                println("REG: ${it.startedAt.month}")
                var entryTime =
                    "${it.startedAt.hour}:${it.startedAt.minute}:${it.startedAt.second}"
                var departureTime =
                    "${it.endedAt?.hour}:${it.endedAt?.minute}:${it.endedAt?.second}"
                table.addCell(Cell().add(Paragraph(it.startedAt.dayOfMonth.toString()).
                setTextAlignment(TextAlignment.CENTER)))

                table.addCell(Cell().add(Paragraph(it.startedAt.month.toString()).
                setTextAlignment(TextAlignment.CENTER)))

                table.addCell(Cell().add(Paragraph(entryTime).
                setTextAlignment(TextAlignment.CENTER)))

                table.addCell(Cell().add(Paragraph(departureTime).
                setTextAlignment(TextAlignment.CENTER)))

                table.addCell(Cell().add(Paragraph(it.startedAt.year.toString()).
                setTextAlignment(TextAlignment.CENTER)))
            }

            mDoc.add(table)
        }else{
            var mText = ""
            if (myMonth != -1){
                println("Mnth position in setRegistriesDocument is ${myMonth}")
                //mText = "There is no register for ${spinnerMonths.get(monthPosition+1)}"
                mText = "No hay registros para el mes $myMonth del año $myYear"
                mDoc.add(Paragraph(mText))
            }
            Toast.makeText(this.context, "There is no registry to add",
                Toast.LENGTH_SHORT).show()
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

    private fun convertIntToTwoDigitString(month: Int) :String{
        var result: String=month.toString()
        if (month < 10){
            result="0${result}"
        }
        return result
    }

}