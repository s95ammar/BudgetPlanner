package com.s95ammar.budgetplanner.models.datasource

import com.s95ammar.budgetplanner.models.api.ApiService
import com.s95ammar.budgetplanner.models.api.requests.*
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteDataSourceImpl @Inject constructor(private val apiService: ApiService) : RemoteDataSource {

    override suspend fun register(userCredentials: UserCredentials) = apiRequestAsFlow {
        apiService.register(userCredentials)
    }

    override suspend fun login(email: String, password: String) = apiRequestAsFlow {
        apiService.login(email, password)
    }

    override suspend fun authenticate() = apiRequestAsFlow {
        apiService.authenticate()
    }

    override suspend fun getCategory(id: Int?) = apiRequestAsFlow {
        apiService.getCategory(id)
    }

    override suspend fun insertCategory(request: CategoryUpsertApiRequest.Insertion) = apiRequestAsFlow {
        apiService.insertCategory(request)
    }

    override suspend fun updateCategory(request: CategoryUpsertApiRequest.Update) = apiRequestAsFlow {
        apiService.updateCategory(request)
    }

    override suspend fun deleteCategory(idBodyRequest: IdBodyRequest) = apiRequestAsFlow {
        apiService.deleteCategory(idBodyRequest)
    }

    override suspend fun getAllUserPeriods() = apiRequestAsFlow {
        apiService.getAllUserPeriods()
    }

    override suspend fun getPeriod(
        id: Int,
        includePeriodicCategories: Boolean,
        includeBudgetTransactions: Boolean,
        includeSavings: Boolean
    ) = apiRequestAsFlow {
        apiService.getPeriod(id, includePeriodicCategories, includeBudgetTransactions, includeSavings)
    }

    override suspend fun getPeriodInsertTemplate() = apiRequestAsFlow {
        apiService.getPeriodInsertTemplate()
    }

    override suspend fun insertPeriod(request: PeriodUpsertApiRequest.Insertion) = apiRequestAsFlow {
        apiService.insertPeriod(request)
    }

    override suspend fun updatePeriod(request: PeriodUpsertApiRequest.Update) = apiRequestAsFlow {
        apiService.updatePeriod(request)
    }

    override suspend fun deletePeriod(idBodyRequest: IdBodyRequest) = apiRequestAsFlow {
        apiService.deletePeriod(idBodyRequest)
    }

    override suspend fun getBudgetTransaction(id: Int?, periodId: Int?) = apiRequestAsFlow {
        apiService.getBudgetTransaction(id, periodId)
    }

    override suspend fun getBudgetTransactionsForPeriod(periodId: Int?) = apiRequestAsFlow {
        apiService.getBudgetTransaction(id = null, periodId)
    }

    override suspend fun insertBudgetTransaction(request: BudgetTransactionUpsertApiRequest.Insertion) = apiRequestAsFlow {
        apiService.insertBudgetTransaction(request)
    }

    override suspend fun updateBudgetTransaction(request: BudgetTransactionUpsertApiRequest.Update) = apiRequestAsFlow {
        apiService.updateBudgetTransaction(request)
    }

    override suspend fun deleteBudgetTransaction(idBodyRequest: IdBodyRequest) = apiRequestAsFlow {
        apiService.deleteBudgetTransaction(idBodyRequest)
    }

    private suspend fun <T> apiRequestAsFlow(request: suspend () -> Response<T>) = flow { emit(request()) }

}