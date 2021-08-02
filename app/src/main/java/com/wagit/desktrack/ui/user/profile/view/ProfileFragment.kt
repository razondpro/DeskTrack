package com.wagit.desktrack.ui.user.profile.view

import android.widget.TextView
import androidx.fragment.app.activityViewModels
import com.wagit.desktrack.R
import com.wagit.desktrack.databinding.FragmentProfileBinding
import com.wagit.desktrack.ui.BaseFragment
import com.wagit.desktrack.ui.user.viewmodel.SharedHomeViewModel

class ProfileFragment : BaseFragment<FragmentProfileBinding>(R.layout.fragment_profile) {
    private val sharedViewModel: SharedHomeViewModel by activityViewModels()

    override fun FragmentProfileBinding.initialize() {
        println("HHOLA FROM PROILE")
        this.tvUserProfession
        //Get the user's information
        val userEmail = sharedViewModel.employee.value!!.email
        val userCIF = sharedViewModel.employee.value!!.cif
        val userNSS = sharedViewModel.employee.value!!.nss
        val userFirstName = sharedViewModel.employee.value!!.firstName
        val userLastName = sharedViewModel.employee.value!!.lastName
        val userIsAdmin = sharedViewModel.employee.value!!.isAdmin
        println("Profile: User: cif: $userCIF email: $userEmail nss: $userNSS " +
                "namer: $userFirstName $userLastName " +
                "usAdmin: $userIsAdmin ´´´´´´´´´´´´´´´´´´´´´´´´")
        setUserInfo(userEmail, userCIF!!, userNSS!!,
            userFirstName!!, userLastName!!,
            userIsAdmin!!, this.tvUserCIF,this.tvUserEmail,
            this.tvUserNSS, this.tvUserName, this.tvUserProfession)


        //Get the company's information
        val companyName = sharedViewModel.company.value!!.name
        val companyNIF = sharedViewModel.company.value!!.nif
        val companyCCC = sharedViewModel.company.value!!.ccc
        println("Profile: Company: name: $companyName nif: $companyNIF " +
                "ccc: $companyCCC ´´´´´´´´´´´´´´´´´´´´´´´´")
        setCompanyInfo(companyName,companyNIF,companyCCC.toString(),
            this.tvCompanyCCC, this.tvCompanyNIF,
            this.tvCompanyName)

    }

    private fun setUserInfo(userEmail: String,userCIF: String,userNSS: String,
                            userFirstName: String,userLastName: String,
                            userIsAdmin: Boolean, tvUserCIF: TextView, tvUserEmail: TextView,
                            tvUserNSS: TextView, tvUserName: TextView, tvUserProfession: TextView){
        tvUserName.setText("$userFirstName $userLastName")
        tvUserEmail.setText("$userEmail")
        tvUserCIF.setText("$userCIF")
        tvUserNSS.setText("$userNSS")

        if (userIsAdmin === true){
            tvUserProfession.setText("Administrator")
        } else{
            tvUserProfession.setText("Employee")
        }

    }

    private fun setCompanyInfo(companyName: String,companyNIF: String,companyCCC: String,
                               tvCompanyCCC: TextView, tvCompanyNIF: TextView,
                               tvCompanyName: TextView){
        tvCompanyName.setText("$companyName")
        tvCompanyCCC.setText("$companyCCC")
        tvCompanyNIF.setText("$companyNIF")
    }
}