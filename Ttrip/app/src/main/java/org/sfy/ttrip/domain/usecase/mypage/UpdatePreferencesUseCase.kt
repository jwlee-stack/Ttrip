package org.sfy.ttrip.domain.usecase.mypage

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.sfy.ttrip.domain.repository.mypage.MyPageRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UpdatePreferencesUseCase @Inject constructor(
    private val myPageRepository: MyPageRepository
) {
    suspend operator fun invoke(
        preferCheapHotelThanComfort: Int,
        preferCheapTraffic: Int,
        preferDirectFlight: Int,
        preferGoodFood: Int,
        preferNatureThanCity: Int,
        preferPersonalBudget: Int,
        preferPlan: Int,
        preferShoppingThanTour: Int,
        preferTightSchedule: Int
    ) = withContext(Dispatchers.IO) {
        myPageRepository.updatePreferences(
            preferCheapHotelThanComfort,
            preferCheapTraffic,
            preferDirectFlight,
            preferGoodFood,
            preferNatureThanCity,
            preferPersonalBudget,
            preferPlan,
            preferShoppingThanTour,
            preferTightSchedule
        )
    }
}