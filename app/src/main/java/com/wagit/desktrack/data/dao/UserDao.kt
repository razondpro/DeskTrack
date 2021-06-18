package com.wagit.desktrack.data.dao

import androidx.room.*
import com.wagit.desktrack.data.entities.User
import com.wagit.desktrack.data.entities.relations.UserAndRegistry
import kotlinx.coroutines.flow.Flow

@Dao
abstract class UserDao: BaseDao<User> {

    @Query("SELECT * FROM users WHERE id = :userId")
    abstract fun getUser(userId: Long): Flow<List<User>>

    @Query("SELEcT * FROM users")
    abstract fun getAllUsers(): Flow<List<User>>

    @Query("SELECT * FROM users WHERE cif = :cif AND password = :pw")
    abstract fun getUserByCifAndPw(cif: String, pw: String): Flow<List<User>>

    @Transaction
    @Query("SELECT * FROM users WHERE id = :userId")
    abstract fun getUserAndRegistriesByUserId(userId: Long): Flow<List<UserAndRegistry>>

}