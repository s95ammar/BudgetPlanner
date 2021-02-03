package com.s95ammar.budgetplanner.models.repository

import com.s95ammar.budgetplanner.models.api.ApiService
import com.s95ammar.budgetplanner.models.api.requests.*
import com.s95ammar.budgetplanner.models.api.responses.ApiResult
import com.s95ammar.budgetplanner.models.api.responses.PeriodApiEntity
import com.s95ammar.budgetplanner.models.api.responses.TokenResponse
import com.s95ammar.budgetplanner.models.mapToApiResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteRepositoryImpl @Inject constructor(private val apiService: ApiService) : RemoteRepository {

    override suspend fun register(userCredentials: UserCredentials): ApiResult<TokenResponse> = apiRequest {
        apiService.register(userCredentials)
    }

    override suspend fun login(email: String, password: String) = apiRequest {
        apiService.login(email, password)
    }

    override suspend fun authenticate() = apiRequest {
        apiService.authenticate()
    }

    override suspend fun getAllUserCategories() = apiRequest {
        apiService.getCategory(id = null)
    }

    override suspend fun getCategory(id: Int) = apiRequest {
        apiService.getCategory(id)
    }

    override suspend fun insertCategory(request: CategoryUpsertApiRequest.Insertion) = apiRequest {
        apiService.insertCategory(request)
    }

    override suspend fun updateCategory(request: CategoryUpsertApiRequest.Update) = apiRequest {
        apiService.updateCategory(request)
    }

    override suspend fun deleteCategory(id: Int) = apiRequest {
        apiService.deleteCategory(IdBodyRequest(id))
    }

    override suspend fun getAllUserPeriods() = apiRequest {
        apiService.getAllUserPeriods()
    }

    override suspend fun getPeriod(
        id: Int,
        includePeriodicCategories: Boolean,
        includeBudgetTransactions: Boolean,
        includeSavings: Boolean
    ): ApiResult<PeriodApiEntity> = apiRequest {
        apiService.getPeriod(id, includePeriodicCategories, includeBudgetTransactions, includeSavings)
    }

    override suspend fun getPeriodInsertTemplate(): ApiResult<PeriodApiEntity> = apiRequest {
        apiService.getPeriodInsertTemplate()
    }

    override suspend fun insertPeriod(request: PeriodUpsertApiRequest.Insertion) = apiRequest {
        apiService.insertPeriod(request)
    }

    override suspend fun updatePeriod(request: PeriodUpsertApiRequest.Update) = apiRequest {
        apiService.updatePeriod(request)
    }

    override suspend fun deletePeriod(id: Int) = apiRequest {
        apiService.deletePeriod(IdBodyRequest(id))
    }

    override suspend fun getBudgetTransaction(id: Int?, periodId: Int?) = apiRequest {
        apiService.getBudgetTransaction(id, periodId)
    }

    override suspend fun getBudgetTransactionsForPeriod(periodId: Int?) = apiRequest {
        apiService.getBudgetTransaction(id = null, periodId)
    }

    override suspend fun insertBudgetTransaction(request: BudgetTransactionUpsertApiRequest.Insertion) = apiRequest {
        apiService.insertBudgetTransaction(request)
    }

    override suspend fun updateBudgetTransaction(request: BudgetTransactionUpsertApiRequest.Update) = apiRequest {
        apiService.updateBudgetTransaction(request)
    }

    override suspend fun deleteBudgetTransaction(id: IdBodyRequest) = apiRequest {
        apiService.deleteBudgetTransaction(id)
    }

    private suspend fun <T> apiRequest(request: suspend () -> Response<T>): ApiResult<T> = withContext(Dispatchers.IO) {
        try {
            request().mapToApiResult()
        } catch (e: Exception) {
            ApiResult.Error(e)
        }
    }

}