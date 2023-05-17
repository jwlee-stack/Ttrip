package org.sfy.ttrip.common.util

import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

fun dateFormat(input: String): String {
    val dateTime = LocalDateTime.parse(input, DateTimeFormatter.ISO_DATE_TIME)
    val today = LocalDate.now(ZoneId.systemDefault())
    val outputDate = if (dateTime.toLocalDate() == today) {
        dateTime.format(DateTimeFormatter.ofPattern("h:mm a", Locale.ENGLISH))
    } else {
        dateTime.format(DateTimeFormatter.ofPattern("yyyy/MM/dd"))
    }
    return outputDate
}

fun detailDateFormat(input: String): String {
    val dateTime = LocalDateTime.parse(input, DateTimeFormatter.ISO_DATE_TIME)
    return dateTime.format(DateTimeFormatter.ofPattern("M/d H:mm"))
}

fun convertDateString(dateString: String): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
    val outputFormat = SimpleDateFormat("yyyy년 M월 d일", Locale.getDefault())

    val date = inputFormat.parse(dateString)
    return outputFormat.format(date)
}