package com.xabbok.nmediamaps

import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.xabbok.nmediamaps.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(R.layout.activity_main) {
    private val binding: ActivityMainBinding by viewBinding(ActivityMainBinding::bind)
    private val navHostController by lazy {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_container) as NavHostFragment
        val navController = navHostFragment.navController
        navController
    }

    override fun onStart() {
        super.onStart()

        /*navHostController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.mainMap -> {
                    binding.bottomNav.visibility = View.VISIBLE
                }
                R.id.objectList -> {
                    binding.bottomNav.visibility = View.GONE
                }
            }
        }*/

        binding.bottomNav.setupWithNavController(navHostController)
    }
}