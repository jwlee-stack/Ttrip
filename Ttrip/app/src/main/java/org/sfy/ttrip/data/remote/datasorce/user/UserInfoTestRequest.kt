package org.sfy.ttrip.data.remote.datasorce.user

data class UserInfoTestRequest(
    val preferCheapHotelThanComfort: Int,
    val preferCheapTraffic: Int,
    val preferDirectFlight: Int,
    val preferGoodFood: Int,
    val preferNatureThanCity: Int,
    val preferPersonalBudget: Int,
    val preferPlan: Int,
    val preferShoppingThanTour: Int,
    val preferTightSchedule: Int,
)