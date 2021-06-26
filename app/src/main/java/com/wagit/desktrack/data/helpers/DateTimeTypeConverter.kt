package com.wagit.desktrack.data.helpers

import android.util.Log
import androidx.room.TypeConverter
import java.time.LocalDateTime


class DateTimeTypeConverter {
    /**
     * converts String to DateTime
     */
    @TypeConverter
    fun fromTimestamp(value: String?): LocalDateTime? {
        Log.v("dates", "$value")
        //null comes as string from database
        //TODO research about this bug
        return if (value != null && !value.equals("null")) LocalDateTime.parse(value) else null
    }

    /**
     * converts DateTime to String
     */
    @TypeConverter
    fun dateToTimestamp(date: LocalDateTime?): String {
        return date.toString()
    }
}