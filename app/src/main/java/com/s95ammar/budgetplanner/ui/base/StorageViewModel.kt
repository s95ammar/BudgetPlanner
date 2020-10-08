package com.s95ammar.budgetplanner.ui.base

import android.content.Context
import androidx.lifecycle.ViewModel
import com.s95ammar.budgetplanner.ui.common.Keys
import com.s95ammar.budgetplanner.util.NO_ITEM

abstract class StorageViewModel(applicationContext: Context): ViewModel() {

    // TODO: implement injecting storage source as interface type & move implementation functions out of view model

    companion object {
        const val SHARED_PREFERENCES_NAME = "BUDGET_PLANNER_SHARED_PREFERENCES"
    }

    protected val sharedPreferences = applicationContext.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)

    protected val activeBudgetExist
        get() = loadActiveBudgetId() != Int.NO_ITEM

    protected fun loadActiveBudgetId(): Int {
        return sharedPreferences.getInt(Keys.KEY_ACTIVE_BUDGET_ID, Int.NO_ITEM)
    }

    protected fun saveActiveBudgetId(id: Int) {
        sharedPreferences.edit().putInt(Keys.KEY_ACTIVE_BUDGET_ID, id).apply()
    }

}