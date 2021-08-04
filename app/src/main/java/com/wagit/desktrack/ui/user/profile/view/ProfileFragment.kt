package com.wagit.desktrack.ui.user.profile.view

import android.widget.TextView
import com.wagit.desktrack.ui.user.profile.viewmodel.ProfileViewModel
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.wagit.desktrack.R
import com.wagit.desktrack.databinding.FragmentProfileBinding
import com.wagit.desktrack.ui.BaseFragment
import com.wagit.desktrack.ui.user.viewmodel.SharedHomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding>(R.layout.fragment_profile) {
    private val sharedViewModel: SharedHomeViewModel by activityViewModels()
    private val profileViewModel: ProfileViewModel by viewModels()

    override fun FragmentProfileBinding.initialize() {
        println("HHOLA FROM PROILE")
        //Get the user's information
        this.sharedVM=sharedViewModel
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

        profileViewModel.company.observe(viewLifecycleOwner, Observer {
            val cId=sharedViewModel.employee.value?.companyId
            println("CID es $cId")
            val companyL = profileViewModel.getCompany(cId!!).value
            println("Company list: $companyL")
            val companyName = companyL?.first()?.name
            println("Profile: Company: name: $companyName ´´´´´´´´´´´´´´´´´´´´´´´´")
            setCompanyInfo(companyName.toString(), this.tvCompanyName)
        })
    }

    private fun setUserInfo(userEmail: String,userCIF: String,userNSS: String,
                            userFirstName: String,userLastName: String,
                            userIsAdmin: Boolean, tvUserCIF: TextView, tvUserEmail: TextView,
                            tvUserNSS: TextView, tvUserName: TextView, tvUserProfession: TextView){
        tvUserName.setText("$userFirstName $userLastName")
        tvUserEmail.setText("$userEmail")
        tvUserCIF.setText("$userCIF")
        tvUserNSS.setText("$userNSS")
        tvUserProfession.setText("Employee")
    }

    private fun setCompanyInfo(companyName: String, tvCompanyName: TextView){
        tvCompanyName.setText("$companyName")
    }

}