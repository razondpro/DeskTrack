package com.wagit.desktrack.data.dao

import androidx.room.*
import com.wagit.desktrack.data.entities.Registry
import com.wagit.desktrack.data.entities.relations.UserAndRegistry

@Dao
abstract class RegistryDao: BaseDao<Registry> {

    @Query("SELECT * FROM registries WHERE id = :regId")
    abstract suspend fun getRegistry(regId: Long): List<Registry>

    @Query("SELECT * FROM registries")
    abstract suspend fun getAllRegistries(): List<Registry>

    @Query("SELECT * FROM registries WHERE user_id = :userId")
    abstract suspend fun getAllRegistriesdByUserId(userId: Long): List<Registry>

    //TODO Implement paging regsitries
    //suspend fun getPagedRegistriesByUserId(userId: Long): List<Registry>
}