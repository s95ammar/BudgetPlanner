package com.s95ammar.budgetplanner.models.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import com.s95ammar.budgetplanner.models.data.Budget
import com.s95ammar.budgetplanner.models.data.BudgetTransaction
import com.s95ammar.budgetplanner.models.data.Category

@Database(
    entities = [Budget::class, Category::class, BudgetTransaction::class],
    version = 1
)
abstract class BudgetsDatabase : RoomDatabase() {

    companion object {
        const val DATABASE_NAME = "budgets_database"
    }
    abstract fun getBudgetDao(): BudgetDao
    abstract fun getCategoryDao(): CategoryDao
    abstract fun getBudgetTransactionDao(): BudgetTransactionDao
}