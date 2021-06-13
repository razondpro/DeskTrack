package com.wagit.desktrack.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.wagit.desktrack.data.entities.Registry
import com.wagit.desktrack.data.entities.User
import com.wagit.desktrack.data.helpers.DateTimeTypeConverter

@Database(
    entities = [User::class, Registry::class],
    version = AppDatabase.DB_VERSION
)
@TypeConverters(
    DateTimeTypeConverter::class
)
abstract class AppDatabase : RoomDatabase() {

    //database singleton
    companion object {
        const val DB_VERSION = 1
        const val DB_NAME = "desktrack.db"
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase{
            synchronized(this){
                return INSTANCE?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DB_NAME
                ).build().also {
                    INSTANCE = it
                }
            }
        }
    }
}