package com.wagit.desktrack.data.dao

import androidx.room.*
import com.wagit.desktrack.data.entities.Registry
import java.sql.Timestamp
import java.time.LocalDateTime

@Dao
abstract class RegistryDao: BaseDao<Registry> {

    @Query("SELECT * FROM registries WHERE id = :regId")
    abstract suspend fun getRegistry(regId: Long): List<Registry>

    @Query("SELECT * FROM registries")
    abstract suspend fun getAllRegistries(): List<Registry>

    //@Query("SELECT * FROM registries WHERE started_at = ")
    //abstract suspend fun getRegistryByDate(date: LocalDateTime): List<Registry>

    //abstract suspend fun getRegistryByDateAndUser(date: Timestamp, userId: Long): List<Registry>

    //@Query("SELECT * FROM registries where  employee_id = :empId and date(started_at) = date()")
    @Query("SELECT * FROM registries where  employee_id = :empId and date(started_at) = date()")
    abstract suspend fun getTodaysRegByEmployee(empId: Long): List<Registry>

    //TODO Implement paging regsitries
}