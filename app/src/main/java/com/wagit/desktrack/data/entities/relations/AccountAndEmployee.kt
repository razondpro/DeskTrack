package com.wagit.desktrack.data.entities.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.wagit.desktrack.data.entities.Account
import com.wagit.desktrack.data.entities.Employee

data class AccountAndEmployee(
    @Embedded val account: Account,
    @Relation(
        parentColumn = "id",
        entityColumn = "account_id"
    )
    val employee: Employee
)