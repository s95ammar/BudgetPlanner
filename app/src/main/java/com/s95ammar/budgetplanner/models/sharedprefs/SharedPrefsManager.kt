package com.s95ammar.budgetplanner.models.sharedprefs

interface SharedPrefsManager {
    fun doesActiveBudgetExist(): Boolean
    fun loadActiveBudgetId(): Int
    fun saveActiveBudgetId(id: Int)
}