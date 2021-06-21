package com.wagit.desktrack.ui.view.activites

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.wagit.desktrack.R
import com.wagit.desktrack.data.entities.User
import com.wagit.desktrack.databinding.ActivityHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : BaseActivity() {

    private lateinit var binding: ActivityHomeBinding
/*
    val bottomNav by lazy {
        binding.bottomNavigationView
    }

    private val navController: NavController by lazy {
        val navHost = supportFragmentManager.findFragmentById(
            R.id.fragmentContainerView
        ) as NavHostFragment
        navHost.navController
    }
*/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val user = intent.getSerializableExtra("EXTRA_USER") as User
        println(user)
/*
        val topBarConfig = AppBarConfiguration(setOf(R.id.fHome, R.id.fCalendar, R.id.fProfile))

        setupActionBarWithNavController(navController, topBarConfig)
        bottomNav.setupWithNavController(navController)
*/

        val navView: BottomNavigationView = binding.bottomNavigationView

        //val navController = findNavController(R.id.fragmentContainerView)
         val navController: NavController by lazy {
             val navHost = supportFragmentManager.findFragmentById(
                 R.id.fragmentContainerView
             ) as NavHostFragment
             navHost.navController
         }
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.fHome, R.id.fCalendar, R.id.fProfile
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

    }
}