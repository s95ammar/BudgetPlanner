package com.s95ammar.budgetplanner.models.datasource.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.s95ammar.budgetplanner.models.datasource.local.db.dao.BudgetTransactionDao
import com.s95ammar.budgetplanner.models.datasource.local.db.dao.CategoryDao
import com.s95ammar.budgetplanner.models.datasource.local.db.dao.JoinDao
import com.s95ammar.budgetplanner.models.datasource.local.db.dao.PeriodDao
import com.s95ammar.budgetplanner.models.datasource.local.db.dao.PeriodicCategoryDao
import com.s95ammar.budgetplanner.models.datasource.local.db.entity.BudgetTransactionEntity
import com.s95ammar.budgetplanner.models.datasource.local.db.entity.CategoryEntity
import com.s95ammar.budgetplanner.models.datasource.local.db.entity.PeriodEntity
import com.s95ammar.budgetplanner.models.datasource.local.db.entity.PeriodicCategoryEntity

@Database(
    entities = [
        PeriodEntity::class,
        CategoryEntity::class,
        PeriodicCategoryEntity::class,
        BudgetTransactionEntity::class
        /*TODO: Savings entities*/
    ],
    version = BudgetPlannerDbConfig.DB_VERSION
)
abstract class BudgetPlannerDb : RoomDatabase() {

    abstract val periodDao: PeriodDao
    abstract val categoryDao: CategoryDao
    abstract val periodicCategoryDao: PeriodicCategoryDao
    abstract val budgetTransactionDao: BudgetTransactionDao
    abstract val joinDao: JoinDao

}
