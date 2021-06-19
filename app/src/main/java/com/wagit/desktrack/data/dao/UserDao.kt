package com.wagit.desktrack.data.dao

import androidx.room.*
import com.wagit.desktrack.data.entities.User
import com.wagit.desktrack.data.entities.relations.UserAndRegistry

@Dao
abstract class UserDao: BaseDao<User> {

    @Query("SELECT * FROM users WHERE id = :userId")
    abstract suspend fun getUser(userId: Long): List<User>

    @Query("SELEcT * FROM users")
    abstract suspend fun getAllUsers(): List<User>

    @Query("SELECT * FROM users WHERE cif = :cif AND password = :pw")
    abstract suspend fun getUserByCifAndPw(cif: String, pw: String): List<User>

    @Transaction
    @Query("SELECT * FROM users WHERE id = :userId")
    abstract suspend fun getUserAndRegistriesByUserId(userId: Long): List<UserAndRegistry>

}