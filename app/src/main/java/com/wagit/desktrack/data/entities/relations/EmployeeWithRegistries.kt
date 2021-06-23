package com.wagit.desktrack.data.entities.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.wagit.desktrack.data.entities.Registry
import com.wagit.desktrack.data.entities.Employee


data class EmployeeWithRegistries(
    @Embedded val employee: Employee,
    @Relation(
        parentColumn = "id",
        entityColumn = "employee_id"
    )
    val registries: List<Registry>
)