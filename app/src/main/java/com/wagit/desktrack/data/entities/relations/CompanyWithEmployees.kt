package com.wagit.desktrack.data.entities.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.wagit.desktrack.data.entities.Company
import com.wagit.desktrack.data.entities.Employee

data class CompanyWithEmployees (
    @Embedded val company: Company,
    @Relation(
        parentColumn = "id",
        entityColumn = "company_id"
    )
    val employees: List<Employee>

)
