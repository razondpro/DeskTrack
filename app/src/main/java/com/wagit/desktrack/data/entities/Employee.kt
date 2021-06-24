package com.wagit.desktrack.data.entities

import androidx.room.*

@Entity(
    tableName = "employees",
    indices = [Index(value = ["cif","nss"], unique = true)],
    foreignKeys = arrayOf(
        ForeignKey(
        entity = Account::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("account_id"),
        onDelete = ForeignKey.CASCADE),
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
    val id: Long,
    val cif: String,
    val nss: String,
    @ColumnInfo(name = "first_name")val firstName: String,
    @ColumnInfo(name = "last_name")val lasName: String,
    @ColumnInfo(name = "account_id", index = true) val accountId: Long,
    @ColumnInfo(name = "company_id", index = true) val companyId: Long,
    @ColumnInfo(name = "is_deleted") val isDeleted: Boolean
)