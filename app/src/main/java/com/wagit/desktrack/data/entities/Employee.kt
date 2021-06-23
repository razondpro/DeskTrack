package com.wagit.desktrack.data.entities

import androidx.room.*

@Entity(
    tableName = "employee",
    indices = [Index(value = ["cif"], unique = true),
        Index(value = ["nss"], unique = true)],
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
    @ColumnInfo(name = "account_id") val userId: Long,
    @ColumnInfo(name = "company_id") val companyId: Long,
    @ColumnInfo(name = "is_deleted") val isDeleted: Boolean
)