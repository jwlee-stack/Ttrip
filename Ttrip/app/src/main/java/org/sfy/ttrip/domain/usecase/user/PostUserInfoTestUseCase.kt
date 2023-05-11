package org.sfy.ttrip.domain.usecase.user

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.sfy.ttrip.data.remote.datasorce.user.UserInfoTestRequest
import org.sfy.ttrip.domain.entity.user.UserTest
import org.sfy.ttrip.domain.repository.user.UserRepository
import javax.inject.Inject

class PostUserInfoTestUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(userTest: UserTest) = withContext(Dispatchers.IO) {
        userRepository.postUserInfoTest(
            UserInfoTestRequest(
                userTest.preferCheapHotelThanComfort,
                userTest.preferCheapTraffic,
                userTest.preferDirectFlight,
                userTest.preferGoodFood,
                userTest.preferNatureThanCity,
                userTest.preferPersonalBudget,
                userTest.preferPlan,
                userTest.preferShoppingThanTour,
                userTest.preferTightSchedule
            )
        )
    }
}