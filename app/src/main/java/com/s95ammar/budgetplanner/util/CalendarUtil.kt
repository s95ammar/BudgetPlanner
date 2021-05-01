package com.s95ammar.budgetplanner.util

import java.text.SimpleDateFormat
import java.util.*

object CalendarUtil {

    private const val PATTERN_MONTH_DAY = "MMM dd"

    fun getNextMonthPeriodName(locale: Locale): String {
        val todayCalendar = Calendar.getInstance()
        val monthFromTodayCalendar = Calendar.getInstance().apply { add(Calendar.MONTH, 1) }
        val dateFormatter = SimpleDateFormat(PATTERN_MONTH_DAY, locale)
        return dateFormatter.format(todayCalendar.time) + " - " + dateFormatter.format(monthFromTodayCalendar.time)
    }

}