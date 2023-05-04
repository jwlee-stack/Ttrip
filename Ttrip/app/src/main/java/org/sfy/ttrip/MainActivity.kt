package org.sfy.ttrip

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.sfy.ttrip.data.remote.service.FirebaseService
import org.sfy.ttrip.databinding.ActivityMainBinding

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var navHostFragment: NavHostFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initNavigation()
        getFCMToken()
    }

    private fun initNavigation() {
        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment
        navController = navHostFragment.navController

        val graphInflater = navController.navInflater
        val navGraph = graphInflater.inflate(R.navigation.navigation_main)

        binding.bottomNavigation.apply {
            setupWithNavController(navController)
            itemIconTintList = null
        }
        navController.graph = navGraph
    }

    private fun getFCMToken() {
        lifecycleScope.launch {
            val result = FirebaseService().getCurrentToken()
            ApplicationClass.preferences.fcmToken = result
            Log.d("fcmToken", "getFCMToken: $result")
        }
    }

    fun hideBottomNavigation(state: Boolean) {
        when (state) {
            true -> {
                binding.bottomNavigation.visibility = View.GONE
            }
            else -> {
                binding.bottomNavigation.visibility = View.VISIBLE
            }
        }
    }
}