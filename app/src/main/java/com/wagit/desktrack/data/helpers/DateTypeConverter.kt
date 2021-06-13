package com.wagit.desktrack.data.helpers

import androidx.room.TypeConverter
import java.time.LocalDateTime


class DateTimeTypeConverter {
    /**
     * converts String to DateTime
     */
    @TypeConverter
    fun fromTimestamp(value: String?): LocalDateTime? {
        return value?.let {  LocalDateTime.parse(it) }
    }

    /**
     * converts DateTime to String
     */
    @TypeConverter
    fun dateToTimestamp(date: LocalDateTime?): String {
        return date.toString()
    }
}