package com.wagit.desktrack.ui.admin.home

import android.app.AlertDialog
import android.util.Log
import androidx.fragment.app.activityViewModels
import com.wagit.desktrack.R
import com.wagit.desktrack.databinding.FragmentAddEditEmployeeBinding
import com.wagit.desktrack.ui.BaseFragment
import com.wagit.desktrack.ui.admin.viewmodel.SharedViewModel
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter
import android.widget.Spinner;
import com.wagit.desktrack.data.entities.Employee
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.wagit.desktrack.data.db.AppDatabase
import com.wagit.desktrack.data.entities.Company
import com.wagit.desktrack.utils.Validator
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class AddEditEmployeeFragment :
    BaseFragment<FragmentAddEditEmployeeBinding>(R.layout.fragment_add_edit_employee){
    private val shareViewModel: SharedViewModel by activityViewModels()
    var spinnerEmployees = mutableListOf<String>("")
    var spinnerEmplId = mutableListOf<Int>()
    var emplPosition = -1
    var employeesCIF = mutableListOf<String>("")
    var employeesNSS = mutableListOf<String>("")

    var spinnerCompanies = mutableListOf<String>("")
    var spinnerCompId = mutableListOf<Int>()
    var compPosition = -1

    private val emailLD = MutableLiveData<String>()
    private val pwLD = MutableLiveData<String>()
    private val cifLD = MutableLiveData<String>()

    private val isValidLD = MediatorLiveData<Boolean>().apply {
        this.value = false

        addSource(emailLD) { email ->
            val pw = pwLD.value
            val cif = cifLD.value
            this.value = validate(email, pw, cif)
        }
        addSource(pwLD){ pw ->
            val email = emailLD.value
            val cif = cifLD.value
            this.value = validate(email, pw, cif)
        }
        addSource(cifLD){ cif ->
            val email = emailLD.value
            val pw = pwLD.value
            this.value = validate(email, pw, cif)
        }
    }

    override fun FragmentAddEditEmployeeBinding.initialize() {
        println("WELCOME TO THE Employee FRAGMENT!!!!!!!!!")

        initiate()
        var spin = this.spinnerEmployee
        var spinEmployeeCompany = this.spinEmployeeCompany

        updateSnipperCompany(spinEmployeeCompany,this)
        updateSnipperEmployee(spin,this)

        shareViewModel.companies.observe(viewLifecycleOwner, Observer {
            updateSnipperCompany(spinEmployeeCompany,this)
        })

        shareViewModel.employees.observe(viewLifecycleOwner, Observer {
            updateSnipperEmployee(spin,this)
        })

        shareViewModel.company.observe(viewLifecycleOwner, Observer {
            updateEmployeeCompanyPosition(this)
        })

        shareViewModel.employee.observe(viewLifecycleOwner, Observer {
            updateEmployeeCompanyPosition(this)
        })

        btnGoBack.setOnClickListener {
            goBack()
        }

        var companies = shareViewModel.getAllCompanies().value
        var employees = shareViewModel.getAllEmployees().value

    }

    private fun initiate(){
        spinnerEmployees = mutableListOf<String>("")
        spinnerEmplId = mutableListOf<Int>()
        emplPosition = -1
        employeesCIF = mutableListOf<String>("")
        employeesNSS = mutableListOf<String>("")

        spinnerCompanies = mutableListOf<String>("")
        spinnerCompId = mutableListOf<Int>()
        compPosition = -1
    }

    private fun validateEditForm(fragmentAddEditEmployeeBinding: FragmentAddEditEmployeeBinding){
        fragmentAddEditEmployeeBinding.tvEmployeeCIF.doOnTextChanged { text, _, _, _ ->
            cifLD.value = text?.toString()
        }
        fragmentAddEditEmployeeBinding.tvEmployeePasswd.doOnTextChanged { text, _, _, _ ->
            pwLD.value = text?.toString()
        }
        fragmentAddEditEmployeeBinding.tvEmployeeEmail.doOnTextChanged { text, _, _, _ ->
            emailLD.value = text?.toString()
        }
        isValidLD.observe(this){ isValid ->
            Log.v("EditIsValid", isValid.toString())
        }
    }

    private fun validate(email: String?, pw: String?, cif: String?): Boolean{
        return Validator.isValidEmail(email) && !pw.isNullOrBlank() && pw.length >=6 &&
                Validator.isValidCIF(cif)
    }

    private fun goBack(){
        val fragmentManager = (activity as FragmentActivity).supportFragmentManager
        fragmentManager.popBackStackImmediate()
        println("LLEGA AL GOBACK() !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!")
    }

    private fun handleSaveClick(fragmentAddEditEmployeeBinding: FragmentAddEditEmployeeBinding){
        fragmentAddEditEmployeeBinding.btnSave.setOnClickListener {
            var empNSS = mutableListOf<String>("")
            var empCIF = mutableListOf<String>("")
            var isUniqueCIF = true
            var isUniqueNSS = true

            if (employeesCIF.isNotEmpty() && employeesNSS.isNotEmpty()){
                println("employeesCIF.size ------------------------------------------- " +
                        "${employeesCIF.size}")
                println("employeesNSS.size ------------------------------------------- " +
                        "${employeesNSS.size}")
                var fg=0
                employeesCIF.forEach {
                    println("Employees CIF: ${it} and index: $fg ---------------------------")
                    //compNif.add(it)
                    if (fg < employeesCIF.size){
                        println("Employees CIF: employeesCIF.get(index) ---------- " +
                                "${employeesCIF.get(fg)}")
                    }
                    fg+=1
                }

                fg=0
                employeesNSS.forEach {
                    println("Employees NSS: ${it} and index: $fg ---------------------------")
                    //compNif.add(it)
                    if (fg < employeesNSS.size){
                        println("Employees NSS: employeesCIF.get(index) ---------- " +
                                "${employeesNSS.get(fg)}")
                    }
                    fg+=1
                }

                empCIF = copyMutableList(employeesCIF)
                empNSS = copyMutableList(employeesNSS)
            }

            if (empCIF.isNotEmpty()){
                println("empCIF.size ------------------------------------------- " +
                        "${empCIF.size}")

                println("emplPosition: " +
                        "$emplPosition ----------------------------------------------")

                println("empCIF.get(emplPosition) ---------- ${empCIF.get(emplPosition)}")
                println("Removed: ${empCIF.removeAt(emplPosition)} ------------------------")
                println("Employee CIF TV: " +
                        "${fragmentAddEditEmployeeBinding.tvEmployeeCIF.text.toString()}")
                empCIF.forEach{
                    println("Employee CIF (empCIF): ${it} ---------------------------")
                }
                isUniqueCIF = isUniqueElement(empCIF,
                    fragmentAddEditEmployeeBinding.tvEmployeeCIF.text.toString())
                println("isUniqueNIF is $isUniqueCIF ---------")

            }

            if (empNSS.isNotEmpty()){
                println("empNSS.size ------------------------------------------- " +
                        "${empNSS.size}")

                println("emplPosition: " +
                        "$emplPosition ----------------------------------------------")
                println("empNSS.get(emplPosition) ---------- ${empNSS.get(emplPosition)}")
                println("Removed: ${empNSS.removeAt(emplPosition)} ------------------------")
                println("Employee NSS TV: " +
                        "${fragmentAddEditEmployeeBinding.tvEmployeeNss.text.toString()}")
                empNSS.forEach{
                    println("Employee NSS (empNSS): ${it} ---------------------------")
                }
                isUniqueNSS = isUniqueElement(empNSS,
                    fragmentAddEditEmployeeBinding.tvEmployeeNss.text.toString())
                println("isUniqueNSS is $isUniqueNSS ---------")

            }

            if (employeesCIF.isNotEmpty() && employeesNSS.isNotEmpty()){
                employeesCIF.forEach {
                    println("Employees CIF after remove: ${it} ---------------------------")
                }
                employeesNSS.forEach {
                    println("Employees NSS after remove: ${it} ---------------------------")
                }

            }

            if(isValidLD.value as Boolean && isUniqueCIF && isUniqueNSS){
                if (emplPosition != -1 && compPosition != -1){
                    shareViewModel.updateEmployee(emplPosition.toLong(),
                        fragmentAddEditEmployeeBinding.tvEmployeeEmail.text.toString(),
                        fragmentAddEditEmployeeBinding.tvEmployeePasswd.text.toString(),
                        fragmentAddEditEmployeeBinding.tvEmployeeFirstName.text.toString(),
                        fragmentAddEditEmployeeBinding.tvEmployeeLastName.text.toString(),
                        compPosition.toLong(),
                        fragmentAddEditEmployeeBinding.tvEmployeeCIF.text.toString(),
                        fragmentAddEditEmployeeBinding.tvEmployeeNss.text.toString())

                    compPosition = -1
                    emplPosition = -1
                    goBack()
                    //fragmentAddEditEmployeeBinding.initialize()
                }
            }else {
                Toast.makeText(it.context, "Please, insert valid data",
                    Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun attemptEditEmployee(){
        shareViewModel.employee.observe(this,{
            if(it.isEmpty()){
                Toast.makeText(this.context, "Wrong credentials", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun editViewInit(fragmentAddEditEmployeeBinding: FragmentAddEditEmployeeBinding){
        validateEditForm(fragmentAddEditEmployeeBinding)
        handleSaveClick(fragmentAddEditEmployeeBinding)
        attemptEditEmployee()
    }

    private fun addViewInit(fragmentAddEditEmployeeBinding: FragmentAddEditEmployeeBinding){
        validateEditForm(fragmentAddEditEmployeeBinding)
        handleAddClick(fragmentAddEditEmployeeBinding)
        attemptEditEmployee()
    }

    // When User cilcks on dialog button, call this method
    private fun onEmployeeDeleteAlertDialog(fragmentAddEditEmployeeBinding:
    FragmentAddEditEmployeeBinding) {
        //Instantiate builder variable
        val builder = AlertDialog.Builder(this@AddEditEmployeeFragment.view?.context)

        // set title
        builder.setTitle("Delete Employee")

        //set content area
        builder.setMessage("Do you want to delete the employee ${fragmentAddEditEmployeeBinding.
        tvEmployeeFirstName.text}  ${fragmentAddEditEmployeeBinding.tvEmployeeLastName.text}?")

        //set positive button
        builder.setPositiveButton(
            "Yes") { dialog, id ->
            // User clicked Update Now button
            Toast.makeText(this.context, "Deleting the employee",Toast.LENGTH_SHORT).show()
            shareViewModel.deleteEmployee(emplPosition.toLong())
            compPosition = -1
            emplPosition = -1
            goBack()
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

    private fun handleDeleteClick(fragmentAddEditEmployeeBinding: FragmentAddEditEmployeeBinding){
        fragmentAddEditEmployeeBinding.btnDelete.setOnClickListener {
            if (emplPosition != -1 && compPosition!=-1){
                onEmployeeDeleteAlertDialog(fragmentAddEditEmployeeBinding)
            }
        }
    }

    private fun handleAddClick(fragmentAddEditEmployeeBinding: FragmentAddEditEmployeeBinding){
        fragmentAddEditEmployeeBinding.btnSave.setOnClickListener {
            val isUniqueNSS = isUniqueElement(employeesNSS,
                fragmentAddEditEmployeeBinding.tvEmployeeNss.text.toString())
            var isUniqueCIF = isUniqueElement(employeesCIF,
                fragmentAddEditEmployeeBinding.tvEmployeeCIF.text.toString())

            if(isValidLD.value as Boolean && isUniqueCIF && isUniqueNSS){
                //if (emplPosition == -1 && compPosition != -1){
                if (compPosition != -1){
                    println("Llega handleAddClick con emplPosition: $emplPosition " +
                            "y compPosition: $compPosition <----------------->")

                    var job = GlobalScope.launch {
                        val instance = this@AddEditEmployeeFragment.context?.
                        let { AppDatabase.getInstance(it) }

                        val employee = Employee(
                            email = fragmentAddEditEmployeeBinding.tvEmployeeEmail.text.
                            toString(),
                            password = fragmentAddEditEmployeeBinding.tvEmployeePasswd.text.
                            toString(),
                            cif = fragmentAddEditEmployeeBinding.tvEmployeeCIF.text.toString(),
                            nss = fragmentAddEditEmployeeBinding.tvEmployeeNss.text.toString(),
                            firstName = fragmentAddEditEmployeeBinding.tvEmployeeFirstName.text.
                            toString(),
                            lastName = fragmentAddEditEmployeeBinding.tvEmployeeLastName.text.
                            toString(),
                            companyId = compPosition.toLong(),
                            isDeleted = false,
                            isAdmin = false
                        )

                        val empId = instance!!.employeeDao().insert(employee)

                        shareViewModel.getEmployee(empId.toInt()).value
                        if (shareViewModel.employee.value != null){
                            println("shareViewModel.employee.value es ${shareViewModel.employee
                                .value}")
                        }
                    }
                    //compPosition = -1
                    //emplPosition = -1
                    goBack()
                    //fragmentAddEditEmployeeBinding.initialize()
                    //job.cancel()
                    //job.join()


                    // Reload current fragment
                    /*
                    val fragmentManager = (activity as FragmentActivity).supportFragmentManager
                    var frg: Fragment? = null
                    frg = fragmentManager.findFragmentByTag("employeeFragment")
                    val ft: FragmentTransaction = fragmentManager.beginTransaction()
                    if (frg != null) {
                        ft.detach(frg)
                    }
                    if (frg != null) {
                        ft.attach(frg)
                    }
                    ft.commit()

                     */



                }
            }else{
                Toast.makeText(it.context, "Please, insert valid data",
                    Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun updateSnipperEmployee(spin: Spinner,
                                      fragmentAddEditEmployeeBinding:
                                      FragmentAddEditEmployeeBinding){
        var employeesAux = listOf<Employee>()
        if (shareViewModel.employees.value != null){
            employeesAux = shareViewModel.employees.value!!

            employeesCIF = mutableListOf<String>("")
            employeesNSS = mutableListOf<String>("")

            employeesAux.forEach {
                employeesCIF.add(it.cif.toString())
                employeesNSS.add(it.nss.toString())
            }

            if (employeesCIF.isNotEmpty()){
                employeesCIF.forEach {
                    println("Employees CIF in updateSnipperEmployee:" +
                            " ${it} ---------------------------")
                }
            }

            if (employeesNSS.isNotEmpty()){
                employeesNSS.forEach {
                    println("Employees NSS in updateSnipperEmployee:" +
                            " ${it} ---------------------------")
                }
            }


            println("Entra en updateSnipperEmployee con ${shareViewModel.employees.value!!}")
        }
        setSnipperEmployees(employeesAux, spin,fragmentAddEditEmployeeBinding)
    }

    private fun updateEmployeeCompanyPosition(fragmentAddEditEmployeeBinding:
                                       FragmentAddEditEmployeeBinding){
        var employeeAux = listOf<Employee>()
        var companyAux = listOf<Company>()
        if (shareViewModel.employee.value != null){

            println("Entra en updateEmployeeCompanyPosition antes del error con " +
                    "${shareViewModel.employee.value!!}, " +
                    "emplPosition: $emplPosition y compPosition: $compPosition")
            employeeAux = shareViewModel.employee.value!!
            compPosition = employeeAux.first().companyId!!.toInt()
            println("Entra en updateEmployeeCompanyPosition con " +
                    "${shareViewModel.employee.value!!}, " +
                    "emplPosition: $emplPosition y compPosition: $compPosition")
        }
        if (shareViewModel.company.value != null){

            companyAux = shareViewModel.company.value!!
            println("Entra en updateCompanyPosition con ${shareViewModel.company.value!!} y " +
                    "position: ${compPosition}")
        }
        if (emplPosition != -1 && compPosition != -1){
            setEmployeesData(fragmentAddEditEmployeeBinding,employeeAux,companyAux)
        }else{
            resetEmployeesData(fragmentAddEditEmployeeBinding)
        }
    }

    private fun setSnipperEmployees(employees: List<Employee>, spin: Spinner,
                                    fragmentAddEditEmployeeBinding:
                                    FragmentAddEditEmployeeBinding){
        spinnerEmployees = mutableListOf<String>("")
        spinnerEmplId = mutableListOf<Int>()
        employeesCIF = mutableListOf<String>("")
        employeesNSS = mutableListOf<String>("")

        // Spinner Drop down elements
        employees?.forEach {
            if (it.isAdmin == false){
                spinnerEmployees.add(it.firstName + " " + it.lastName)
                spinnerEmplId.add(it.id.toInt())
            }
            employeesNSS.add(it.nss.toString())
            employeesCIF.add(it.cif.toString())
        }
        setSnipperItemSelector(spin,fragmentAddEditEmployeeBinding)
    }

    private fun setSnipperItemSelector(spin: Spinner,
                                       fragmentAddEditEmployeeBinding:
                                       FragmentAddEditEmployeeBinding){
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
                    fragmentAddEditEmployeeBinding.btnSave.setText("Save")
                    //Set the employees data
                    emplPosition = spinnerEmplId.get(position-1)
                    println("Selected Employees Id is: " + spinnerEmplId.get(position-1))
                    getEmployeesCompany(fragmentAddEditEmployeeBinding)
                    editViewInit(fragmentAddEditEmployeeBinding)
                    handleDeleteClick(fragmentAddEditEmployeeBinding)
                } else{
                    emplPosition = -1
                    updateEmployeeCompanyPosition(fragmentAddEditEmployeeBinding)
                    //boton save (a delete) y añadirle el listener para que añada
                    fragmentAddEditEmployeeBinding.btnSave.setText("Add Employee")
                    addViewInit(fragmentAddEditEmployeeBinding)
                }
            }

            override fun onNothingSelected(arg0: AdapterView<*>?) {
                // TODO - Custom Code
            }
        }
    }

    private fun getEmployeesCompany(fragmentAddEditEmployeeBinding:
                                    FragmentAddEditEmployeeBinding){
        println("emplPosition ----------------------------------------> ${emplPosition}")
        println("compPosition ----------------------------------------> ${compPosition}")

        var employeesAux = listOf<Employee>()
        if (emplPosition != -1 && shareViewModel.getEmployee(emplPosition).value != null){
            println("ENTRA EN EL IF: --------------------> emplPosition != -1 && " +
                    "shareViewModel.getEmployee(emplPosition).value != null")
            employeesAux = shareViewModel.getEmployee(emplPosition).value!!
            var empl: Employee
            if (employeesAux.isNotEmpty() && employeesAux.first() != null){
                empl = employeesAux.first()
                println("empl ----------------------------------------> ${empl}")
                var empIdAux = empl.companyId
                println("empIdAux ----------------------------------------> ${empIdAux}")
                var company = empIdAux?.let { shareViewModel.getCompany(it).value?.first() }
                println("company ----------------------------------------> ${company}")
            }
        }
    }

    private fun setSnipperCompanyItemSelector(spin: Spinner,
                                       fragmentAddEditEmployeeBinding:
                                       FragmentAddEditEmployeeBinding){
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
                /*
                Toast.makeText(
                    spin?.context,
                    "Selected Company: " + spinnerCompanies.get(position),
                    Toast.LENGTH_SHORT
                ).show()
                 */
                if (position != 0){
                    //Set the employees data
                    println("Selected Companie's Id is: " + spinnerCompId.get(position-1) +
                            " and emplPosition: $emplPosition")
                    compPosition = spinnerCompId.get(position-1)
                    if (emplPosition != -1){
                        shareViewModel.updateEmployeesCompId(emplPosition.toLong(),
                            compPosition.toLong())
                        updateEmployeeCompanyPosition(fragmentAddEditEmployeeBinding)

                    }
                } else{
                    compPosition = -1
                    updateEmployeeCompanyPosition(fragmentAddEditEmployeeBinding)

                }
                println("ARRAY FOR SELECTED SIZE: "+ spinnerCompanies.size)
            }

            override fun onNothingSelected(arg0: AdapterView<*>?) {
                // TODO - Custom Code
            }
        }
    }

    private fun updateSnipperCompany(spin: Spinner,
                                      fragmentAddEditEmployeeBinding:
                                      FragmentAddEditEmployeeBinding){
        var companyAux = listOf<Company>()
        if (shareViewModel.companies.value != null){
            companyAux = shareViewModel.companies.value!!
            println("Entra en updateSnipperCompany con ${shareViewModel.companies.value!!}")
        }
        setSnipperCompanies(companyAux, spin,fragmentAddEditEmployeeBinding)
    }

    private fun setSnipperCompanies(companies: List<Company>, spin: Spinner,
                                    fragmentAddEditEmployeeBinding:
                                    FragmentAddEditEmployeeBinding){
        spinnerCompanies = mutableListOf<String>("")
        spinnerCompId = mutableListOf<Int>()

        // Spinner Drop down elements
        companies?.forEach {
            if (it.id.toInt() >= 1){
                spinnerCompanies.add(it.name)
                spinnerCompId.add(it.id.toInt())
            }
        }
        setSnipperCompanyItemSelector(spin,fragmentAddEditEmployeeBinding)
    }

    private fun setEmployeesData(fragmentAddEditEmployeeBinding:
    FragmentAddEditEmployeeBinding, employeeData: List<Employee>, companyData: List<Company>){
        employeeData.forEach {
            if (it.isAdmin == false){
                fragmentAddEditEmployeeBinding.tvEmployeeCIF.setText("${it.cif.toString()}")

                fragmentAddEditEmployeeBinding.
                tvEmployeeFirstName.setText("${it.firstName.toString()}")

                fragmentAddEditEmployeeBinding.
                tvEmployeeLastName.setText("${it.lastName.toString()}")

                fragmentAddEditEmployeeBinding.
                tvEmployeeNss.setText("${it.nss.toString()}")

                fragmentAddEditEmployeeBinding.
                tvEmployeeEmail.setText("${it.email.toString()}")

                fragmentAddEditEmployeeBinding.
                tvEmployeePasswd.setText("${it.password.toString()}")

                fragmentAddEditEmployeeBinding.
                spinEmployeeCompany.setSelection(compPosition)

                println("Entra en setEmployeesData ------------------------------->" +
                        "compPosition: $compPosition")

            }else{
                resetEmployeesData(fragmentAddEditEmployeeBinding)
            }
        }

    }

    private fun resetEmployeesData(fragmentAddEditEmployeeBinding:
                                   FragmentAddEditEmployeeBinding){
        fragmentAddEditEmployeeBinding.tvEmployeeCIF.setText("")
        fragmentAddEditEmployeeBinding.tvEmployeeFirstName.setText("")
        fragmentAddEditEmployeeBinding.tvEmployeeLastName.setText("")
        fragmentAddEditEmployeeBinding.tvEmployeeNss.setText("")
        fragmentAddEditEmployeeBinding.tvEmployeeEmail.setText("")
        fragmentAddEditEmployeeBinding.tvEmployeePasswd.setText("")
        fragmentAddEditEmployeeBinding.spinEmployeeCompany.setSelection(0)

        emplPosition=-1
        compPosition=-1

    }

    private fun isUniqueElement(source: List<String>,element: String): Boolean{
        var result=true

        if (source.isNotEmpty()){
            if (source.contains(element)){
                result=false
            }
        }
        return result
    }

    private fun copyMutableList(source: MutableList<String>): MutableList<String>{
        val mutableCopy = source.toMutableList()

        return mutableCopy
    }
}