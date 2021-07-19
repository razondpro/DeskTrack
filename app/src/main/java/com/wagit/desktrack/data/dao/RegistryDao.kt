package com.wagit.desktrack.data.dao

import androidx.room.*
import com.wagit.desktrack.data.entities.Registry
import java.sql.Timestamp
import java.time.LocalDateTime
import java.time.Month

@Dao
abstract class RegistryDao: BaseDao<Registry> {

    @Query("SELECT * FROM registries WHERE id = :regId")
    abstract suspend fun getRegistry(regId: Long): List<Registry>

    @Query("SELECT * FROM registries")
    abstract suspend fun getAllRegistries(): List<Registry>

    @Query("SELECT * FROM registries WHERE (SELECT strftime('%Y %m', 'now')) = strftime('%Y %m', started_at)")
    abstract suspend fun getAllRegistriesByCurrentMonth(): List<Registry>

    @Query("SELECT * FROM registries WHERE :year = strftime('%Y', started_at) AND :month = strftime('%m', started_at)")
    abstract suspend fun getAllRegistriesByMonthAndYear(month: Int, year: Int): List<Registry>

    @Query("SELECT * FROM registries where  employee_id = :empId and date(started_at) = date(:date)")
    abstract suspend fun getRegistryByEmployeeAndDay(date: LocalDateTime, empId: Long): List<Registry>

    @Query("SELECT * FROM registries where  employee_id = :empId and date(started_at) = date()")
    abstract suspend fun getTodaysRegByEmployee(empId: Long): List<Registry>

}