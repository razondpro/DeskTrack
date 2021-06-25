package com.wagit.desktrack.data.helpers

import com.wagit.desktrack.data.entities.Account
import com.wagit.desktrack.data.entities.Company
import com.wagit.desktrack.data.entities.Employee
import com.wagit.desktrack.data.entities.Registry

object PrepopulateData {
    var account = Account(email ="admin@admin.com",password = "admin123",isAdmin = true,isDeleted = false)
    var user = Account(email = "user@user.com", password = "user123", isAdmin = false, isDeleted = false)
    val company = Company(name = "Tesla", nif = "0000000", ccc = 111111, isDeleted = false)
}