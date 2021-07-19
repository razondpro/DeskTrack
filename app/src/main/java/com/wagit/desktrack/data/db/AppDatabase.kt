package com.wagit.desktrack.data.db

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.wagit.desktrack.data.dao.RegistryDao
import com.wagit.desktrack.data.dao.CompanyDao
import com.wagit.desktrack.data.dao.EmployeeDao
import com.wagit.desktrack.data.entities.Registry
import com.wagit.desktrack.data.entities.Company
import com.wagit.desktrack.data.entities.Employee
import com.wagit.desktrack.data.helpers.DateTimeTypeConverter
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import com.wagit.desktrack.data.helpers.PrepopulateData
import java.time.LocalDateTime

@Database(
    entities = [
        Registry::class,
        Employee::class,
        Company::class
    ],
    version = AppDatabase.DB_VERSION
)
@TypeConverters(
    DateTimeTypeConverter::class
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun registryDao(): RegistryDao
    abstract fun employeeDao(): EmployeeDao
    abstract fun companyDao(): CompanyDao

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
                    val instance = getInstance(context)
                    val comId = instance.companyDao().insert(PrepopulateData.company)
                    val employee = Employee(
                        email = "user@user.com",
                        password = "user123",
                        cif = "12345678z",
                        nss = "222222",
                        firstName = "Johnny",
                        lastName = "Doey",
                        companyId = comId,
                        isDeleted = false,
                        isAdmin = false
                    )
                    val empId = instance.employeeDao().insert(employee)
                    /*
                    instance.registryDao().insert(
                        Registry(
                            employeeId = empId,
                            startedAt = LocalDateTime.now(),
                            endedAt = null)
                    )

                     */
                }
            }
        }
    }
}