package com.s95ammar.budgetplanner.models.sharedprefs

import android.content.SharedPreferences
import com.s95ammar.budgetplanner.ui.common.Keys
import javax.inject.Inject

class SharedPrefsManagerImpl @Inject constructor(private val sharedPreferences: SharedPreferences) : SharedPrefsManager {

    companion object {
        const val SHARED_PREFERENCES_NAME = "BUDGET_PLANNER_SHARED_PREFERENCES"
    }

    override fun saveAuthToken(token: String) {
        sharedPreferences.edit().putString(Keys.KEY_AUTH_TOKEN, token).apply()
    }

    override fun loadAuthToken() = sharedPreferences.getString(Keys.KEY_AUTH_TOKEN, null)

    override fun clearAuthToken() {
        sharedPreferences.edit().remove(Keys.KEY_AUTH_TOKEN).apply()
    }
}