package com.wagit.desktrack.data.repositories

import com.wagit.desktrack.data.dao.RegistryDao
import com.wagit.desktrack.data.entities.Registry
import java.time.LocalDateTime
import javax.inject.Inject

class RegistryRepository @Inject constructor(
    private val registryDao: RegistryDao
    ): BaseRepository<Registry>() {

    /**
     * Inserts object in database
     * @param obj object to insert in database
     */
    override suspend fun insert(obj: Registry): Long {
        return registryDao.insert(obj)
    }

    /**
     * Updates object in database
     * @param obj object to update in database
     */
    override suspend fun update(obj: Registry) {
        registryDao.update(obj)
    }

    /**
     * Deletes object in database
     * @param obj object to delete in database
     */
    override suspend fun delete(obj: Registry) {
        registryDao.delete(obj)
    }

    suspend fun getTodaysRegByEmployee(empId: Long): List<Registry> {
        return registryDao.getTodaysRegByEmployee(empId)
    }

    suspend fun getRegByIdAndEmployee(regId: Long, empId: Long): List<Registry> {
        return registryDao.getRegByIdAndEmployee(regId,empId)
    }

    //Esta función obtiene todos los registros del mes actual dado el usuario
    suspend fun getAllRegistriesByEmployeeAndCurrentMonth(empId: Long): List<Registry> {
        return registryDao.getAllRegistriesByEmployeeAndCurrentMonth(empId)
    }

    suspend fun getRegistryByEmployeeAndDay(date: LocalDateTime, empId: Long): List<Registry> {
        return registryDao.getRegistryByEmployeeAndDay(date,empId)
    }

    suspend fun getAllRegistriesByEmployeeAndMonthAndYear(empId: Long, month: String,
                                                          year: String): List<Registry> {

        val aux: List<Registry> = registryDao.getAllRegistriesByEmployeeAndMonthAndYear(empId,
            month, year)
        println("REGISTRY REPO.: $aux")
        return aux
    }

    suspend fun getRegistryByEmployeeAndMonthAndYearAndHourAndMinute(empId: Long,
                                                                     month: String,
                                                                     year: String,
                                                                     hour: String,
                                                                     minute: String):
            List<Registry> {

        val aux: List<Registry> = registryDao.getRegistryByEmployeeAndMonthAndYearAndHourAndMinute(
            empId, month, year, hour, minute)
        println("REGISTRY REPO.: $aux")
        return aux
    }

    suspend fun getAllRegistriesByMonthAndYear(month: String,
                                                          year: String): List<Registry> {

        val aux: List<Registry> = registryDao.getAllRegistriesByMonthAndYear(month, year)
        println("REGISTRY REPO. in getAllRegistriesByMonthAndYear: $aux")
        return aux
    }

    suspend fun getRegByEmployeeMonthYearAndDay(empId: Long,year: String,month: String,
                                                day: String): List<Registry> {

        val aux: List<Registry> = registryDao.getRegByEmployeeMonthYearAndDay(empId, year, month,
        day)
        println("REGISTRY REPO. in getRegByEmployeeMonthYearAndDay: $aux")
        return aux
    }

    suspend fun getRegistriesByEmployeeAndMonth(empId: Long, month: String): List<Registry>{
        val result: List<Registry> = registryDao.getRegistriesByEmployeeAndMonth(empId,month)
        println("REGISTRY REPO.: $result in getRegistriesByEmployeeAndMonth")
        return result
    }

    suspend fun getAllRegistries(): List<Registry> {
        return registryDao.getAllRegistries()
    }


}