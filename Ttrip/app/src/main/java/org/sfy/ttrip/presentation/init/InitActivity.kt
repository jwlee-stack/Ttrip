package org.sfy.ttrip.presentation.init

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import dagger.hilt.android.AndroidEntryPoint
import org.sfy.ttrip.MainActivity
import org.sfy.ttrip.R
import org.sfy.ttrip.databinding.ActivityInitBinding

@AndroidEntryPoint
class InitActivity : AppCompatActivity()  {
    private lateinit var binding: ActivityInitBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInitBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_init) as NavHostFragment
        navController = navHostFragment.navController

        val graphInflater = navController.navInflater
        val navGraph = graphInflater.inflate(R.navigation.navigation_init)

        navController.graph = navGraph
    }

    fun goToMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}