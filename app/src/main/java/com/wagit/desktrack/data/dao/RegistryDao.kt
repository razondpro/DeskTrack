package com.wagit.desktrack.data.dao

import androidx.room.*
import com.wagit.desktrack.data.entities.Registry
import java.time.LocalDateTime

@Dao
abstract class RegistryDao: BaseDao<Registry> {

    @Query("SELECT * FROM registries WHERE id = :regId")
    abstract suspend fun getRegistry(regId: Long): List<Registry>

    @Query("SELECT * FROM registries")
    abstract suspend fun getAllRegistries(): List<Registry>

    //Esta función obtiene todos los registros del mes actual dado el usuario
    @Query("SELECT * FROM registries WHERE employee_id = :empId and (SELECT strftime('%Y %m', 'now')) = strftime('%Y %m', started_at)")
    abstract suspend fun getAllRegistriesByEmployeeAndCurrentMonth(empId: Long): List<Registry>

    @Query("SELECT * FROM registries WHERE employee_id = :empId AND :year = strftime('%Y', started_at) AND :month = strftime('%m', started_at)")
    abstract suspend fun getAllRegistriesByEmployeeAndMonthAndYear(empId: Long, month: String, year: String): List<Registry>

    @Query("SELECT * FROM registries WHERE :year = strftime('%Y', started_at) AND :month = strftime('%m', started_at)")
    abstract suspend fun getAllRegistriesByMonthAndYear(month: String, year: String): List<Registry>

    @Query("SELECT * FROM registries WHERE employee_id = :empId AND :month = strftime('%m', started_at)")
    abstract suspend fun getRegistriesByEmployeeAndMonth(empId: Long, month: String): List<Registry>

    @Query("SELECT * FROM registries where  employee_id = :empId and date(started_at) = date(:date)")
    abstract suspend fun getRegistryByEmployeeAndDay(date: LocalDateTime, empId: Long): List<Registry>

    @Query("SELECT * FROM registries where  employee_id = :empId and date(started_at) = date()")
    abstract suspend fun getTodaysRegByEmployee(empId: Long): List<Registry>

    @Query("SELECT * FROM registries where  employee_id = :empId AND :year = strftime('%Y', started_at) AND :month = strftime('%m', started_at)")
    abstract suspend fun getRegByEmployee(empId: Long,year: String,month: String): List<Registry>

}