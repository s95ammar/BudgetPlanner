package com.s95ammar.budgetplanner.models.datasource

import com.s95ammar.budgetplanner.models.api.requests.*
import com.s95ammar.budgetplanner.models.api.responses.*
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface RemoteDataSource {

    suspend fun register(userCredentials: UserCredentials): Flow<Response<TokenResponse>>
    suspend fun login(email: String, password: String): Flow<Response<TokenResponse>>
    suspend fun authenticate(): Flow<Response<String>>

    suspend fun getAllUserCategories(): Flow<Response<List<CategoryApiEntity>>>
    suspend fun getCategory(id: Int): Flow<Response<List<CategoryApiEntity>>>
    suspend fun insertCategory(request: CategoryUpsertApiRequest.Insertion): Flow<Response<CategoryApiEntity>>
    suspend fun updateCategory(request: CategoryUpsertApiRequest.Update): Flow<Response<CategoryApiEntity>>
    suspend fun deleteCategory(idBodyRequest: IdBodyRequest): Flow<Response<Boolean>>

    suspend fun getAllUserPeriods(): Flow<Response<List<PeriodSimpleApiEntity>>>
    suspend fun getPeriod(
        id: Int,
        includePeriodicCategories: Boolean,
        includeBudgetTransactions: Boolean,
        includeSavings: Boolean
    ): Flow<Response<PeriodApiEntity>>
    suspend fun getPeriodInsertTemplate(): Flow<Response<PeriodApiEntity>>
    suspend fun insertPeriod(request: PeriodUpsertApiRequest.Insertion): Flow<Response<PeriodApiEntity>>
    suspend fun updatePeriod(request: PeriodUpsertApiRequest.Update): Flow<Response<PeriodApiEntity>>
    suspend fun deletePeriod(idBodyRequest: IdBodyRequest): Flow<Response<Boolean>>

//    suspend fun getPeriodicCategory(id: Int?, periodId: Int?): Flow<Response<List<PeriodicCategoryApiEntity>>
//    suspend fun getPeriodicCategoriesForPeriod(periodId: Int?): Flow<Response<List<PeriodicCategoryApiEntity>>
//    suspend fun insertPeriodicCategory(request: PeriodicCategoryUpsertApiRequest.Insertion): Flow<Response<PeriodicCategoryApiEntity>
//    suspend fun updatePeriodicCategory(request: PeriodicCategoryUpsertApiRequest.Update): Flow<Response<PeriodicCategoryApiEntity>
//    suspend fun deletePeriodicCategory(id: IdBodyRequest): Flow<Response<Boolean>

    suspend fun getBudgetTransaction(id: Int?, periodId: Int?): Flow<Response<List<BudgetTransactionApiEntity>>>
    suspend fun getBudgetTransactionsForPeriod(periodId: Int?): Flow<Response<List<BudgetTransactionApiEntity>>>
    suspend fun insertBudgetTransaction(request: BudgetTransactionUpsertApiRequest.Insertion): Flow<Response<BudgetTransactionApiEntity>>
    suspend fun updateBudgetTransaction(request: BudgetTransactionUpsertApiRequest.Update): Flow<Response<BudgetTransactionApiEntity>>
    suspend fun deleteBudgetTransaction(idBodyRequest: IdBodyRequest): Flow<Response<Boolean>>
}