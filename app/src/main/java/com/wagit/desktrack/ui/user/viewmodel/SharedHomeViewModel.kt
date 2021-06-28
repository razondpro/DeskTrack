package com.wagit.desktrack.ui.user.viewmodel

import androidx.lifecycle.*
import com.wagit.desktrack.data.entities.Employee
import com.wagit.desktrack.data.repositories.EmployeeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class SharedHomeViewModel @Inject constructor(
    private val employeeRepository: EmployeeRepository
    ) : ViewModel() {

    private var _employee: MutableLiveData<Employee> = MutableLiveData()
    val employee: LiveData<Employee> get() = _employee

    /**
     * This function is called only once from oncreate HomeActivity
     * to set the employee (user) in the application
     */
    fun setUser(user: Employee){
        _employee.value = user
    }

}