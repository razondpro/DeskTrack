package com.wagit.desktrack.data.repositories

import com.wagit.desktrack.data.dao.AccountDao
import com.wagit.desktrack.data.entities.Account
import javax.inject.Inject

class AccountRepository @Inject constructor(
    private val accountDao: AccountDao
    ): BaseRepository<Account>() {

    /**
     * Inserts object in database
     * @param obj object to insert in database
     */
    override suspend fun insert(obj: Account): Long {
        TODO("Not yet implemented")
    }

    /**
     * Updates object in database
     * @param obj object to update in database
     */
    override suspend fun update(obj: Account) {
        TODO("Not yet implemented")
    }

    /**
     * Deletes object in database
     * @param obj object to delete in database
     */
    override suspend fun delete(obj: Account) {
        TODO("Not yet implemented")
    }

    suspend fun getAccoundByEmailAndPw(email: String, pw: String): List<Account> {
        return accountDao.getAccountByEmailAndPw(email, pw)
    }
}