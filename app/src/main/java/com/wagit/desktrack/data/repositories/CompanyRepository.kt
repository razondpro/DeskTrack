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
    override suspend fun insert(obj: Company) {
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
}