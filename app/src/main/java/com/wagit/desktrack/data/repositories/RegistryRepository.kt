package com.wagit.desktrack.data.repositories

import com.wagit.desktrack.data.dao.RegistryDao
import com.wagit.desktrack.data.entities.User
import javax.inject.Inject

class RegistryRepository @Inject constructor(
    private val registryDao: RegistryDao
    ): BaseRepository<User>() {

    /**
     * Inserts object in database
     * @param obj object to insert in database
     */
    override suspend fun insert(obj: User) {
        TODO("Not yet implemented")
    }

    /**
     * Updates object in database
     * @param obj object to update in database
     */
    override suspend fun update(obj: User) {
        TODO("Not yet implemented")
    }

    /**
     * Deletes object in database
     * @param obj object to delete in database
     */
    override suspend fun delete(obj: User) {
        TODO("Not yet implemented")
    }

}