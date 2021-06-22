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

    val bottomNav by lazy {
        binding.bottomNavigationView
    }

    private val navController: NavController by lazy {
        val navHost = supportFragmentManager.findFragmentById(
            R.id.fragmentContainerView
        ) as NavHostFragment
        navHost.navController
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val user = intent.getSerializableExtra("EXTRA_USER") as User
        println(user)

        val topBarConfig = AppBarConfiguration(setOf(R.id.homeFragment, R.id.calendarFragment, R.id.profileFragment))

        setupActionBarWithNavController(navController, topBarConfig)
        bottomNav.setupWithNavController(navController)
    }
}