package com.s95ammar.budgetplanner.util

fun <T> Collection<T>.contains(predicate: (t: T) -> Boolean): Boolean {
    return indexOfFirst { predicate(it) } != Int.INVALID
}
