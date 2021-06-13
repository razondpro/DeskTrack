package com.wagit.desktrack.data.entities

import androidx.room.*
import java.time.LocalDateTime

@Entity(
    tableName = "registries",
    foreignKeys = arrayOf(ForeignKey(
        entity = User::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("user_id"),
        onDelete = ForeignKey.CASCADE
    ))
)
data class Registry(
    @PrimaryKey(autoGenerate = true) val id: Long,
    @ColumnInfo(name = "user_id") val userId: Long,
    @ColumnInfo(name = "started_at") val startedAt: LocalDateTime,
    @ColumnInfo(name = "ended_at") val endedAt: LocalDateTime?,
)