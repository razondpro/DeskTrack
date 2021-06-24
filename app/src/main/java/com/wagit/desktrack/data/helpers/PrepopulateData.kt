package com.wagit.desktrack.data.helpers

import com.wagit.desktrack.data.entities.Account
import com.wagit.desktrack.data.entities.Company
import com.wagit.desktrack.data.entities.Employee
import com.wagit.desktrack.data.entities.Registry

object PrepopulateData {
    var account = Account(1, "admin@admin.com","admin123",true,false)
    var user = Account(2, "user@user.com","user123",false,false)
    val company = Company(1,"Tesla","0000000",111111,false)
    val employee = Employee(1,"12345678z","222222","Johnny","Doey",user.id,company.id,false)
}