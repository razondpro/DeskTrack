package com.wagit.desktrack.ui.admin.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.wagit.desktrack.data.entities.Company
import com.wagit.desktrack.data.entities.Employee
import com.wagit.desktrack.data.repositories.CompanyRepository
import com.wagit.desktrack.data.repositories.EmployeeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class SharedViewModel  @Inject constructor(
    private val employeeRepository: EmployeeRepository,
    private val companyRepository: CompanyRepository,
) : ViewModel() {

    private var _user: MutableLiveData<Employee> = MutableLiveData()
    val user: LiveData<Employee> get() = _user

    private val _companies: MutableLiveData<List<Company>> = MutableLiveData()
    val companies: LiveData<List<Company>> get() = _companies

    private val _company: MutableLiveData<List<Company>> = MutableLiveData()
    val company: LiveData<List<Company>> get() = _company

    private val _employee: MutableLiveData<List<Employee>> = MutableLiveData()
    val employee: LiveData<List<Employee>> get() = _employee

    private val _employees: MutableLiveData<List<Employee>> = MutableLiveData()
    val employees: LiveData<List<Employee>> get() = _employees

    fun getAllEmployees(): LiveData<List<Employee>> {
        viewModelScope.launch(Dispatchers.IO) {
            _employees.postValue(employeeRepository.getAllEmployees())
        }
        Log.d("AdminHomeViewModel",
            "Llega al viewmodel para getAllEmployees con ${employees.value?.first()}")
        println("Llega al SHVM del getAll con ${employees.value?.first()}")
        return employees
    }

    fun getEmployee(employeeId: Int): LiveData<List<Employee>> {
        viewModelScope.launch(Dispatchers.IO) {
            _employee.postValue(employeeRepository.getEmployee(employeeId))
        }
        Log.d("AdminHomeViewModel","Llega al viewmodel para getEmployee con " +
                "${employee.value}")
        println("Llega al SHVM del get con ${employee.value}")
        return employee
    }

    fun updateEmployee(employeeId: Long, email: String, pw: String,
                       firstName: String, lastName: String, companyId: Long,
                       cif: String, nss: String){
        viewModelScope.launch(Dispatchers.IO) {
            employeeRepository.updateEmployee(employeeId, email, pw, firstName,
                lastName, companyId, cif, nss)
            _employee.postValue(employeeRepository.getEmployee(employeeId.toInt()))
        }
        Log.d("AdminHomeViewModel","Llega al viewmodel para updateEmployee con " +
                "${employee.value}")
        println("Llega al SHVM del updateEmployee con ${employee.value}")
    }

    fun updateEmployeesCompId(employeeId: Long, companyId: Long){
        viewModelScope.launch(Dispatchers.IO) {
            employeeRepository.updateEmployeesCompId(employeeId, companyId)
            _employee.postValue(employeeRepository.getEmployee(employeeId.toInt()))
        }
        Log.d("AdminHomeViewModel","Llega al viewmodel para updateEmployeesCompId con " +
                "${employee.value}")
        println("Llega al SHVM del updateEmployeesCompId con ${employee.value}")
    }

    fun deleteEmployee(employeeId: Long){
        viewModelScope.launch(Dispatchers.IO) {
            employeeRepository.deleteEmployee(employeeId)
        }
        Log.d("AdminHomeViewModel","Llega al viewmodel para deleteEmployee con " +
                "${employee.value}")
        println("Llega al SHVM del deleteEmployee con ${employee.value}")
    }

    fun getAllCompanies(): LiveData<List<Company>>{
        Log.d("AdminHomeViewModel","Llega al viewmodel para getAllCompanies")
        viewModelScope.launch(Dispatchers.IO) {
            _companies.postValue(companyRepository.getAllCompanies())
        }
        Log.d("ProfileViewModel","Esto es el company ${companies.value?.first()?.name}")
        return companies
    }

    fun getCompany(companyId: Long): LiveData<List<Company>>{
        Log.d("AdminHomeViewModel","Llega al viewmodel para getAllCompanies")
        viewModelScope.launch(Dispatchers.IO) {
            _company.postValue(companyRepository.getCompany(companyId))
        }
        Log.d("ProfileViewModel","Esto es el company ${company.value?.first()?.name}")
        return company
    }

    fun setUser(user: Employee){
        _user.value = user
    }

}