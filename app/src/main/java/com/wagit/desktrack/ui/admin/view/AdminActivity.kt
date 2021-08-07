package com.wagit.desktrack.ui.admin.view
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.wagit.desktrack.R
import com.wagit.desktrack.data.entities.Employee
import com.wagit.desktrack.databinding.ActivityAdminBinding
import com.wagit.desktrack.ui.admin.viewmodel.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint
import com.wagit.desktrack.ui.BaseActivity

@AndroidEntryPoint
class AdminActivity: BaseActivity()  {
    private val sharedViewModel: SharedViewModel by viewModels()

    private lateinit var binding: ActivityAdminBinding

    val bottomNav by lazy {
        binding.bottomAdminNavigationView
    }

    private val navController: NavController by lazy {
        val navHost = supportFragmentManager.findFragmentById(
            R.id.fragmentAdminContainerView
        ) as NavHostFragment
        navHost.navController
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        this.setupNavigation()

        //set User in sharedVM for fragments
        val user = intent.getSerializableExtra("EXTRA_USER") as Employee
        Log.v("SHARED_VIEW_USER", "${user.id}")
        sharedViewModel.setUser(user)
    }

    /**
     * Set configuration for top bar and
     * bottom navigation
     */
    fun setupNavigation(){
        val topBarConfig = AppBarConfiguration(setOf(R.id.adminFragment, R.id.adminCalendarFragment, R.id.adminProfileFragment))
        setupActionBarWithNavController(navController, topBarConfig)
        bottomNav.setupWithNavController(navController)
    }

}