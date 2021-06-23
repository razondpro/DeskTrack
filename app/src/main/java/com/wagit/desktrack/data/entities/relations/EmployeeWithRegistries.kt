package com.wagit.desktrack.data.entities.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.wagit.desktrack.data.entities.Registry
import com.wagit.desktrack.data.entities.Account


data class EmployeeWithRegistries(
    @Embedded val account: Account,
    @Relation(
        parentColumn = "id",
        entityColumn = "account_id"
    )
    val registries: List<Registry>
)