package com.s95ammar.budgetplanner.util

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

object CalendarUtil {

    private const val PATTERN_MONTH_YEAR = "MMM yyyy"

    fun getNextMonthPeriodName(locale: Locale): String {
        val nextMonthCalendar = Calendar.getInstance().apply { add(Calendar.MONTH, 1) }
        return SimpleDateFormat(PATTERN_MONTH_YEAR, locale).format(nextMonthCalendar.time)
    }

}