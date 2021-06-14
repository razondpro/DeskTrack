package com.wagit.desktrack.data.entities.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.wagit.desktrack.data.entities.Registry
import com.wagit.desktrack.data.entities.User


data class UserAndRegistry(
    @Embedded val user: User,
    @Relation(
        parentColumn = "id",
        entityColumn = "user_id"
    )
    val registries: List<Registry>
)