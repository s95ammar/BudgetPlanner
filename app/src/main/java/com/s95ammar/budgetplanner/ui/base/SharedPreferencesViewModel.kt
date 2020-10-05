package com.s95ammar.budgetplanner.ui.base

import android.content.Context
import androidx.lifecycle.ViewModel
import com.s95ammar.budgetplanner.ui.common.BudgetPlannerSharedPreferences
import com.s95ammar.budgetplanner.util.NO_ITEM

abstract class SharedPreferencesViewModel(applicationContext: Context): ViewModel() {

    private val sharedPreferences = applicationContext.getSharedPreferences(BudgetPlannerSharedPreferences.NAME, Context.MODE_PRIVATE)
    protected var activeBudgetId = loadActiveBudgetId()
    protected val activeBudgetExist
        get() = activeBudgetId != Int.NO_ITEM

    private fun loadActiveBudgetId(): Int {
        return sharedPreferences.getInt(BudgetPlannerSharedPreferences.KEY_ACTIVE_BUDGET_ID, Int.NO_ITEM)
    }

    protected fun saveActiveBudgetId(id: Int) {
        sharedPreferences.edit().putInt(BudgetPlannerSharedPreferences.KEY_ACTIVE_BUDGET_ID, id).apply()
        activeBudgetId = id
    }

}