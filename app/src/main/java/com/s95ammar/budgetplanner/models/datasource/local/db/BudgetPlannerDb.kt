package com.s95ammar.budgetplanner.models.datasource.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.s95ammar.budgetplanner.models.datasource.local.db.dao.BudgetTransactionDao
import com.s95ammar.budgetplanner.models.datasource.local.db.dao.CategoryDao
import com.s95ammar.budgetplanner.models.datasource.local.db.dao.CategoryOfPeriodDao
import com.s95ammar.budgetplanner.models.datasource.local.db.dao.CurrencyDao
import com.s95ammar.budgetplanner.models.datasource.local.db.dao.JoinDao
import com.s95ammar.budgetplanner.models.datasource.local.db.dao.PeriodDao
import com.s95ammar.budgetplanner.models.datasource.local.db.entity.BudgetTransactionEntity
import com.s95ammar.budgetplanner.models.datasource.local.db.entity.CategoryEntity
import com.s95ammar.budgetplanner.models.datasource.local.db.entity.CategoryOfPeriodEntity
import com.s95ammar.budgetplanner.models.datasource.local.db.entity.CurrencyEntity
import com.s95ammar.budgetplanner.models.datasource.local.db.entity.PeriodEntity

@Database(
    entities = [
        PeriodEntity::class,
        CategoryEntity::class,
        CategoryOfPeriodEntity::class,
        BudgetTransactionEntity::class,
        CurrencyEntity::class
    ],
    version = BudgetPlannerDbConfig.DB_VERSION
)
abstract class BudgetPlannerDb : RoomDatabase() {

    abstract val periodDao: PeriodDao
    abstract val categoryDao: CategoryDao
    abstract val categoryOfPeriodDao: CategoryOfPeriodDao
    abstract val budgetTransactionDao: BudgetTransactionDao
    abstract val joinDao: JoinDao
    abstract val currencyDao: CurrencyDao

}
