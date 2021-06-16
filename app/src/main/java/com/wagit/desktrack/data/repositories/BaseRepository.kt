package com.wagit.desktrack.data.repositories

import android.content.Context
import com.wagit.desktrack.data.db.AppDatabase

abstract class BaseRepository<T>{

    companion object {
        fun getDBInstance(context: Context): AppDatabase{
            return AppDatabase.getInstance(context)
        }
    }

    /**
     * Inserts object in database
     * @param obj object to insert in database
     */
    abstract suspend fun insert(obj: T)

    /**
     * Updates object in database
     * @param obj object to update in database
     */
    abstract suspend fun update(obj: T)

    /**
     * Deletes object in database
     * @param obj object to delete in database
     */
    abstract suspend fun delete(obj: T)


}