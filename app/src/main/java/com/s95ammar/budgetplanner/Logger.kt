package com.s95ammar.budgetplanner

import android.util.Log

private const val LOG_TAG = "Budget_Planner"

fun<T: Any> T.logFromHere(message: String) {
    Log.d(LOG_TAG, "${this::class.java.simpleName}: $message")
}

fun <T : Any> T.logThreadNameFromHere(message: String = "") {
    val prefix = if (message.isEmpty()) "" else "$message - "
    logFromHere("$prefix${Thread.currentThread().name}")
}

fun<T: Any> T.logFromHere(throwable: Throwable) {
    Log.e(LOG_TAG, "${this::class.java.simpleName}: ${throwable.stackTraceToString()}")
}