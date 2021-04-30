package com.s95ammar.budgetplanner.models.repository

//import com.s95ammar.budgetplanner.models.datasource.remote.RemoteDataSource
import com.s95ammar.budgetplanner.models.datasource.local.LocalDataSource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BudgetTransactionRepository @Inject constructor(
    private val localDataSource: LocalDataSource,
//    private val remoteDataSource: RemoteDataSource,
) {
/*
    fun getBudgetTransactionsForPeriod(periodId: Int?) = flowOnIo {
        remoteDataSource.getBudgetTransactionsForPeriod(periodId)
            .parseResponse()
    }

    fun getBudgetTransaction(id: Int?, periodId: Int?) = flowOnIo {
        remoteDataSource.getBudgetTransaction(id, periodId)
            .parseResponse()
            .map { it.singleOrNull() }
    }
*/

}