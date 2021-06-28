package com.wagit.desktrack.data.entities

import androidx.room.*
import java.io.Serializable

@Entity(
    tableName = "employees",
    indices = [Index(value = ["cif","nss"], unique = true)],
    foreignKeys = arrayOf(
        ForeignKey(
            entity = Company::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("company_id"),
            onDelete = ForeignKey.CASCADE
        )
    )
)
class Employee(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val email: String,
    val password: String,
    val cif: String?,
    val nss: String?,
    @ColumnInfo(name = "first_name")
    val firstName: String?,
    @ColumnInfo(name = "last_name")
    val lastName: String?,
    @ColumnInfo(name = "company_id", index = true)
    val companyId: Long?,
    @ColumnInfo(name = "is_admin", defaultValue = "false")
    val isAdmin: Boolean?,
    @ColumnInfo(name = "is_deleted", defaultValue = "false")
    val isDeleted: Boolean?
): Serializable