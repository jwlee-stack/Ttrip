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
import org.sfy.ttrip.common.util.DeclarationDialog
import org.sfy.ttrip.common.util.DeclarationDialogListener
import org.sfy.ttrip.common.util.EvaluateUserDialog
import org.sfy.ttrip.common.util.EvaluateUserDialogListener
import org.sfy.ttrip.data.remote.service.FirebaseService
import org.sfy.ttrip.databinding.ActivityMainBinding
import org.sfy.ttrip.presentation.init.UserInfoViewModel

@AndroidEntryPoint
class MainActivity : AppCompatActivity(),
    EvaluateUserDialogListener,
    DeclarationDialogListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var navHostFragment: NavHostFragment
    private val userViewModel by viewModels<UserInfoViewModel>()
    private lateinit var nickname: String
    private lateinit var matchHistoryId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initNavigation()
        getFCMToken()

        val fragmentName = intent.getStringExtra("fragment")
        if (fragmentName != null) {
            getFCMData(fragmentName)
        }

        /*this.nickname = "nickname"
        this.matchHistoryId = "match"
        EvaluateUserDialog(this, this, "nickname!!", "matchHistoryId!!").show()*/
    }

    override fun evaluate(matchHistoryId: String, rate: Int) {
        userViewModel.postEvaluateUser(matchHistoryId, rate)
    }

    override fun openDeclaration(reportedNickname: String) {
        // 신고 다이얼로그 열기
        DeclarationDialog(this, this, reportedNickname).show()
    }

    override fun postDeclaration(reportContext: String, reportedNickname: String) {
        // 신고 api
        userViewModel.postReportUser(reportContext, reportedNickname)
    }

    override fun cancelDeclaration() {
        // 신고 취소 시 다시 평가 다이얼로그
        EvaluateUserDialog(this, this, this.nickname, this.matchHistoryId).show()
    }

    private fun getFCMData(fragment: String) {
        NEW_ALARM_FLAG = true
        when (fragment) {
            "evaluateDialog" -> {
                val nickname = intent.getStringExtra("nickName")
                val matchHistoryId = intent.getStringExtra("matchHistoryId")
                this.nickname = nickname!!
                this.matchHistoryId = matchHistoryId!!
                EvaluateUserDialog(this, this, nickname, matchHistoryId).show()
            }
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
            userViewModel.postUserFcmToken(true, result)
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