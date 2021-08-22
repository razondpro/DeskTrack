package com.wagit.desktrack.data.repositories

import com.wagit.desktrack.data.dao.EmployeeDao
import com.wagit.desktrack.data.entities.Employee
import javax.inject.Inject

class EmployeeRepository @Inject constructor(
    private val employeeDao: EmployeeDao
    ) :BaseRepository<Employee>(){
    /**
     * Inserts object in database
     * @param obj object to insert in database
     */
    override suspend fun insert(obj: Employee): Long {
        TODO("Not yet implemented")
    }

    /**
     * Updates object in database
     * @param obj object to update in database
     */
    override suspend fun update(obj: Employee) {
        TODO("Not yet implemented")
    }

    /**
     * Deletes object in database
     * @param obj object to delete in database
     */
    override suspend fun delete(obj: Employee) {
        TODO("Not yet implemented")
    }

    /**
     * Get user by email and password
     */
    suspend fun getUserByEmailAndPw(email: String, pw: String): List<Employee> {
        return employeeDao.getUserByEmailAndPw(email, pw)
    }

    suspend fun getAllEmployees(): List<Employee> {
        return employeeDao.getAllEmployees()
    }

    suspend fun getEmployee(employeeId: Int): List<Employee> {
        return employeeDao.getEmployee(employeeId)
    }

    suspend fun updateEmployee(employeeId: Long, email: String, pw: String,
                               firstName: String, lastName: String, companyId: Long,
                               cif: String, nss: String) {
        employeeDao.updateEmployee(employeeId, email, pw, firstName, lastName, companyId,
            cif, nss)
    }

    suspend fun updateEmployeesCompId(employeeId: Long, companyId: Long){
        employeeDao.updateEmployeesCompId(employeeId,companyId)
    }

    suspend fun deleteEmployee(employeeId: Long) {
        employeeDao.deleteEmployee(employeeId)
    }
}