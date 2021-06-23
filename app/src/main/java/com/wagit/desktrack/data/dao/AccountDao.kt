package com.wagit.desktrack.data.dao

import androidx.room.*
import com.wagit.desktrack.data.entities.Account
import com.wagit.desktrack.data.entities.relations.AccountAndEmployee

@Dao
abstract class AccountDao: BaseDao<Account> {

    @Query("SELECT * FROM accounts WHERE id = :accountId")
    abstract suspend fun getAccount(accountId: Long): List<Account>

    @Query("SELECT * FROM accounts")
    abstract suspend fun getAllAccounts(): List<Account>

    @Query("SELECT * FROM accounts WHERE email = :email AND password = :pw")
    abstract suspend fun getAccountByEmailAndPw(email: String, pw: String): List<Account>

    @Transaction
    @Query("SELECT * FROM accounts WHERE id = :accountId")
    abstract suspend fun getAccountAndEmployeeByAccountId(accountId: Long): List<AccountAndEmployee>
}