package com.wagit.desktrack.data.dao

import androidx.room.*
import com.wagit.desktrack.data.entities.Company
import com.wagit.desktrack.data.entities.relations.CompanyWithEmployees

@Dao
abstract class CompanyDao: BaseDao<Company> {

    @Query("SELECT * FROM companies WHERE id = :companyId")
    abstract suspend fun getCompany(companyId: Long): List<Company>

    @Query("SELECT * FROM companies WHERE nif = :nif")
    abstract suspend fun getCompanyByNif(nif: Long): List<Company>

    @Query("SELECT * FROM companies")
    abstract suspend fun getAllCompanies(): List<Company>

    @Transaction
    @Query("SELECT * FROM companies WHERE id = :companyId")
    abstract suspend fun geEmployeesByCompanyId(companyId: Long): List<CompanyWithEmployees>

    @Query("UPDATE companies SET nif = :nif, ccc = :ccc, name = :name WHERE id = :companyId")
    abstract suspend fun updateCompany(companyId: Long, nif: String, ccc: String, name: String)

    @Query("DELETE FROM companies WHERE id = :companyId")
    abstract suspend fun deleteCompany(companyId: Long)
}