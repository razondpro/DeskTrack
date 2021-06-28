package com.wagit.desktrack.ui.login.viewmodel

import androidx.lifecycle.*
import com.wagit.desktrack.data.entities.Employee
import com.wagit.desktrack.data.repositories.EmployeeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val employeeRepository: EmployeeRepository) : ViewModel() {

    private val _user: MutableLiveData<List<Employee>> = MutableLiveData()
    val user: LiveData<List<Employee>> get() = _user

    fun logUser(email: String, pw: String){
        viewModelScope.launch {
            _user.value = employeeRepository.getUserByEmailAndPw(email,pw)
        }
    }
}