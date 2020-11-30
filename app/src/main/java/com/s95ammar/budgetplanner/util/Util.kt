package com.s95ammar.budgetplanner.util

fun Boolean?.orFalse() = this ?: false

val Int.Companion.NO_ITEM
    get() = -1

fun String?.orEmptyJson() = this ?: "{}"