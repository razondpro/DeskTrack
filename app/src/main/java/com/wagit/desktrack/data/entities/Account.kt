package com.wagit.desktrack.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

import java.io.Serializable

@Entity(
    tableName = "accounts",
    indices = [Index(value = ["email"], unique = true)]
)
data class Account(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val email: String,
    val password: String,
    @ColumnInfo(name = "is_admin")
    val isAdmin: Boolean,
    @ColumnInfo(name = "is_deleted")
    val isDeleted: Boolean
): Serializable
