package com.wagit.desktrack.data.dao

import androidx.room.*
import com.wagit.desktrack.data.entities.Registry
import com.wagit.desktrack.data.entities.relations.UserAndRegistry

@Dao
interface RegistryDao {
    @Insert
    suspend fun insert(reg: Registry)

    @Update
    suspend fun update(reg: Registry)

    @Delete
    suspend fun delete(reg: Registry)

    @Query("SELECT * FROM registries WHERE id = :regId")
    suspend fun getRegistry(regId: Long): List<Registry>

    @Query("SELECT * FROM registries")
    suspend fun getAllRegistries(): List<Registry>

    @Query("SELECT * FROM registries WHERE user_id = :userId")
    suspend fun getAllRegistriesdByUserId(userId: Long): List<Registry>

    //TODO Implement paging regsitries
    //suspend fun getPagedRegistriesByUserId(userId: Long): List<Registry>
}