package org.sfy.ttrip.common.util

import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

fun dateFormat(input: String): String{
    val dateTime = LocalDateTime.parse(input, DateTimeFormatter.ISO_DATE_TIME)
    val today = LocalDateTime.now(ZoneId.systemDefault()).withHour(0).withMinute(0).withSecond(0)
    val outputDate = if (dateTime.isEqual(today)) {
        dateTime.format(DateTimeFormatter.ofPattern("h:mm a"))
    } else {
        val dateFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd")
        dateTime.format(dateFormatter)
    }
    return outputDate
}