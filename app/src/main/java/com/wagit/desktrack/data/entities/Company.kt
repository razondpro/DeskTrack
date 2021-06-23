package com.wagit.desktrack.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey


@Entity(
    tableName = "companies",
    indices = [Index(value = ["nif"], unique = true)],
)
data class Company(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val name: String,
    val nif: String,
    val ccc: Int,
    @ColumnInfo(name = "is_deleted") val isDeleted: Boolean
)
