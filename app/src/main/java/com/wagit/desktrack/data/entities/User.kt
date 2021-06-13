package com.wagit.desktrack.data.entities

import androidx.room.*

@Entity(tableName = "users", indices = [Index(value = ["cif"], unique = true)])
data class User(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val cif: String,
    val name: String,
    val surname: String,
    val password: String,
    @ColumnInfo(name = "is_admin") val isAdmin: Boolean,
    @ColumnInfo(name = "is_deleted") val isDeleted: Boolean
)
