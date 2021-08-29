package com.wagit.desktrack.data.dao


import androidx.room.*
import com.wagit.desktrack.data.entities.Employee
import com.wagit.desktrack.data.entities.relations.EmployeeWithRegistries

@Dao
abstract class EmployeeDao: BaseDao<Employee> {

    @Query("SELECT * FROM employees WHERE id = :employeeId")
    abstract suspend fun getEmployee(employeeId: Int): List<Employee>

    @Query("SELECT * FROM employees")
    abstract suspend fun getAllEmployees(): List<Employee>

    @Transaction
    @Query("SELECT * FROM employees WHERE id = :employeeId")
    abstract suspend fun getAllRegistriesdByEmployeeId(employeeId: Long): List<EmployeeWithRegistries>

    @Query("SELECT * FROM employees WHERE email = :email AND password = :pw")
    abstract suspend fun getUserByEmailAndPw(email: String, pw: String): List<Employee>

    @Query("UPDATE employees SET email = :email, password = :pw, first_name = :firstName, last_name = :lastName, company_id = :companyId, cif = :cif, nss = :nss WHERE id = :employeeId")
    abstract suspend fun updateEmployee(employeeId: Long, email: String, pw: String, firstName: String, lastName: String, companyId: Long, cif: String, nss: String)

    @Query("UPDATE employees SET company_id = :companyId WHERE id = :employeeId")
    abstract suspend fun updateEmployeesCompId(employeeId: Long, companyId: Long)

    @Query("DELETE from employees WHERE id = :employeeId")
    abstract suspend fun deleteEmployee(employeeId: Long)

    @Query("SELECT * FROM employees WHERE company_id = :companyId")
    abstract suspend fun getEmployeesByComp(companyId: Long): List<Employee>
}