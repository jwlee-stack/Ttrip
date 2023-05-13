package org.sfy.ttrip

import android.content.Context
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.sfy.ttrip.data.remote.service.FirebaseService
import org.sfy.ttrip.databinding.ActivityMainBinding
import org.sfy.ttrip.presentation.init.UserInfoViewModel

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var navHostFragment: NavHostFragment
    private val userViewModel by viewModels<UserInfoViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initNavigation()
        getFCMToken()

        val fragmentName = intent.getStringExtra("fragment")
        val extraData = intent.getStringExtra("articleId") // 인텐트에서 추가 데이터를 가져옵니다.

        if (fragmentName != null) {
            getFCMData(fragmentName)

//            val fragmentClass = Class.forName(fragmentName).kotlin
//            val fragmentInstance = fragmentClass.createInstance() as Fragment
//
//            // 추가 데이터를 프래그먼트 인스턴스의 arguments에 저장합니다.
//            val bundle = Bundle()
//            bundle.putString("articleId", extraData)
//            fragmentInstance.arguments = bundle
//
//            supportFragmentManager.beginTransaction().apply {
//                replace(R.id.frame_container, fragmentInstance)
//                addToBackStack(null)
//                commit()
//            }
        }
    }

    private fun getFCMData(fragment: String) {
        NEW_ALARM_FLAG = true
        when (fragment) {
            "BoardFragment" -> {
                val articleId = intent.getStringExtra("articleId")
                val dDay = intent.getStringExtra("dDay")
                val bundle = Bundle()
                bundle.putString("articleId", articleId)
                bundle.putString("dDay", dDay)

                navController.navigate(R.id.boardFragment, bundle)
            }
            "ChatFragment" -> {
                val chatroomId = intent.getStringExtra("chatroomId")
                val bundle = Bundle()
                bundle.putString("chatroomId", chatroomId)

                navController.navigate(R.id.chatFragment, bundle)
            }
        }
    }

    private fun initNavigation() {
        navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment
        navController = navHostFragment.navController

        binding.bottomNavigation.apply {
            setupWithNavController(navController)
            itemIconTintList = null

            setOnNavigationItemSelectedListener { menuItem ->
                val navOptions = NavOptions.Builder().setPopUpTo(R.id.boardFragment, true).build()

                when (menuItem.itemId) {
                    R.id.boardFragment -> {
                        navController.navigate(R.id.boardFragment, null, navOptions)
                        true
                    }
                    R.id.liveFragment -> {
                        navController.navigate(R.id.liveFragment, null, navOptions)
                        true
                    }
                    R.id.chatFragment -> {
                        navController.navigate(R.id.chatFragment, null, navOptions)
                        true
                    }
                    R.id.myPageFragment -> {
                        navController.navigate(R.id.myPageFragment, null, navOptions)
                        true
                    }
                    else -> false
                }
            }
        }

        val graphInflater = navController.navInflater
        val navGraph = graphInflater.inflate(R.navigation.navigation_main)

        navController.graph = navGraph
    }

    private fun getFCMToken() {
        lifecycleScope.launch {
            val result = FirebaseService().getCurrentToken()
            ApplicationClass.preferences.fcmToken = result
            userViewModel.postUserFcmToken(true)
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

    // GPS가 켜져있는지 확인
    fun checkLocationService(): Boolean {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    companion object {
        var NEW_ALARM_FLAG = false
    }
}