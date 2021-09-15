package com.wagit.desktrack.data.repositories

import com.wagit.desktrack.data.dao.CompanyDao
import com.wagit.desktrack.data.entities.Company
import javax.inject.Inject

class CompanyRepository @Inject constructor(
    private val companyDao: CompanyDao
    ) :BaseRepository<Company>(){
    /**
     * Inserts object in database
     * @param obj object to insert in database
     */
    override suspend fun insert(obj: Company): Long {
        TODO("Not yet implemented")
    }

    /**
     * Updates object in database
     * @param obj object to update in database
     */
    override suspend fun update(obj: Company) {
        TODO("Not yet implemented")
    }

    /**
     * Deletes object in database
     * @param obj object to delete in database
     */
    override suspend fun delete(obj: Company) {
        TODO("Not yet implemented")
    }

    suspend fun getCompany(companyId: Long): List<Company>{
        return companyDao.getCompany(companyId)
    }

    suspend fun getAllCompanies(): List<Company>{
        return companyDao.getAllCompanies()
    }

    suspend fun updateCompany(companyId: Long, nif: String, ccc: String, name: String){
        companyDao.updateCompany(companyId,nif,ccc,name)
    }

    suspend fun deleteCompany(companyId: Long){
        companyDao.deleteCompany(companyId)
    }
}