package com.s95ammar.budgetplanner.models.repository

import com.s95ammar.budgetplanner.models.api.requests.*
import com.s95ammar.budgetplanner.models.api.responses.*

interface RemoteRepository {

    suspend fun register(userCredentials: UserCredentials): ApiResult<TokenResponse>
    suspend fun login(email: String, password: String): ApiResult<TokenResponse>
    suspend fun authenticate(): ApiResult<String>

    suspend fun getAllUserCategories(): ApiResult<List<CategoryApiEntity>>
    suspend fun getCategory(id: Int): ApiResult<List<CategoryApiEntity>>
    suspend fun insertCategory(request: CategoryUpsertApiRequest.Insertion): ApiResult<CategoryApiEntity>
    suspend fun updateCategory(request: CategoryUpsertApiRequest.Update): ApiResult<CategoryApiEntity>
    suspend fun deleteCategory(id: Int): ApiResult<Boolean>

    suspend fun getAllUserPeriods(): ApiResult<List<PeriodSimpleApiEntity>>
    suspend fun getPeriod(
        id: Int,
        includePeriodicCategories: Boolean = false,
        includeBudgetTransactions: Boolean = false,
        includeSavings: Boolean = false
    ): ApiResult<PeriodApiEntity>
    suspend fun getPeriodInsertTemplate(): ApiResult<PeriodApiEntity>
    suspend fun insertPeriod(request: PeriodUpsertApiRequest.Insertion): ApiResult<PeriodApiEntity>
    suspend fun updatePeriod(request: PeriodUpsertApiRequest.Update): ApiResult<PeriodApiEntity>
    suspend fun deletePeriod(id: Int): ApiResult<Boolean>

//    suspend fun getPeriodicCategory(id: Int?, periodId: Int?): ApiResult<List<PeriodicCategoryApiEntity>>
//    suspend fun getPeriodicCategoriesForPeriod(periodId: Int?): ApiResult<List<PeriodicCategoryApiEntity>>
//    suspend fun insertPeriodicCategory(request: PeriodicCategoryUpsertApiRequest.Insertion): ApiResult<PeriodicCategoryApiEntity>
//    suspend fun updatePeriodicCategory(request: PeriodicCategoryUpsertApiRequest.Update): ApiResult<PeriodicCategoryApiEntity>
//    suspend fun deletePeriodicCategory(id: IdBodyRequest): ApiResult<Boolean>

    suspend fun getBudgetTransaction(id: Int?, periodId: Int?): ApiResult<List<BudgetTransactionApiEntity>>
    suspend fun getBudgetTransactionsForPeriod(periodId: Int?): ApiResult<List<BudgetTransactionApiEntity>>
    suspend fun insertBudgetTransaction(request: BudgetTransactionUpsertApiRequest.Insertion): ApiResult<BudgetTransactionApiEntity>
    suspend fun updateBudgetTransaction(request: BudgetTransactionUpsertApiRequest.Update): ApiResult<BudgetTransactionApiEntity>
    suspend fun deleteBudgetTransaction(id: IdBodyRequest): ApiResult<Boolean>
}