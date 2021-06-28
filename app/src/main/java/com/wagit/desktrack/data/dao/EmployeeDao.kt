package com.wagit.desktrack.data.dao


import androidx.room.*
import com.wagit.desktrack.data.entities.Employee
import com.wagit.desktrack.data.entities.relations.EmployeeWithRegistries

@Dao
abstract class EmployeeDao: BaseDao<Employee> {

    @Query("SELECT * FROM employees WHERE id = :employeeId")
    abstract suspend fun getEmployee(employeeId: Long): List<Employee>

    @Query("SELECT * FROM employees")
    abstract suspend fun getAllEmployees(): List<Employee>

    @Transaction
    @Query("SELECT * FROM employees WHERE id = :employeeId")
    abstract suspend fun getAllRegistriesdByEmployeeId(employeeId: Long): List<EmployeeWithRegistries>

    @Query("SELECT * FROM employees WHERE email = :email AND password = :pw")
    abstract suspend fun getUserByEmailAndPw(email: String, pw: String): List<Employee>
}