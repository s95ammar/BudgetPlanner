package com.s95ammar.budgetplanner

import android.util.Log

object Logger {
    private const val LOG_TAG = "Tag_Budget_Planner"

    fun logDebug(message: String) {
        Log.d(LOG_TAG, message)
    }

    fun logObjToString(obj: Any?) {
        Log.d(LOG_TAG, obj.toString())
    }
}