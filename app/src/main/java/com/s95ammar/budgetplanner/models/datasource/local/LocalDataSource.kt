package com.s95ammar.budgetplanner.models.datasource.local

import com.s95ammar.budgetplanner.models.datasource.local.db.entity.BudgetTransactionEntity
import com.s95ammar.budgetplanner.models.datasource.local.db.entity.CategoryEntity
import com.s95ammar.budgetplanner.models.datasource.local.db.entity.CurrencyEntity
import com.s95ammar.budgetplanner.models.datasource.local.db.entity.PeriodEntity
import com.s95ammar.budgetplanner.models.datasource.local.db.entity.PeriodicCategoryEntity
import com.s95ammar.budgetplanner.models.datasource.local.db.entity.join.BudgetTransactionJoinEntity
import com.s95ammar.budgetplanner.models.datasource.local.db.entity.join.PeriodicCategoryJoinEntity
import com.s95ammar.budgetplanner.models.datasource.local.db.entity.join.PeriodicCategorySimpleJoinEntity
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {

    fun getPeriodEditDataFlow(periodId: Int): Flow<List<PeriodicCategoryJoinEntity>>
    fun getPeriodInsertTemplateFlow(): Flow<List<PeriodicCategoryJoinEntity>>
    fun getAllPeriodsFlow(): Flow<List<PeriodEntity>>
    suspend fun insertPeriodWithPeriodicCategories(period: PeriodEntity, periodicCategories: List<PeriodicCategoryEntity>)
    suspend fun updatePeriodWithPeriodicCategories(
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
    fun getPeriodicCategorySimple(periodId: Int): Flow<List<PeriodicCategorySimpleJoinEntity>>

    fun getPeriodBudgetTransactionsFlow(periodId: Int): Flow<List<BudgetTransactionJoinEntity>>
    fun getBudgetTransactionFlow(id: Int): Flow<BudgetTransactionJoinEntity>
    suspend fun insertBudgetTransaction(budgetTransaction: BudgetTransactionEntity)
    suspend fun updateBudgetTransaction(budgetTransaction: BudgetTransactionEntity)
    suspend fun deleteBudgetTransaction(id: Int)

    suspend fun setMainCurrencyCode(code: String)
    fun getMainCurrencyCode(): String
    fun getMainCurrencyFlow(): Flow<CurrencyEntity?>
    fun getAllCurrenciesFlow(): Flow<List<CurrencyEntity>>
    suspend fun insertCurrency(currencyEntity: CurrencyEntity)
}
