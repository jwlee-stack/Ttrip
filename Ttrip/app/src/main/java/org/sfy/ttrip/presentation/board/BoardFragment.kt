package org.sfy.ttrip.presentation.board

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint
import org.sfy.ttrip.ApplicationClass
import org.sfy.ttrip.MainActivity
import org.sfy.ttrip.R
import org.sfy.ttrip.databinding.FragmentBoardBinding
import org.sfy.ttrip.presentation.base.BaseFragment
import java.util.*

@AndroidEntryPoint
class BoardFragment : BaseFragment<FragmentBoardBinding>(R.layout.fragment_board) {

    private lateinit var callback: OnBackPressedCallback
    private var waitTime = 0L

    private val viewModel by activityViewModels<BoardViewModel>()
    private val boardListAdapter by lazy {
        BoardListAdapter(this::selectBoard, requireContext())
    }

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val locationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // 권한이 부여된 경우 현재 위치 가져오기
            setLocationService()
        } else {
            // 권한이 거부된 경우 처리
            showToast("위치 권한이 필요합니다.")
        }
    }

    // 초기값으로 초기화합니다.
    private var articleId: String? = null
    private var dDay: String? = null
    private var country: String = ""

    override fun initView() {
        initObserver()
        initRecyclerView()
        initListener()

        if (!ApplicationClass.preferences.tutorials) {
            ApplicationClass.preferences.tutorials = true
            navigate(BoardFragmentDirections.actionBoardFragmentToTutorialsFragment())
        }
        (activity as MainActivity).hideBottomNavigation(false)

        requestLocationPermission()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        articleId = arguments?.getString("articleId")
        dDay = arguments?.getString("dDay")

        if (MainActivity.NEW_ALARM_FLAG && arguments != null) {
            selectBoard(articleId!!.toInt(), dDay!!.toInt())
            MainActivity.NEW_ALARM_FLAG = false
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (System.currentTimeMillis() - waitTime >= 2500) {
                    waitTime = System.currentTimeMillis()
                    showToast("뒤로가기 버튼을\n한번 더 누르면 종료됩니다.")
                } else {
                    requireActivity().finishAffinity()
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    private fun initListener() {
        binding.apply {
            ivSearchBoard.setOnClickListener {
                viewModel.getBoards(2, "", "", etSearchBoard.text.toString())
            }
            ivPostBoard.setOnClickListener {
                navigate(BoardFragmentDirections.actionBoardFragmentToPostBoardFragment())
            }
            ivSelectMode.setOnClickListener {
                if (clBoardSearchTitle.visibility == View.INVISIBLE) clBoardSearchTitle.visibility =
                    View.VISIBLE
                else clBoardSearchTitle.visibility = View.INVISIBLE
            }
            tvBoardTotal.setOnClickListener {
                clBoardSearchTitle.visibility = View.INVISIBLE
                viewModel.getBoards(0, "", "", "")
            }
            tvBoardCity.apply {
                setOnClickListener {
                    if (text == "?") {
                        if ((activity as MainActivity).checkLocationService()) {
                            requestLocationPermission()
                            showToast("위치 기반 검색입니다.")
                        } else {
                            showToast("GPS를 먼저 실행해주세요.")
                        }
                    } else {
                        showToast("$country 기준 검색 결과입니다")
                        viewModel.getBoards(1, "", this@BoardFragment.country, "")
                    }
                    viewModel.getBoards(1, "", this@BoardFragment.country, "")
                }

            }
            tvBoardDay.setOnClickListener {
                clBoardSearchTitle.visibility = View.INVISIBLE
                boardListAdapter.sorting()
                binding.rvBoardBrief.apply {
                    adapter = boardListAdapter
                    layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
                }
            }
            slBoard.setOnRefreshListener {
                viewModel.getBoards(0, "", "", "")
            }
        }
    }

    private fun initRecyclerView() {
        viewModel.getBoards(0, "", "", "")
        binding.rvBoardBrief.apply {
            adapter = boardListAdapter
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        }
    }

    private fun initObserver() {
        viewModel.boardListData.observe(viewLifecycleOwner) { response ->
            response?.let {
                if (it.isEmpty()) {
                    binding.apply {
                        rvBoardBrief.visibility = View.GONE
                        tvNoneBoard.visibility = View.VISIBLE
                    }
                } else {
                    binding.apply {
                        rvBoardBrief.visibility = View.VISIBLE
                        tvNoneBoard.visibility = View.GONE
                    }
                    boardListAdapter.setBoard(it)
                }
                binding.slBoard.isRefreshing = false
            }
        }
    }

    private fun selectBoard(boardId: Int, dDay: Int) {
        navigate(BoardFragmentDirections.actionBoardFragmentToBoardDetailFragment(boardId, dDay))
    }

    private fun setLocationService() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    val latitude = location.latitude
                    val longitude = location.longitude

                    // 위도(latitude)와 경도(longitude) 값을 사용하여 원하는 작업 수행
                    val geocoderCountry = Geocoder(requireContext(), Locale.KOREA)
                    val addressesCountry =
                        geocoderCountry.getFromLocation(latitude, longitude, 1)
                    val countryName = addressesCountry!![0].countryName?.trim()?.replace("-", "")
                    country = countryName!!
                    binding.tvBoardCity.text = country
                }
            }
        }
    }

    private fun requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // 권한이 부여되어 있지 않은 경우 권한 요청
            locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        } else {
            // 권한이 이미 부여되어 있는 경우 처리할 로직
            setLocationService()
        }
    }
}