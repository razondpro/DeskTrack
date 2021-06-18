package com.wagit.desktrack.DI

import android.content.Context
import com.wagit.desktrack.data.dao.UserDao
import com.wagit.desktrack.data.db.AppDatabase
import com.wagit.desktrack.data.repositories.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DBModule {

    /**
     * @return returns the Dao object to be used as dependency in Repository class.
     */
    @Provides
    fun provideUserDao (
        @ApplicationContext context: Context
    ): UserDao {
        return AppDatabase.getInstance(context).userDao()
    }

    @Provides
    fun provideUserRepository(userDao: UserDao): UserRepository {
        return UserRepository(userDao)
    }


}