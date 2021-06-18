package com.wagit.desktrack.data.repositories

abstract class BaseRepository<T>{
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