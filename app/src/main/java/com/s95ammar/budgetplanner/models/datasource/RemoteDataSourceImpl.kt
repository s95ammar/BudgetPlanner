package com.s95ammar.budgetplanner.models.datasource

import com.s95ammar.budgetplanner.models.api.ApiService
import com.s95ammar.budgetplanner.models.api.requests.*
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteDataSourceImpl @Inject constructor(private val apiService: ApiService) : RemoteDataSource {

    override fun register(userCredentials: UserCredentials) = apiRequestAsFlow {
        apiService.register(userCredentials)
    }

    override fun login(email: String, password: String) = apiRequestAsFlow {
        apiService.login(email, password)
    }

    override fun authenticate() = apiRequestAsFlow {
        apiService.authenticate()
    }

    override fun getCategory(id: Int?) = apiRequestAsFlow {
        apiService.getCategory(id)
    }

    override fun insertCategory(request: CategoryUpsertApiRequest.Insertion) = apiRequestAsFlow {
        apiService.insertCategory(request)
    }

    override fun updateCategory(request: CategoryUpsertApiRequest.Update) = apiRequestAsFlow {
        apiService.updateCategory(request)
    }

    override fun deleteCategory(idBodyRequest: IdBodyRequest) = apiRequestAsFlow {
        apiService.deleteCategory(idBodyRequest)
    }

    override fun getAllUserPeriods() = apiRequestAsFlow {
        apiService.getAllUserPeriods()
    }

    override fun getPeriod(
        id: Int,
        includePeriodicCategories: Boolean,
        includeBudgetTransactions: Boolean,
        includeSavings: Boolean
    ) = apiRequestAsFlow {
        apiService.getPeriod(id, includePeriodicCategories, includeBudgetTransactions, includeSavings)
    }

    override fun getPeriodInsertTemplate() = apiRequestAsFlow {
        apiService.getPeriodInsertTemplate()
    }

    override fun insertPeriod(request: PeriodUpsertApiRequest.Insertion) = apiRequestAsFlow {
        apiService.insertPeriod(request)
    }

    override fun updatePeriod(request: PeriodUpsertApiRequest.Update) = apiRequestAsFlow {
        apiService.updatePeriod(request)
    }

    override fun deletePeriod(idBodyRequest: IdBodyRequest) = apiRequestAsFlow {
        apiService.deletePeriod(idBodyRequest)
    }

    override fun getBudgetTransaction(id: Int?, periodId: Int?) = apiRequestAsFlow {
        apiService.getBudgetTransaction(id, periodId)
    }

    override fun getBudgetTransactionsForPeriod(periodId: Int?) = apiRequestAsFlow {
        apiService.getBudgetTransaction(id = null, periodId)
    }

    override fun insertBudgetTransaction(request: BudgetTransactionUpsertApiRequest.Insertion) = apiRequestAsFlow {
        apiService.insertBudgetTransaction(request)
    }

    override fun updateBudgetTransaction(request: BudgetTransactionUpsertApiRequest.Update) = apiRequestAsFlow {
        apiService.updateBudgetTransaction(request)
    }

    override fun deleteBudgetTransaction(idBodyRequest: IdBodyRequest) = apiRequestAsFlow {
        apiService.deleteBudgetTransaction(idBodyRequest)
    }

    private fun <T> apiRequestAsFlow(request: suspend () -> Response<T>) = flow { emit(request()) }

}