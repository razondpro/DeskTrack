package com.wagit.desktrack.ui.admin.profile.view

import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.wagit.desktrack.R
import com.wagit.desktrack.data.entities.Employee
import com.wagit.desktrack.databinding.FragmentAdminProfileBinding
import com.wagit.desktrack.ui.BaseFragment
import com.wagit.desktrack.ui.admin.profile.viewmodel.ProfileViewModel
import com.wagit.desktrack.ui.admin.viewmodel.SharedViewModel
import com.wagit.desktrack.utils.Validator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment :
    BaseFragment<FragmentAdminProfileBinding>(R.layout.fragment_admin_profile) {
    private val shareViewModel: SharedViewModel by activityViewModels()
    private val profileViewModel: ProfileViewModel by viewModels()

    var employeesCIF = mutableListOf<String>()
    var employeesNSS = mutableListOf<String>()
    var adminId = -1

    private val emailLD = MutableLiveData<String>()
    private val pwLD = MutableLiveData<String>()
    private val cifLD = MutableLiveData<String>()

    private val isValidLAD = MediatorLiveData<Boolean>().apply {
        this.value = true

        addSource(emailLD) { email ->
            val pw = pwLD.value
            val cif = cifLD.value
            this.value = validate(email, pw, cif)
            println("addSource(emailLD) { email: $email -> pw: $pw; cif: $cif; this.value: " +
                    "${this.value}")
        }
        addSource(pwLD){ pw ->
            val email = emailLD.value
            val cif = cifLD.value
            this.value = validate(email, pw, cif)
            println("addSource(pwLD){ pw: $pw -> email: $email; cif: $cif; this.value: " +
                    "${this.value}")
        }
        addSource(cifLD){ cif ->
            val email = emailLD.value
            val pw = pwLD.value
            this.value = validate(email, pw, cif)
            println("addSource(cifLD){ cif: $cif -> email: $email; pw: $pw; this.value: " +
                    "${this.value}")
        }
    }

    override fun FragmentAdminProfileBinding.initialize() {
        println("Llega al profile")
        this.sharedVM = shareViewModel
        initiateAdminInfo(this)
        editViewInit(this)

        shareViewModel.employees.observe(viewLifecycleOwner, Observer {
            updateEmployeesData()
        })

        var employees = shareViewModel.getAllEmployees().value
        if (shareViewModel.user.value != null){
            var admin = profileViewModel.getEmployee(shareViewModel.user.value!!.id.toInt()).value
        }
    }

    private fun initiateAdminInfo(fragmentAdminProfileBinding: FragmentAdminProfileBinding){
        adminId = shareViewModel.user.value!!.id.toInt()
        val adminEmail = shareViewModel.user.value!!.email
        val adminCIF = shareViewModel.user.value!!.cif
        val adminNSS = shareViewModel.user.value!!.nss
        val adminFirstName = shareViewModel.user.value!!.firstName
        val adminLastName = shareViewModel.user.value!!.lastName
        val adminIsAdmin = shareViewModel.user.value!!.isAdmin
        val adminPasswd = shareViewModel.user.value!!.password

        println("Profile: Admin: cif: $adminCIF email: $adminEmail nss: $adminNSS " +
                "name: $adminFirstName $adminLastName " +
                "isAdmin: $adminIsAdmin ´´´´´´´´´´´´´´´´´´´´´´´´")

        setAdminInfo(adminEmail, adminCIF!!, adminNSS!!,
            adminFirstName!!, adminLastName!!,
            adminIsAdmin!!, adminPasswd!!, fragmentAdminProfileBinding.tvAdminCIF,
            fragmentAdminProfileBinding.tvAdminEmail, fragmentAdminProfileBinding.tvAdminNSS,
            fragmentAdminProfileBinding.tvAdminFirstName,
            fragmentAdminProfileBinding.tvAdminLastName,
            fragmentAdminProfileBinding.tvAdminProfession,
            fragmentAdminProfileBinding.tvAdminPassword)
    }

    private fun setAdminInfo(adminEmail: String, adminCIF: String, adminNSS: String,
                             adminFirstName: String, adminLastName: String,
                             adminIsAdmin: Boolean, adminPassword: String,
                             tvAdminCIF: TextView, tvAdminEmail: TextView,
                             tvAdminNSS: TextView, tvAdminFirstName: TextView,
                             tvAdminLastName: TextView,
                             tvAdminProfession: TextView, tvAdminPassword: TextView){

        tvAdminFirstName.setText("$adminFirstName")
        tvAdminLastName.setText("$adminLastName")
        tvAdminEmail.setText("$adminEmail")
        tvAdminCIF.setText("$adminCIF")
        tvAdminNSS.setText("$adminNSS")
        tvAdminPassword.setText("$adminPassword")
        tvAdminProfession.setText("Admin")
    }

    private fun initializeValidationData(fragmentAdminProfileBinding: FragmentAdminProfileBinding){
        cifLD.value = fragmentAdminProfileBinding.tvAdminCIF.text.toString()
        pwLD.value = fragmentAdminProfileBinding.tvAdminPassword.text.toString()
        emailLD.value = fragmentAdminProfileBinding.tvAdminEmail.text.toString()
    }

    private fun validateEditForm(fragmentAdminProfileBinding: FragmentAdminProfileBinding){
        fragmentAdminProfileBinding.tvAdminCIF.doOnTextChanged { text, _, _, _ ->
            cifLD.value = text?.toString()
        }
        fragmentAdminProfileBinding.tvAdminPassword.doOnTextChanged { text, _, _, _ ->
            pwLD.value = text?.toString()
        }
        fragmentAdminProfileBinding.tvAdminEmail.doOnTextChanged { text, _, _, _ ->
            emailLD.value = text?.toString()
        }
        isValidLAD.observe(this){ isValid ->
            Log.v("EditIsValidAdmin", isValid.toString())
        }
    }

    private fun validate(email: String?, pw: String?, cif: String?): Boolean{
        return Validator.isValidEmail(email) && !pw.isNullOrBlank() && pw.length >=6 &&
                Validator.isValidCIF(cif)
    }

    private fun handleSaveClick(fragmentAdminProfileBinding: FragmentAdminProfileBinding){
        fragmentAdminProfileBinding.btnSaveAdmin.setOnClickListener {
            var empNSS = mutableListOf<String>("")
            var empCIF = mutableListOf<String>("")
            var isUniqueCIF = true
            var isUniqueNSS = true

            if (employeesCIF.isNotEmpty() && employeesNSS.isNotEmpty() && adminId != -1){
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

            if (empCIF.isNotEmpty() && adminId != -1){
                println("empCIF.size ------------------------------------------- " +
                        "${empCIF.size}")

                println("adminId: " +
                        "$adminId ----------------------------------------------")

                println("empCIF.get(emplPosition) ---------- ${empCIF.get(adminId-1)}")
                println("Removed: ${empCIF.removeAt(adminId-1)} ------------------------")
                println("Employee CIF TV: " +
                        "${fragmentAdminProfileBinding.tvAdminCIF.text.toString()}")
                empCIF.forEach{
                    println("Employee CIF (empCIF): ${it} ---------------------------")
                }
                isUniqueCIF = isUniqueElement(empCIF,
                    fragmentAdminProfileBinding.tvAdminCIF.text.toString())
                println("isUniqueCIF is $isUniqueCIF ---------")

            }

            if (empNSS.isNotEmpty() && adminId != -1){
                println("empNSS.size ------------------------------------------- " +
                        "${empNSS.size}")

                println("adminId: " +
                        "$adminId ----------------------------------------------")
                println("empNSS.get(adminId) ---------- ${empNSS.get(adminId-1)}")
                println("Removed: ${empNSS.removeAt(adminId-1)} ------------------------")
                println("Employee NSS TV: " +
                        "${fragmentAdminProfileBinding.tvAdminNSS.text.toString()}")
                empNSS.forEach{
                    println("Employee NSS (empNSS): ${it} ---------------------------")
                }
                isUniqueNSS = isUniqueElement(empNSS,
                    fragmentAdminProfileBinding.tvAdminNSS.text.toString())
                println("isUniqueNSS is $isUniqueNSS ---------")

            }

            if (employeesCIF.isNotEmpty() && employeesNSS.isNotEmpty()){
                employeesCIF.forEach {
                    println("Admins CIF after remove: ${it} ---------------------------")
                }
                employeesNSS.forEach {
                    println("Admins NSS after remove: ${it} ---------------------------")
                }

            }
            val bbb = isValidLAD.value as Boolean
            println("isValidLAD.value as Boolean $bbb ------------------------------------------")
            if(isValidLAD.value as Boolean && isUniqueCIF && isUniqueNSS){
                profileViewModel.updateAdmin(shareViewModel.user.value!!.id,
                    fragmentAdminProfileBinding.tvAdminEmail.text.toString(),
                    fragmentAdminProfileBinding.tvAdminPassword.text.toString(),
                    fragmentAdminProfileBinding.tvAdminFirstName.text.toString(),
                    fragmentAdminProfileBinding.tvAdminLastName.text.toString(),
                    fragmentAdminProfileBinding.tvAdminCIF.text.toString(),
                    fragmentAdminProfileBinding.tvAdminNSS.text.toString()
                )

                println("Todo correcto!!!")
                Toast.makeText(it.context,
                    "Data updated", Toast.LENGTH_SHORT).show()
                //Hacer un getEmployee
            }else {
                Toast.makeText(it.context,
                    "Please, insert valid data", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun attemptEditAdmin(fragmentAdminProfileBinding: FragmentAdminProfileBinding){
        profileViewModel.admin.observe(this,{
            if(it.isEmpty()){
                Toast.makeText(this.context, "Wrong credentials", Toast.LENGTH_LONG).show()
            }
            else{
                updateAdminInfo(fragmentAdminProfileBinding)
            }
        })
    }

    private fun editViewInit(fragmentAdminProfileBinding: FragmentAdminProfileBinding){
        initializeValidationData(fragmentAdminProfileBinding)
        validateEditForm(fragmentAdminProfileBinding)
        handleSaveClick(fragmentAdminProfileBinding)
        attemptEditAdmin(fragmentAdminProfileBinding)
    }

    private fun updateAdminInfo(fragmentAdminProfileBinding: FragmentAdminProfileBinding){
        if (profileViewModel.admin.value != null){
            adminId = profileViewModel.admin.value!!.first().id.toInt()
            val adminEmail = profileViewModel.admin.value!!.first().email
            val adminCIF = profileViewModel.admin.value!!.first().cif
            val adminNSS = profileViewModel.admin.value!!.first().nss
            val adminFirstName = profileViewModel.admin.value!!.first().firstName
            val adminLastName = profileViewModel.admin.value!!.first().lastName
            val adminIsAdmin = profileViewModel.admin.value!!.first().isAdmin
            val adminPasswd = profileViewModel.admin.value!!.first().password

            println("Profile: Admin: cif: $adminCIF email: $adminEmail nss: $adminNSS " +
                    "name: $adminFirstName $adminLastName " +
                    "isAdmin: $adminIsAdmin ´´´´´´´´´´´´´´´´´´´´´´´´")

            setAdminInfo(adminEmail, adminCIF!!, adminNSS!!,
                adminFirstName!!, adminLastName!!,
                adminIsAdmin!!, adminPasswd!!, fragmentAdminProfileBinding.tvAdminCIF,
                fragmentAdminProfileBinding.tvAdminEmail, fragmentAdminProfileBinding.tvAdminNSS,
                fragmentAdminProfileBinding.tvAdminFirstName,
                fragmentAdminProfileBinding.tvAdminLastName,
                fragmentAdminProfileBinding.tvAdminProfession,
                fragmentAdminProfileBinding.tvAdminPassword)
        }

    }

    private fun updateEmployeesData(){
        var employeesAux = listOf<Employee>()
        if (shareViewModel.employees.value != null){
            employeesAux = shareViewModel.employees.value!!

            employeesCIF = mutableListOf<String>()
            employeesNSS = mutableListOf<String>()

            employeesAux.forEach {
                employeesCIF.add(it.cif.toString())
                employeesNSS.add(it.nss.toString())
            }

            if (employeesCIF.isNotEmpty()){
                employeesCIF.forEach {
                    println("Employees CIF in updateEmployeesData:" +
                            " ${it} ---------------------------")
                }
            }

            if (employeesNSS.isNotEmpty()){
                employeesNSS.forEach {
                    println("Employees NSS in updateEmployeesData:" +
                            " ${it} ---------------------------")
                }
            }


            println("Entra en updateEmployeesData con ${shareViewModel.employees.value!!}")
        }
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