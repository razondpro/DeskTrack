package com.wagit.desktrack.ui.admin.profile.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wagit.desktrack.data.entities.Employee
import com.wagit.desktrack.data.repositories.EmployeeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val adminRepository: EmployeeRepository,
) : ViewModel() {

    private val _admin: MutableLiveData<List<Employee>> = MutableLiveData()
    val admin: LiveData<List<Employee>> get() = _admin

    fun updateAdmin(adminId: Long, email: String, pw: String,
                       firstName: String, lastName: String,
                       cif: String, nss: String){
        viewModelScope.launch(Dispatchers.IO) {
            adminRepository.updateAdmin(adminId, email, pw, firstName,
                lastName, cif, nss)
            _admin.postValue(adminRepository.getEmployee(adminId.toInt()))
        }
        Log.d("ProfileViewModel","Llega al viewmodel para updateAdmin con " +
                "${admin.value}")
        println("Llega al SHVM del updateAdmin con ${admin.value}")
    }

}