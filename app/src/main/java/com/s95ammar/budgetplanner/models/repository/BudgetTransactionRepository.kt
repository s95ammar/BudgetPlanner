package com.s95ammar.budgetplanner.models.repository

import com.s95ammar.budgetplanner.models.datasource.local.LocalDataSource
import com.s95ammar.budgetplanner.models.datasource.local.db.entity.BudgetTransactionEntity
import com.s95ammar.budgetplanner.util.flowOnDispatcher
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

    fun insertBudgetTransactionFlow(budgetTransaction: BudgetTransactionEntity) = flowOnDispatcher(Dispatchers.IO) {
        localDataSource.insertBudgetTransaction(budgetTransaction)
    }

    fun updateBudgetTransactionFlow(budgetTransaction: BudgetTransactionEntity) = flowOnDispatcher(Dispatchers.IO) {
        localDataSource.updateBudgetTransaction(budgetTransaction)
    }

    fun deleteBudgetTransactionFlow(id: Int) = flowOnDispatcher(Dispatchers.IO) {
        localDataSource.deleteBudgetTransaction(id)
    }

}