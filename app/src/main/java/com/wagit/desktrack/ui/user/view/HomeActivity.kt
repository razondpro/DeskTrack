package com.wagit.desktrack.ui.user.view

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.wagit.desktrack.R
import com.wagit.desktrack.data.entities.Account
import com.wagit.desktrack.databinding.ActivityHomeBinding
import com.wagit.desktrack.ui.BaseActivity
import com.wagit.desktrack.ui.user.viewmodel.SharedHomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : BaseActivity() {

    private val sharedViewModel: SharedHomeViewModel by viewModels()

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

        this.setupNavigation()

        //set Account in sharedVM for fragments
        val account = intent.getSerializableExtra("EXTRA_ACCOUNT") as Account
        //set employee associated to the account from database
        Log.v("SHARED_VIEWM", "${account.id}")
        sharedViewModel.setEmployee(account.id)
    }

    /**
     * Set configuration for top bar and
     * bottom navigation
     */
    fun setupNavigation(){
        val topBarConfig = AppBarConfiguration(setOf(R.id.homeFragment, R.id.calendarFragment, R.id.profileFragment))
        setupActionBarWithNavController(navController, topBarConfig)
        bottomNav.setupWithNavController(navController)
    }
}