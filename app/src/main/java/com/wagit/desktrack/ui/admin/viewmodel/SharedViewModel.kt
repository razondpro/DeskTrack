package com.wagit.desktrack.ui.admin.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.wagit.desktrack.data.entities.Company
import com.wagit.desktrack.data.entities.Employee
import com.wagit.desktrack.data.entities.Registry
import com.wagit.desktrack.data.repositories.CompanyRepository
import com.wagit.desktrack.data.repositories.EmployeeRepository
import com.wagit.desktrack.data.repositories.RegistryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class SharedViewModel  @Inject constructor(
    private val employeeRepository: EmployeeRepository,
    private val companyRepository: CompanyRepository,
    private val registryRepository: RegistryRepository,
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

    private val _monthRegistry: MutableLiveData<List<Registry>> = MutableLiveData()
    val monthRegistry: LiveData<List<Registry>> get() = _monthRegistry

    private val _registry: MutableLiveData<List<Registry>> = MutableLiveData()
    val registry: LiveData<List<Registry>> get() = _registry

    private val _deleteRegistry: MutableLiveData<List<Registry>> = MutableLiveData()
    val deleteRegistry: LiveData<List<Registry>> get() = _deleteRegistry


    fun getAllEmployees(): LiveData<List<Employee>> {
        viewModelScope.launch(Dispatchers.IO) {
            _employees.postValue(employeeRepository.getAllEmployees())
        }
        Log.d("AdminHomeViewModel",
            "Llega al viewmodel para getAllEmployees con ${employees.value?.first()}")
        println("Llega al SHVM del getAll con ${employees.value?.first()}")
        return employees
    }

    fun getEmployeesByComp(companyId: Long): LiveData<List<Employee>> {
        viewModelScope.launch(Dispatchers.IO) {
            _employees.postValue(employeeRepository.getEmployeesByComp(companyId))
        }
        Log.d("AdminHomeViewModel",
            "Llega al viewmodel para getEmployeesByComp con ${employees.value?.first()}")
        println("Llega al SHVM del getEmployeesByComp con ${employees.value?.first()}")
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
        if(!companies.value.isNullOrEmpty()){
            if(companies.value != null && companies.value!!.first() != null){
                Log.d("AdminHomeViewModel","Esto es el " +
                    "company ${companies.value?.first()?.name}")
            }
        }
        return companies
    }

    fun getCompany(companyId: Long): LiveData<List<Company>>{
        Log.d("AdminHomeViewModel","Llega al viewmodel para getCompany")
        viewModelScope.launch(Dispatchers.IO) {
            _company.postValue(companyRepository.getCompany(companyId))
        }
        if (!company.value.isNullOrEmpty()){
            if (company.value != null && company.value!!.first() != null){
                Log.d("AdminHomeViewModel","Esto es el " +
                    "company ${company.value?.first()?.name}")
            }
        }
        return company
    }

    fun setUser(user: Employee){
        _user.value = user
    }

    fun updateCompany(companyId: Long, nif: String, ccc: String, name: String){
        Log.d("AdminHomeViewModel","Llega al viewmodel para updateCompany")
        viewModelScope.launch(Dispatchers.IO) {
            companyRepository.updateCompany(companyId,nif,ccc,name)
            _company.postValue(companyRepository.getCompany(companyId))
        }
        println("Llega al SHVM del updateCompany con ${company.value}")
    }

    fun deleteCompany(companyId: Long){
        Log.d("AdminHomeViewModel","Llega al viewmodel para deleteCompany")
        viewModelScope.launch(Dispatchers.IO) {
            companyRepository.deleteCompany(companyId)
        }
        println("Llega al SHVM del deleteCompany con ${company.value}")

    }

    fun getAllRegistriesByEmployeeAndMonthAndYear(empId: Long, month: String,
                                                  year: String): LiveData<List<Registry>>{
        Log.d("AdminHomeViewModel",
            "Llega al viewmodel para getAllRegistriesByEmployeeAndMonthAndYear")
        viewModelScope.launch(Dispatchers.IO) {
            _monthRegistry.postValue(
                registryRepository.getAllRegistriesByEmployeeAndMonthAndYear(empId,month,year))
        }
        println("Llega al SHVM del getAllRegistriesByEmployeeAndMonthAndYear con " +
                "${monthRegistry.value}")
        return monthRegistry
    }

    fun getRegByEmployeeMonthYearAndDay(empId: Long,year: String,month: String,
                                        day: String): LiveData<List<Registry>>{
        Log.d("AdminHomeViewModel",
            "Llega al viewmodel para getRegByEmployeeMonthYearAndDay")
        viewModelScope.launch(Dispatchers.IO) {
            _registry.postValue(
                registryRepository.getRegByEmployeeMonthYearAndDay(empId,year, month, day))
        }
        println("Llega al SHVM del getRegByEmployeeMonthYearAndDay con " +
                "${registry.value}")
        return registry
    }

    fun getRegByIdAndEmployee(regId: Long, empId: Long): LiveData<List<Registry>>{
        Log.d("AdminHomeViewModel",
            "Llega al viewmodel para getRegByIdAndEmployee")
        viewModelScope.launch(Dispatchers.IO) {
            _deleteRegistry.postValue(
                registryRepository.getRegByIdAndEmployee(regId,empId))
        }
        println("Llega al SHVM del getRegByIdAndEmployee con ${deleteRegistry.value}")
        return deleteRegistry
    }

    fun getRegistryByEmployeeAndMonthAndYearAndHourAndMinute(empId: Long,
                                                             month: String,
                                                             year: String,
                                                             hour: String,
                                                             minute: String):
            LiveData<List<Registry>>{
        Log.d("AdminHomeViewModel",
            "Llega al viewmodel para getRegistryByEmployeeAndMonthAndYearAndHourAndMinute")
        viewModelScope.launch(Dispatchers.IO) {
            _monthRegistry.postValue(
                registryRepository.getRegistryByEmployeeAndMonthAndYearAndHourAndMinute(
                    empId,month,year,hour,minute))
        }
        println("Llega al SHVM del getRegistryByEmployeeAndMonthAndYearAndHourAndMinute" +
                " con ${monthRegistry.value}")
        return monthRegistry
    }

    /**
     * Creates a registry in db
     */
    fun insertRegistry(registry: Registry) {
        viewModelScope.launch(Dispatchers.IO) {
            registryRepository.insert(registry)
            _registry.postValue(registryRepository.
            getAllRegistriesByEmployeeAndMonthAndYear(registry.employeeId,
                registry.startedAt.monthValue.toString(),registry.startedAt.year.toString()))
        }
    }
    /**
     * Updates a registry in db
     */
    fun updateRegistry(registry: Registry) {
        viewModelScope.launch(Dispatchers.IO) {
            registryRepository.update(registry)
            _registry.postValue(registryRepository.getRegByIdAndEmployee(registry.id,
                registry.employeeId))
        }
    }

    fun deleteRegistry(registry: Registry){
        viewModelScope.launch(Dispatchers.IO) {
            registryRepository.delete(registry)
        }
    }

}