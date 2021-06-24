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
    override suspend fun insert(obj: Employee) {
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
}