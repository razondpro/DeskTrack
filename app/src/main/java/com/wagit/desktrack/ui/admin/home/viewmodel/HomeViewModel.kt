package com.wagit.desktrack.ui.admin.home.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wagit.desktrack.data.entities.Company
import com.wagit.desktrack.data.entities.Employee
import com.wagit.desktrack.data.repositories.CompanyRepository
import com.wagit.desktrack.data.repositories.EmployeeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel

class HomeViewModel @Inject constructor(
    private val employeeRepository: EmployeeRepository,
) : ViewModel() {

    private val _employee: MutableLiveData<List<Employee>> = MutableLiveData()
    val employee: LiveData<List<Employee>> get() = _employee

    private val _company: MutableLiveData<List<Company>> = MutableLiveData()
    val company: LiveData<List<Company>> get() = _company

    fun getAllEmployees(): LiveData<List<Employee>> {
        Log.d("AdminHomeViewModel","Llega al viewmodel para getAllEmployees")
        viewModelScope.launch(Dispatchers.IO) {
            _employee.postValue(employeeRepository.getAllEmployees())
        }
        return employee
    }

}