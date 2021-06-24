package com.wagit.desktrack.DI

import android.content.Context
import com.wagit.desktrack.data.dao.RegistryDao
import com.wagit.desktrack.data.dao.AccountDao
import com.wagit.desktrack.data.dao.CompanyDao
import com.wagit.desktrack.data.dao.EmployeeDao
import com.wagit.desktrack.data.db.AppDatabase
import com.wagit.desktrack.data.repositories.RegistryRepository
import com.wagit.desktrack.data.repositories.AccountRepository
import com.wagit.desktrack.data.repositories.CompanyRepository
import com.wagit.desktrack.data.repositories.EmployeeRepository
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
    fun provideAccountDao (
        @ApplicationContext context: Context
    ): AccountDao {
        return AppDatabase.getInstance(context).accountDao()
    }

    /**
     * @return returns the Dao object to be used as dependency in Repository class.
     */
    @Provides
    fun provideRegistryDao (
        @ApplicationContext context: Context
    ): RegistryDao {
        return AppDatabase.getInstance(context).registryDao()
    }

    /**
     * @return returns the Dao object to be used as dependency in Repository class.
     */
    @Provides
    fun provideCompanyDao(
        @ApplicationContext context: Context
    ): CompanyDao {
        return AppDatabase.getInstance(context).companyDao()
    }

    /**
     * @return returns the Dao object to be used as dependency in Repository class.
     */
    @Provides
    fun provideEmployeeDao(
        @ApplicationContext context: Context
    ): EmployeeDao {
        return AppDatabase.getInstance(context).employeeDao()
    }

    /**
     * Provides account repository
     */
    @Provides
    fun provideAccountRepository(accountDao: AccountDao): AccountRepository {
        return AccountRepository(accountDao)
    }

    /**
     * Provides registry repository
     */
    @Provides
    fun provideRegistryRepository(registryDao: RegistryDao): RegistryRepository {
        return RegistryRepository(registryDao)
    }

    /**
     * provides company repository
     */
    @Provides
    fun provideCompanyRepository(companyDao: CompanyDao): CompanyRepository {
        return CompanyRepository(companyDao)
    }

    /**
     * Provies employee repository
     */
    @Provides
    fun provideEmployeeRepository(employeeDao: EmployeeDao): EmployeeRepository {
        return EmployeeRepository(employeeDao)
    }

}