package org.sfy.ttrip.domain.repository.mypage

interface MyPageRepository {

    suspend fun updateUserInfo(age: Int, gender: String, intro: String, nickname: String)

    suspend fun updatePreferences(
        preferCheapHotelThanComfort: Int,
        preferCheapTraffic: Int,
        preferDirectFlight: Int,
        preferGoodFood: Int,
        preferNatureThanCity: Int,
        preferPersonalBudget: Int,
        preferPlan: Int,
        preferShoppingThanTour: Int,
        preferTightSchedule: Int
    )
}