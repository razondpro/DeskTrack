package com.wagit.desktrack.ui.helpers

import android.util.Log
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.temporal.ChronoUnit

class TimeHelper {
    companion object {
        fun timeDifference(initialTime: LocalDateTime, finalTime: LocalDateTime): LocalTime {
            Log.d("DFS","Entra en la función timeDifference(initialTime: LocalDateTime, finalTime: LocalDateTime): LocalTime")
            var start = LocalDateTime.of(1971,1,1,initialTime.hour, initialTime.minute, initialTime.second)
            var stop = LocalDateTime.of(1971,1,1,finalTime.hour,finalTime.minute,finalTime.second)

            var fromTemp = LocalDateTime.from(start)
            val years = fromTemp.until(stop, ChronoUnit.YEARS)
            fromTemp = fromTemp.plusYears(years)

            val months = fromTemp.until(stop, ChronoUnit.MONTHS)
            fromTemp = fromTemp.plusMonths(months)

            val days = fromTemp.until(stop, ChronoUnit.DAYS)
            fromTemp = fromTemp.plusDays(days)

            val hours = fromTemp.until(stop, ChronoUnit.HOURS)
            fromTemp = fromTemp.plusHours(hours)

            val minutes = fromTemp.until(stop, ChronoUnit.MINUTES)
            fromTemp = fromTemp.plusMinutes(minutes)

            val seconds = fromTemp.until(stop, ChronoUnit.SECONDS)
            fromTemp = fromTemp.plusSeconds(seconds)

            val millis = fromTemp.until(stop, ChronoUnit.MILLIS)

            return LocalTime.of(hours.toInt(),minutes.toInt(),seconds.toInt())
        }
    }



}