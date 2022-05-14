package com.s95ammar.budgetplanner.models.repository

import com.s95ammar.budgetplanner.models.datasource.local.LocalDataSource
import com.s95ammar.budgetplanner.models.datasource.local.db.entity.BudgetTransactionEntity
import com.s95ammar.budgetplanner.util.flowOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BudgetTransactionRepository @Inject constructor(
    private val localDataSource: LocalDataSource
) {

    fun getPeriodBudgetTransactionsFlow(periodId: Int) = localDataSource.getPeriodBudgetTransactionsFlow(periodId)
        .flowOn(Dispatchers.IO)

    fun getBudgetTransactionFlow(id: Int) = localDataSource.getBudgetTransactionFlow(id)
        .flowOn(Dispatchers.IO)

    fun insertBudgetTransactionFlow(budgetTransaction: BudgetTransactionEntity) = flowOf {
        localDataSource.insertBudgetTransaction(budgetTransaction)
    }.flowOn(Dispatchers.IO)

    fun updateBudgetTransactionFlow(budgetTransaction: BudgetTransactionEntity) = flowOf {
        localDataSource.updateBudgetTransaction(budgetTransaction)
    }.flowOn(Dispatchers.IO)

    fun deleteBudgetTransactionFlow(id: Int) = flowOf {
        localDataSource.deleteBudgetTransaction(id)
    }.flowOn(Dispatchers.IO)

}