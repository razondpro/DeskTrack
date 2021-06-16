package com.wagit.desktrack.data.repositories

import android.content.Context
import androidx.lifecycle.LiveData
import com.wagit.desktrack.data.entities.User

class UserRepository(val context: Context): BaseRepository<User>() {

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

    suspend fun getUserByCifAndPsw(cif: String, pw: String): List<User>{
        return getDBInstance(context).userDao().getUserByCifAndPw(cif, pw)
    }
}