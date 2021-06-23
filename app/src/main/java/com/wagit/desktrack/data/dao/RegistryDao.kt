package com.wagit.desktrack.data.dao

import androidx.room.*
import com.wagit.desktrack.data.entities.Registry

@Dao
abstract class RegistryDao: BaseDao<Registry> {

    @Query("SELECT * FROM registries WHERE id = :regId")
    abstract suspend fun getRegistry(regId: Long): List<Registry>

    @Query("SELECT * FROM registries")
    abstract suspend fun getAllRegistries(): List<Registry>

    //TODO Implement paging regsitries
}