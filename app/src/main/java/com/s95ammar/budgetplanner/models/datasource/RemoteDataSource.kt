package com.s95ammar.budgetplanner.models.datasource

import com.s95ammar.budgetplanner.models.api.requests.*
import com.s95ammar.budgetplanner.models.api.responses.*
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface RemoteDataSource {

    fun register(userCredentials: UserCredentials): Flow<Response<TokenResponse>>
    fun login(email: String, password: String): Flow<Response<TokenResponse>>
    fun authenticate(): Flow<Response<String>>

    fun getCategory(id: Int?): Flow<Response<List<CategoryApiEntity>>>
    fun insertCategory(request: CategoryUpsertApiRequest.Insertion): Flow<Response<CategoryApiEntity>>
    fun updateCategory(request: CategoryUpsertApiRequest.Update): Flow<Response<CategoryApiEntity>>
    fun deleteCategory(idBodyRequest: IdBodyRequest): Flow<Response<Boolean>>

    fun getAllUserPeriods(): Flow<Response<List<PeriodSimpleApiEntity>>>
    fun getPeriod(
        id: Int,
        includePeriodicCategories: Boolean,
        includeBudgetTransactions: Boolean,
        includeSavings: Boolean
    ): Flow<Response<PeriodApiEntity>>
    fun getPeriodInsertTemplate(): Flow<Response<PeriodApiEntity>>
    fun insertPeriod(request: PeriodUpsertApiRequest.Insertion): Flow<Response<PeriodApiEntity>>
    fun updatePeriod(request: PeriodUpsertApiRequest.Update): Flow<Response<PeriodApiEntity>>
    fun deletePeriod(idBodyRequest: IdBodyRequest): Flow<Response<Boolean>>

    fun getBudgetTransaction(id: Int?, periodId: Int?): Flow<Response<List<BudgetTransactionApiEntity>>>
    fun getBudgetTransactionsForPeriod(periodId: Int?): Flow<Response<List<BudgetTransactionApiEntity>>>
    fun insertBudgetTransaction(request: BudgetTransactionUpsertApiRequest.Insertion): Flow<Response<BudgetTransactionApiEntity>>
    fun updateBudgetTransaction(request: BudgetTransactionUpsertApiRequest.Update): Flow<Response<BudgetTransactionApiEntity>>
    fun deleteBudgetTransaction(idBodyRequest: IdBodyRequest): Flow<Response<Boolean>>
}