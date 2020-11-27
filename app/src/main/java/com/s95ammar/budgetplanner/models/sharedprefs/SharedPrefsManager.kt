package com.s95ammar.budgetplanner.models.sharedprefs

interface SharedPrefsManager {
    fun doesActiveBudgetExist(): Boolean
    fun loadActiveBudgetId(): Int
    fun saveActiveBudgetId(id: Int)
    fun saveAuthToken(token: String)
    fun loadAuthToken(): String?
    fun clearAuthToken()
}