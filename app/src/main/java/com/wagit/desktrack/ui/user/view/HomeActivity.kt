package com.wagit.desktrack.ui.user.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.wagit.desktrack.R

class HomeActivity : AppCompatActivity() {

    private val navController by lazy {findNavController(R.id.fragmentContainerView)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val btmNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        NavigationUI.setupActionBarWithNavController(this, navController)
        val appConfig = AppBarConfiguration(
            setOf(
                R.id.homeFragment,
                R.id.calendarFragment,
                R.id.profileFragment
            )
        )

        NavigationUI.setupWithNavController(btmNav, navController)
        setupActionBarWithNavController(navController, appConfig)
    }
}