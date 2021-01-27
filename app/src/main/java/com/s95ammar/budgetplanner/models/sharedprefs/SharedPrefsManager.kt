package com.s95ammar.budgetplanner.models.sharedprefs

interface SharedPrefsManager {
    fun saveAuthToken(token: String)
    fun loadAuthToken(): String?
    fun clearAuthToken()
}