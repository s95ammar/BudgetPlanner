package com.s95ammar.budgetplanner.models.sharedprefs

import android.content.SharedPreferences
import com.s95ammar.budgetplanner.ui.common.Keys
import com.s95ammar.budgetplanner.util.NO_ITEM
import javax.inject.Inject

class SharedPrefsManagerImpl @Inject constructor(private val sharedPreferences: SharedPreferences) : SharedPrefsManager {

    companion object {
        const val SHARED_PREFERENCES_NAME = "BUDGET_PLANNER_SHARED_PREFERENCES"
    }

    override fun doesActiveBudgetExist() = loadActiveBudgetId() != Int.NO_ITEM

    override fun loadActiveBudgetId() = sharedPreferences.getInt(Keys.KEY_ACTIVE_BUDGET_ID, Int.NO_ITEM)

    override fun saveActiveBudgetId(id: Int) {
        sharedPreferences.edit().putInt(Keys.KEY_ACTIVE_BUDGET_ID, id).apply()
    }

}