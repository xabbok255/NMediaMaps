package com.xabbok.nmediamaps

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.xabbok.nmediamaps.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(R.layout.activity_main) {
    private val binding: ActivityMainBinding by viewBinding(ActivityMainBinding::bind)
    private val navHostController by lazy {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_container) as NavHostFragment
        val navController = navHostFragment.navController
        navController
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.mainMap -> {

                    navHostController.navigate(R.id.action_global_mapsFragmentNav)
                    true
                }

                R.id.objectList -> {
                    navHostController.navigate(R.id.action_global_objectsFragmentNav)
                    true
                }

                R.id.settings -> {

                    true
                }

                else -> {
                    false
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)


    }
}