package com.s95ammar.budgetplanner.models.repository

import com.s95ammar.budgetplanner.models.api.parseResponse
import com.s95ammar.budgetplanner.models.api.requests.IdBodyRequest
import com.s95ammar.budgetplanner.models.api.requests.PeriodUpsertApiRequest
import com.s95ammar.budgetplanner.models.datasource.LocalDataSource
import com.s95ammar.budgetplanner.models.datasource.RemoteDataSource
import com.s95ammar.budgetplanner.util.flowOnIo
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PeriodRepository @Inject constructor(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource,
) {
    fun getPeriod(
        id: Int,
        includePeriodicCategories: Boolean = false,
        includeBudgetTransactions: Boolean = false,
        includeSavings: Boolean = false
    ) = flowOnIo {
        remoteDataSource.getPeriod(id, includePeriodicCategories, includeBudgetTransactions, includeSavings)
            .parseResponse()
    }

    fun getAllUserPeriods() = flowOnIo {
        remoteDataSource.getAllUserPeriods()
            .parseResponse()
    }

    fun insertPeriod(request: PeriodUpsertApiRequest.Insertion) = flowOnIo {
        remoteDataSource.insertPeriod(request)
            .parseResponse()
    }

    fun updatePeriod(request: PeriodUpsertApiRequest.Update) = flowOnIo {
        remoteDataSource.updatePeriod(request)
            .parseResponse()
    }

    fun deletePeriod(id: Int) = flowOnIo {
        remoteDataSource.deletePeriod(IdBodyRequest(id))
            .parseResponse()
    }

    fun getPeriodInsertTemplate() = flowOnIo {
        remoteDataSource.getPeriodInsertTemplate()
            .parseResponse()
    }
}