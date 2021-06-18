package com.wagit.desktrack.data.dao

import androidx.room.*
import com.wagit.desktrack.data.entities.Registry
import com.wagit.desktrack.data.entities.relations.UserAndRegistry
import kotlinx.coroutines.flow.Flow

@Dao
abstract class RegistryDao: BaseDao<Registry> {

    @Query("SELECT * FROM registries WHERE id = :regId")
    abstract fun getRegistry(regId: Long): Flow<List<Registry>>

    @Query("SELECT * FROM registries")
    abstract fun getAllRegistries(): Flow<List<Registry>>

    @Query("SELECT * FROM registries WHERE user_id = :userId")
    abstract fun getAllRegistriesdByUserId(userId: Long): Flow<List<Registry>>

    //TODO Implement paging regsitries
    //suspend fun getPagedRegistriesByUserId(userId: Long): List<Registry>
}