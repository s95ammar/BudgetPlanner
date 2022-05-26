package com.s95ammar.budgetplanner.util

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

object CalendarUtil {

    private const val PATTERN_MONTH_YEAR = "MMM yyyy"
    private const val MIN_DAYS_TO_DISCARD_CURRENT_MONTH = 15

    fun getSuggestedPeriodName(locale: Locale): String {
        val calendar = Calendar.getInstance()
        val currentDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
        val periodMonth = if (currentDayOfMonth < MIN_DAYS_TO_DISCARD_CURRENT_MONTH) {
            calendar.get(Calendar.MONTH)
        } else {
            calendar.apply { add(Calendar.MONTH, 1) }.get(Calendar.MONTH)
        }
        return getPeriodNameByMonth(periodMonth, locale)
    }

    private fun getPeriodNameByMonth(month: Int, locale: Locale): String {
        val nextMonthCalendar = Calendar.getInstance().apply { set(Calendar.MONTH, month) }
        return SimpleDateFormat(PATTERN_MONTH_YEAR, locale).format(nextMonthCalendar.time)
    }
}
