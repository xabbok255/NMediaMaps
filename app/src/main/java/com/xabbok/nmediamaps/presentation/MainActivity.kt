package com.xabbok.nmediamaps.presentation

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.xabbok.nmediamaps.R
import com.xabbok.nmediamaps.databinding.ActivityMainBinding
import com.xabbok.nmediamaps.utils.setStatusBarColor
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

        setStatusBarColor(this)

        navHostController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.objectEditFragment -> {
                    binding.bottomNav.visibility = View.GONE
                }

                else -> binding.bottomNav.visibility = View.VISIBLE

            }
        }

        binding.bottomNav.setupWithNavController(navHostController)
    }
}