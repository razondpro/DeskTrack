package com.wagit.desktrack.ui.admin.home

import android.app.AlertDialog
import android.util.Log
import android.view.View
import com.wagit.desktrack.R
import com.wagit.desktrack.databinding.FragmentAddEditCompanyBinding
import com.wagit.desktrack.ui.BaseFragment
import com.wagit.desktrack.ui.admin.viewmodel.SharedViewModel
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.wagit.desktrack.data.db.AppDatabase
import com.wagit.desktrack.data.entities.Company
import com.wagit.desktrack.utils.Validator
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class AddEditCompanyFragment :
    BaseFragment<FragmentAddEditCompanyBinding>(R.layout.fragment_add_edit_company){
    private val shareViewModel: SharedViewModel by activityViewModels()
    var spinnerCompanies = mutableListOf<String>("")
    var spinnerCompId = mutableListOf<Int>()
    var compPosition = -1

    private val nifLD = MutableLiveData<String>()

    private val isValidLD = MediatorLiveData<Boolean>().apply {
        this.value = false

        addSource(nifLD){ nif ->
            this.value = validate(nif)
        }
    }

    override fun FragmentAddEditCompanyBinding.initialize() {
        println("HELLO FROM AddEditCompanyFragment -----------------------------------------")
        var spin = this.spinnerCompany
        var companies = shareViewModel.getAllCompanies().value

        updateSnipperCompany(spin,this)

        shareViewModel.companies.observe(viewLifecycleOwner, Observer {
            updateSnipperCompany(spin,this)
        })

        shareViewModel.company.observe(viewLifecycleOwner, Observer {
            updateCompanyPosition(this)
        })

        btnGoBackFromCompany.setOnClickListener {
            goBack()
        }

    }

    private fun validateEditForm(fragmentAddEditCompanyBinding: FragmentAddEditCompanyBinding){
        fragmentAddEditCompanyBinding.tvCompanyNIF.doOnTextChanged { text, _, _, _ ->
            nifLD.value = text?.toString()
        }
        isValidLD.observe(this){ isValid ->
            Log.v("EditIsValid", isValid.toString())
        }
    }

    private fun validate(nif: String?): Boolean{
        return Validator.isValidNIF(nif)
    }

    private fun goBack(){
        val fragmentManager = (activity as FragmentActivity).supportFragmentManager
        fragmentManager.popBackStackImmediate()
        println("LLEGA AL GOBACK() DEL COMPANY !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!")
    }

    private fun handleSaveClick(fragmentAddEditCompanyBinding: FragmentAddEditCompanyBinding){
        fragmentAddEditCompanyBinding.btnSaveCompany.setOnClickListener {
            if (isValidLD.value as Boolean){
                if (compPosition != -1){
                    shareViewModel.updateCompany(compPosition.toLong(),
                        fragmentAddEditCompanyBinding.tvCompanyNIF.text.toString(),
                        fragmentAddEditCompanyBinding.tvCompanyCCC.text.toString().toInt(),
                        fragmentAddEditCompanyBinding.tvCompanyName.text.toString())

                    compPosition = -1
                    goBack()
                }
            }else {
                Toast.makeText(it.context, "Please, insert valid data",
                    Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun attemptEditCompany(){
        shareViewModel.company.observe(this,{
            if(it.isEmpty()){
                Toast.makeText(this.context, "Wrong credentials", Toast.LENGTH_LONG).show()
            }
        })

    }

    private fun editViewInit(fragmentAddEditCompanyBinding: FragmentAddEditCompanyBinding){
        validateEditForm(fragmentAddEditCompanyBinding)
        handleSaveClick(fragmentAddEditCompanyBinding)
        attemptEditCompany()
    }

    private fun handleAddClick(fragmentAddEditCompanyBinding: FragmentAddEditCompanyBinding){
        fragmentAddEditCompanyBinding.btnSaveCompany.setOnClickListener {
            if (isValidLD.value as Boolean){
                GlobalScope.launch {
                    val instance = this@AddEditCompanyFragment.context?.
                    let { AppDatabase.getInstance(it) }

                    val company = Company(
                        name = fragmentAddEditCompanyBinding.tvCompanyName.text.toString(),
                        nif = fragmentAddEditCompanyBinding.tvCompanyNIF.text.toString(),
                        ccc = fragmentAddEditCompanyBinding.tvCompanyCCC.text.toString().toInt(),
                        isDeleted = false,
                    )

                    val compId = instance!!.companyDao().insert(company)
                    shareViewModel.getCompany(compId).value
                    if (shareViewModel.company.value != null){
                        println("shareViewModel.company.value es ${shareViewModel.company.value}")
                    }

                }
                goBack()
            }
        }
    }

    private fun onCompanyDeleteAlertDialog(fragmentAddEditCompanyBinding: FragmentAddEditCompanyBinding) {
        //Instantiate builder variable
        val builder = AlertDialog.Builder(this@AddEditCompanyFragment.view?.context)

        // set title
        builder.setTitle("Delete Company")

        //set content area
        builder.setMessage("Do you want to delete the company ${fragmentAddEditCompanyBinding.
        tvCompanyName.text}?")

        //set positive button
        builder.setPositiveButton(
            "Yes") { dialog, id ->
            // User clicked Update Now button
            Toast.makeText(this.context, "Deleting the company",Toast.LENGTH_SHORT).show()
            //shareViewModel.deleteEmployee(emplPosition.toLong())
            shareViewModel.deleteCompany(compPosition.toLong())
            compPosition = -1
            goBack()
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

    private fun handleDeleteClick(fragmentAddEditCompanyBinding: FragmentAddEditCompanyBinding){
        fragmentAddEditCompanyBinding.btnDeleteCompany.setOnClickListener {
            if (compPosition!=-1){
                onCompanyDeleteAlertDialog(fragmentAddEditCompanyBinding)
            }
        }
    }

    private fun addViewInit(fragmentAddEditCompanyBinding: FragmentAddEditCompanyBinding){
        validateEditForm(fragmentAddEditCompanyBinding)
        handleAddClick(fragmentAddEditCompanyBinding)
        attemptEditCompany()
    }

    private fun setSnipperCompanyItemSelector(spin: Spinner,
                                              fragmentAddEditCompanyBinding:
                                              FragmentAddEditCompanyBinding){
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
                    println("Selected Companie's Id is: " + spinnerCompId.get(position-1))
                    fragmentAddEditCompanyBinding.btnSaveCompany.setText("Save")
                    compPosition = spinnerCompId.get(position-1)
                    shareViewModel.getCompany(compPosition.toLong()).value
                    updateCompanyPosition(fragmentAddEditCompanyBinding)
                    editViewInit(fragmentAddEditCompanyBinding)
                    handleDeleteClick(fragmentAddEditCompanyBinding)
                }else{
                    compPosition = -1
                    fragmentAddEditCompanyBinding.btnSaveCompany.setText("Add Employee")
                    updateCompanyPosition(fragmentAddEditCompanyBinding)
                    addViewInit(fragmentAddEditCompanyBinding)
                }
                println("ARRAY FOR SELECTED SIZE: "+ spinnerCompanies.size)
            }

            override fun onNothingSelected(arg0: AdapterView<*>?) {
                // TODO - Custom Code
            }
        }
    }

    private fun updateSnipperCompany(spin: Spinner,
                                     fragmentAddEditCompanyBinding:
                                     FragmentAddEditCompanyBinding
    ){
        var companyAux = listOf<Company>()
        if (shareViewModel.companies.value != null){
            companyAux = shareViewModel.companies.value!!
            println("Entra en updateSnipperCompany con ${shareViewModel.companies.value!!}")
        }
        setSnipperCompanies(companyAux, spin,fragmentAddEditCompanyBinding)
    }

    private fun setSnipperCompanies(companies: List<Company>, spin: Spinner,
                                    fragmentAddEditCompanyBinding:
                                    FragmentAddEditCompanyBinding){
        spinnerCompanies = mutableListOf<String>("")
        spinnerCompId = mutableListOf<Int>()

        // Spinner Drop down elements
        companies?.forEach {
            if (it.id.toInt() >= 1){
                spinnerCompanies.add(it.name)
                spinnerCompId.add(it.id.toInt())
            }
        }
        setSnipperCompanyItemSelector(spin,fragmentAddEditCompanyBinding)
    }

    private fun updateCompanyPosition(fragmentAddEditCompanyBinding: FragmentAddEditCompanyBinding){
        var companyAux = listOf<Company>()

        if (shareViewModel.company.value != null){
            companyAux = shareViewModel.company.value!!
            println("Entra en updateCompanyPosition con ${shareViewModel.company.value!!} y " +
                    "position: ${compPosition}")
        }
        if(compPosition != -1){
            setCompaniesData(fragmentAddEditCompanyBinding,companyAux)
        }else{
            resetCompaniesData(fragmentAddEditCompanyBinding)
        }
    }

    private fun setCompaniesData(fragmentAddEditCompanyBinding: FragmentAddEditCompanyBinding, companyData: List<Company>){
        companyData.forEach {
            fragmentAddEditCompanyBinding.tvCompanyCCC.setText("${it.ccc.toString()}")
            fragmentAddEditCompanyBinding.tvCompanyNIF.setText("${it.nif.toString()}")
            fragmentAddEditCompanyBinding.tvCompanyName.setText("${it.name.toString()}")

            println("Entra en setCompaniesData ------------------------------->" +
                    "compPosition: $compPosition")
        }
    }

    private fun resetCompaniesData(fragmentAddEditCompanyBinding: FragmentAddEditCompanyBinding)
    {
        fragmentAddEditCompanyBinding.tvCompanyCCC.setText("")
        fragmentAddEditCompanyBinding.tvCompanyNIF.setText("")
        fragmentAddEditCompanyBinding.tvCompanyName.setText("")

        println("Entra en resetCompaniesData ------------------------------->" +
                "compPosition: $compPosition")
    }
}





























/*
// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AddEditCompanyFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddEditCompanyFragment : Fragment() {
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_edit_company, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AddEditCompanyFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AddEditCompanyFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}

 */