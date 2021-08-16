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
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.wagit.desktrack.utils.Validator

class AddEditEmployeeFragment :
    BaseFragment<FragmentAddEditEmployeeBinding>(R.layout.fragment_add_edit_employee){
    private val shareViewModel: SharedViewModel by activityViewModels()
    var spinnerEmployees = mutableListOf<String>("")
    var spinnerEmplId = mutableListOf<Int>()
    var emplPosition = -1

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
        println("Hello from employee fragment!!!")
        var spin = this.spinnerEmployee
        this@AddEditEmployeeFragment.view
        var employees = shareViewModel.getAllEmployees().value
        updateSnipperEmployee(spin,this)

        shareViewModel.employees.observe(viewLifecycleOwner, Observer {
            updateSnipperEmployee(spin,this)
        })

        shareViewModel.employee.observe(viewLifecycleOwner, Observer {
            updateEmployeePosition(this)
        })

        btnGoBack.setOnClickListener {
            goBack()
        }

        //No vuelve atrás al clicar sobre save?!

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
            //TODO Implement behaviors after form validation
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
            if(isValidLD.value as Boolean){
                //TODO: implementar que se edite el empleado en la DB
                if (emplPosition != -1){
                    val userP = shareViewModel.updateEmployee(emplPosition.toLong(),
                        fragmentAddEditEmployeeBinding.tvEmployeeEmail.text.toString(),
                        fragmentAddEditEmployeeBinding.tvEmployeePasswd.text.toString(),
                        fragmentAddEditEmployeeBinding.tvEmployeeFirstName.text.toString(),
                        fragmentAddEditEmployeeBinding.tvEmployeeLastName.text.toString(),
                        fragmentAddEditEmployeeBinding.tvEmployeeCompanyId.text.toString().toLong(),
                        fragmentAddEditEmployeeBinding.tvEmployeeCIF.text.toString(),
                        fragmentAddEditEmployeeBinding.tvEmployeeNss.text.toString())

                    goBack()
                }

                //updateEmployee(employeeId: Long, email: String, pw: String,
                //                               firstName: String, lastName: String, companyId: Long,
                //                               cif: String, nss: String)
            }else {
                Toast.makeText(it.context, "Please, insert valid data",
                    Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun attemptEditEmployee(){
        //TODO: implementar un observer para que vaya comprobando si los datos son correctos o no,
        // si es correcto irá atras goBack()
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

        //set negative button
        builder.setPositiveButton(
            "Yes") { dialog, id ->
            // User clicked Update Now button
            Toast.makeText(this.context, "Deleting the employee",Toast.LENGTH_SHORT).show()
            shareViewModel.deleteEmployee(emplPosition.toLong())
            goBack()
        }

        //set positive button

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
            if (emplPosition != -1){
                onEmployeeDeleteAlertDialog(fragmentAddEditEmployeeBinding)
            }
        }
    }

    private fun updateSnipperEmployee(spin: Spinner,
                                      fragmentAddEditEmployeeBinding:
                                      FragmentAddEditEmployeeBinding){
        var employeesAux = listOf<Employee>()
        if (shareViewModel.employees.value != null){
            employeesAux = shareViewModel.employees.value!!
            println("Entra en updateSnipperEmployee con ${shareViewModel.employees.value!!}")
        }
        setSnipperEmployees(employeesAux, spin,fragmentAddEditEmployeeBinding)
    }

    private fun updateEmployeePosition(fragmentAddEditEmployeeBinding:
                                       FragmentAddEditEmployeeBinding){
        var employeeAux = listOf<Employee>()
        if (shareViewModel.employee.value != null){
            employeeAux = shareViewModel.employee.value!!
            println("Entra en updateEmployeePosition con ${shareViewModel.employee.value!!} y " +
                    "position: $emplPosition")
        }
        if (emplPosition != -1){
            setEmployeesData(emplPosition,fragmentAddEditEmployeeBinding,employeeAux)
        }else{
            resetEmployeesData(fragmentAddEditEmployeeBinding,employeeAux)
        }
    }

    private fun setSnipperEmployees(employees: List<Employee>, spin: Spinner,
                                    fragmentAddEditEmployeeBinding:
                                    FragmentAddEditEmployeeBinding){
        spinnerEmployees = mutableListOf<String>("")
        spinnerEmplId = mutableListOf<Int>()

        // Spinner Drop down elements
        employees?.forEach {
            if (it.isAdmin == false){
                spinnerEmployees.add(it.firstName + " " + it.lastName)
                spinnerEmplId.add(it.id.toInt())
            }
        }
        setSnipperItemSelector(spin,fragmentAddEditEmployeeBinding)
    }

    private fun setSnipperItemSelector(spin: Spinner,
                                       fragmentAddEditEmployeeBinding:
                                       FragmentAddEditEmployeeBinding){
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
                    println("Selected Employees Id is: " + spinnerEmplId.get(position-1))
                    var employee = shareViewModel.getEmployee(emplPosition).value
                    updateEmployeePosition(fragmentAddEditEmployeeBinding)
                    editViewInit(fragmentAddEditEmployeeBinding)
                    handleDeleteClick(fragmentAddEditEmployeeBinding)
                } else{
                    emplPosition = -1
                    updateEmployeePosition(fragmentAddEditEmployeeBinding)
                }
                println("ARRAY FOR SELECTED SIZE: "+ spinnerEmployees.size)
            }

            override fun onNothingSelected(arg0: AdapterView<*>?) {
                // TODO - Custom Code
            }
        }
    }

    private fun setEmployeesData(position: Int, fragmentAddEditEmployeeBinding:
    FragmentAddEditEmployeeBinding, employeeData: List<Employee>){
        println("Hello from setEmployeesData with ${employeeData}")
        employeeData.forEach {
            println("Name & Email is: "+ it.firstName + " " + it.email)
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
                tvEmployeeCompanyId.setText("${it.companyId.toString()}")

            }else{
                resetEmployeesData(fragmentAddEditEmployeeBinding,employeeData)
            }
        }

    }

    private fun resetEmployeesData(fragmentAddEditEmployeeBinding:
    FragmentAddEditEmployeeBinding, employeeData: List<Employee>){
        println("Hello from resetEmployeesData with position: ${emplPosition}")
        employeeData.forEach {
            println("Name & Email is: "+ it.firstName + " " + it.email)
            fragmentAddEditEmployeeBinding.tvEmployeeCIF.setText("")
            fragmentAddEditEmployeeBinding.tvEmployeeFirstName.setText("")
            fragmentAddEditEmployeeBinding.tvEmployeeLastName.setText("")
            fragmentAddEditEmployeeBinding.tvEmployeeNss.setText("")
            fragmentAddEditEmployeeBinding.tvEmployeeEmail.setText("")
            fragmentAddEditEmployeeBinding.tvEmployeePasswd.setText("")
            fragmentAddEditEmployeeBinding.tvEmployeeCompanyId.setText("")
        }

    }
}








/*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AddEditEmployeeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

class AddEditEmployeeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        println("Hello from employee fragment!!!")
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_edit_employee, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AddEditEmployeeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AddEditEmployeeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}

 */