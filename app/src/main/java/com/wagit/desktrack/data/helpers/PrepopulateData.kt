package com.wagit.desktrack.data.helpers

import com.wagit.desktrack.data.entities.Company

object PrepopulateData {
    val company = Company(name = "Tesla", nif = "0000000", ccc = 111111, isDeleted = false)
    val companyTwo = Company(name = "Seat", nif = "1111111", ccc = 222222, isDeleted = false)
}