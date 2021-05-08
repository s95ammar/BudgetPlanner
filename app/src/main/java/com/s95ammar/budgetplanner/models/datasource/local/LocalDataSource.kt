package com.s95ammar.budgetplanner.models.datasource.local

import com.s95ammar.budgetplanner.models.datasource.local.db.entity.CategoryEntity
import com.s95ammar.budgetplanner.models.datasource.local.db.entity.PeriodEntity
import com.s95ammar.budgetplanner.models.datasource.local.db.entity.PeriodicCategoryEntity
import com.s95ammar.budgetplanner.models.datasource.local.db.entity.join.BudgetTransactionJoinEntity
import com.s95ammar.budgetplanner.models.datasource.local.db.entity.join.PeriodJoinEntity
import com.s95ammar.budgetplanner.models.datasource.local.db.entity.join.PeriodicCategoryJoinEntity
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {

    fun getPeriodJoinEntityListFlow(periodId: Int): Flow<List<PeriodJoinEntity>>
    fun getPeriodEditDataFlow(periodId: Int): Flow<List<PeriodicCategoryJoinEntity>>
    fun getPeriodInsertTemplateFlow(): Flow<List<PeriodicCategoryJoinEntity>>
    fun getAllPeriodsFlow(): Flow<List<PeriodEntity>>
    suspend fun insertPeriodWithPeriodicCategories(period: PeriodEntity, periodicCategories: List<PeriodicCategoryEntity>)
    suspend fun updatePeriodWithPeriodicCategoriesFlow(
        period: PeriodEntity,
        periodicCategoriesIdsToDelete: List<Int>,
        periodicCategoriesToUpdate: List<PeriodicCategoryEntity>,
        periodicCategoriesToInsert: List<PeriodicCategoryEntity>

    )
    suspend fun deletePeriod(id: Int)

    fun getAllCategoriesFlow(): Flow<List<CategoryEntity>>
    fun getCategoryFlow(id: Int): Flow<CategoryEntity>
    suspend fun insertCategory(category: CategoryEntity)
    suspend fun updateCategory(category: CategoryEntity)
    suspend fun deleteCategory(id: Int)

    fun getPeriodicCategoriesFlow(periodId: Int): Flow<List<PeriodicCategoryJoinEntity>>

    fun getBudgetTransactionsFlow(periodId: Int): Flow<List<BudgetTransactionJoinEntity>>
}
