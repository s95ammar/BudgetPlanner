package com.s95ammar.budgetplanner.models.datasource.local

import com.s95ammar.budgetplanner.models.datasource.local.db.entity.BudgetTransactionEntity
import com.s95ammar.budgetplanner.models.datasource.local.db.entity.CategoryEntity
import com.s95ammar.budgetplanner.models.datasource.local.db.entity.CategoryOfPeriodEntity
import com.s95ammar.budgetplanner.models.datasource.local.db.entity.CurrencyEntity
import com.s95ammar.budgetplanner.models.datasource.local.db.entity.PeriodEntity
import com.s95ammar.budgetplanner.models.datasource.local.db.entity.join.BudgetTransactionJoinEntity
import com.s95ammar.budgetplanner.models.datasource.local.db.entity.join.CategoryOfPeriodJoinEntity
import com.s95ammar.budgetplanner.models.datasource.local.db.entity.join.CategoryOfPeriodSimpleJoinEntity
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {

    fun getPeriodEditDataFlow(periodId: Int): Flow<List<CategoryOfPeriodJoinEntity>>
    fun getPeriodInsertTemplateFlow(): Flow<List<CategoryOfPeriodJoinEntity>>
    fun getAllPeriodsFlow(): Flow<List<PeriodEntity>>
    suspend fun insertPeriodWithCategoriesOfPeriod(period: PeriodEntity, categoriesOfPeriod: List<CategoryOfPeriodEntity>)
    suspend fun updatePeriodWithCategoriesOfPeriod(
        period: PeriodEntity,
        categoriesOfPeriodIdsToDelete: List<Int>,
        categoriesOfPeriodToUpdate: List<CategoryOfPeriodEntity>,
        categoriesOfPeriodToInsert: List<CategoryOfPeriodEntity>
    )
    suspend fun deletePeriod(id: Int)

    fun getAllCategoriesFlow(): Flow<List<CategoryEntity>>
    fun getCategoryFlow(id: Int): Flow<CategoryEntity>
    suspend fun insertCategory(category: CategoryEntity)
    suspend fun updateCategory(category: CategoryEntity)
    suspend fun deleteCategory(id: Int)

    fun getCategoriesOfPeriodFlow(periodId: Int): Flow<List<CategoryOfPeriodJoinEntity>>
    fun getCategoryOfPeriodSimple(periodId: Int): Flow<List<CategoryOfPeriodSimpleJoinEntity>>
    suspend fun updateCategoryOfPeriodEstimate(categoryOfPeriod: CategoryOfPeriodEntity)

    fun getPeriodBudgetTransactionsFlow(periodId: Int): Flow<List<BudgetTransactionJoinEntity>>
    fun getBudgetTransactionFlow(id: Int): Flow<BudgetTransactionJoinEntity>
    suspend fun insertBudgetTransaction(budgetTransaction: BudgetTransactionEntity)
    suspend fun updateBudgetTransaction(budgetTransaction: BudgetTransactionEntity)
    suspend fun deleteBudgetTransaction(id: Int)

    suspend fun setMainCurrencyCode(code: String)
    fun getMainCurrencyCode(): String
    fun getMainCurrencyFlow(): Flow<CurrencyEntity?>
    fun getAllCurrenciesFlow(): Flow<List<CurrencyEntity>>
    suspend fun insertCurrencyIfDoesNotExist(currencyEntity: CurrencyEntity)
}
