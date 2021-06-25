package com.wagit.desktrack.data.entities

import androidx.room.*
import java.time.LocalDateTime

@Entity(
    tableName = "registries",
    indices = arrayOf(Index(value = ["employee_id"])),
    foreignKeys = arrayOf(ForeignKey(
        entity = Employee::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("employee_id"),
        onDelete = ForeignKey.CASCADE
    ))
)
data class Registry(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    @ColumnInfo(name = "employee_id")
    val employeeId: Long,
    @ColumnInfo(name = "started_at",defaultValue = "CURRENT_TIMESTAMP")
    val startedAt: LocalDateTime,
    @ColumnInfo(name = "ended_at")
    val endedAt: LocalDateTime?,
)