package com.s95ammar.budgetplanner.flavor

import androidx.room.RoomDatabase
import com.s95ammar.budgetplanner.models.datasource.local.db.BudgetPlannerDb
import javax.inject.Inject

class FlavorConfigImpl @Inject constructor() : FlavorConfig {

    companion object {
        const val DATABASE_FILE_PATH = "database/budgetPlanner.db"
    }

    override fun <T : RoomDatabase> RoomDatabase.Builder<T>.configureDbPrepopulation(): RoomDatabase.Builder<T> {
        return createFromAsset(DATABASE_FILE_PATH)
    }
}
