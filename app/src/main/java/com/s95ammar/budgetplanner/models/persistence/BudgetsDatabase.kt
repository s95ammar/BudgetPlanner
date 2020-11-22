package com.s95ammar.budgetplanner.models.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import com.s95ammar.budgetplanner.models.data.*
import com.s95ammar.budgetplanner.models.persistence.dao.*

@Database(
    entities = [Budget::class, Category::class, CategoryStatus::class, BudgetTransaction::class, SavingsJar::class, Saving::class],
    version = 1
)
abstract class BudgetsDatabase : RoomDatabase() {

    companion object {
        const val DATABASE_NAME = "budgets_database"
    }
    abstract fun getBudgetDao(): BudgetDao
    abstract fun getCategoryDao(): CategoryDao
    abstract fun getBudgetTransactionDao(): BudgetTransactionDao
    abstract fun getCategoryStatusDao(): CategoryStatusDao
    abstract fun getSavingJarDao(): SavingJarDao
    abstract fun getSavingDao(): SavingDao
}