package com.wagit.desktrack.data.dao

import androidx.room.*
import com.wagit.desktrack.data.entities.User
import com.wagit.desktrack.data.entities.relations.UserAndRegistry

@Dao
interface UserDao {
    @Insert
    suspend fun insert(user: User)

    @Update
    suspend fun update(user: User)

    @Delete
    suspend fun delete(user: User)

    @Query("SELECT * FROM users WHERE id = :userId")
    suspend fun getUser(userId: Long): List<User>

    @Query("SELEcT * FROM users")
    suspend fun getAllUsers(): List<User>

    @Query("SELECT * FROM users WHERE cif = :cif AND password = :pw")
    suspend fun getUserByCifAndPw(cif: String, pw: String): List<User>

    @Query("SELECT * FROM users WHERE id = :userId")
    suspend fun getUserAndRegistriesByUserId(userId: Long): List<UserAndRegistry>
}