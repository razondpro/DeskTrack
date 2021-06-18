package com.wagit.desktrack.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.wagit.desktrack.data.dao.RegistryDao
import com.wagit.desktrack.data.dao.UserDao
import com.wagit.desktrack.data.entities.Registry
import com.wagit.desktrack.data.entities.User
import com.wagit.desktrack.data.helpers.DateTimeTypeConverter
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import com.wagit.desktrack.data.helpers.PrepopulateData

@Database(
    entities = [User::class, Registry::class],
    version = AppDatabase.DB_VERSION
)
@TypeConverters(
    DateTimeTypeConverter::class
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun registryDao(): RegistryDao

    companion object {
        const val DB_VERSION = 1
        const val DB_NAME = "desktrack.db"
        @Volatile
        private var INSTANCE: AppDatabase? = null

        /**
         * Get a singleton db instance
         * @param context Application context
         * @return AppDatabase instance
         */
        fun getInstance(context: Context): AppDatabase{
            /*Multiple threads can ask for the database at the same time, ensure we only initialize
            it once by using synchronized. Only one thread may enter a synchronized block at a time.*/
            synchronized(this){
                return INSTANCE?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DB_NAME
                ).addCallback(dbCreateCallback(context))
                 .build()
                 .also {
                    INSTANCE = it
                }
            }
        }

        /**
         * Prepopulating data in db
         * Insert data on first run
         */
        private fun dbCreateCallback(context: Context) = object : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                GlobalScope.launch {
                    getInstance(context).userDao()
                        .insert(PrepopulateData.user)
                }
            }
        }
    }
}