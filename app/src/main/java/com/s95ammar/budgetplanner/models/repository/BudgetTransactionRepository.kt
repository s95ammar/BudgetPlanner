package com.s95ammar.budgetplanner.models.repository

import com.s95ammar.budgetplanner.models.datasource.local.LocalDataSource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BudgetTransactionRepository @Inject constructor(
    private val localDataSource: LocalDataSource
) {

    fun getBudgetTransactionsFlow(periodId: Int) = localDataSource.getBudgetTransactionsFlow(periodId)
}