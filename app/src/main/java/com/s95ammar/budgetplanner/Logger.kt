package com.s95ammar.budgetplanner

import android.util.Log
import kotlin.reflect.KClass

object Logger {

    private const val LOG_TAG = "Tag_Budget_Planner"

    fun logDebug(logSource: KClass<out Any>, message: String) {
        Log.d(LOG_TAG, "${logSource.simpleName}: $message")
    }

    fun logObjToString(logSource: KClass<out Any>, obj: Any?) {
        Log.d(LOG_TAG, "${logSource.simpleName}: ${obj.toString()}")
    }
}