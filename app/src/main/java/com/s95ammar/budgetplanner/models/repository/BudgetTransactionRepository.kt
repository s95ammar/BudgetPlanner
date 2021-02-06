package com.s95ammar.budgetplanner.models.repository

import com.s95ammar.budgetplanner.models.api.parseResponse
import com.s95ammar.budgetplanner.models.datasource.LocalDataSource
import com.s95ammar.budgetplanner.models.datasource.RemoteDataSource
import com.s95ammar.budgetplanner.util.flowOnIo
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BudgetTransactionRepository @Inject constructor(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource,
) {
    fun getBudgetTransactionsForPeriod(periodId: Int?) = flowOnIo {
        remoteDataSource.getBudgetTransactionsForPeriod(periodId)
            .parseResponse()
    }

}