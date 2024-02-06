package com.project.vacation.utils

import java.text.SimpleDateFormat
import java.util.*


class DateUtils constructor() {

    fun getFormattedDateString(dateFormat: String, date: Date?): String? {
        val df = SimpleDateFormat(dateFormat, Locale.US)
        if (date == null) {
            return null
        }
        return try {
            df.format(date)
        } catch (exception: Exception) {
            null
        }
    }

    companion object {
        const val YYYY_MM_DD_DATE_FORMAT = "yyyy-MM-dd"
    }

}