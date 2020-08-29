package com.s95ammar.budgetplanner.models.repository

import androidx.lifecycle.LiveData
import com.s95ammar.budgetplanner.models.data.Budget
import com.s95ammar.budgetplanner.models.data.BudgetTransaction
import com.s95ammar.budgetplanner.models.data.Category

interface Repository {

    // Budget CRUD

    suspend fun insert(budget: Budget)
    suspend fun update(budgets: List<Budget>)
    suspend fun delete(budget: Budget)
    fun getBudgetById(id: Int): LiveData<Budget>
    fun getAllBudgets(): LiveData<List<Budget>>

     // Category CRUD

    suspend fun insert(category: Category)
    suspend fun update(category: Category)
    suspend fun delete(category: Category)
    fun getCategoryById(id: Int): LiveData<Category>
    fun getAllCategories(): LiveData<List<Category>>

     // BudgetTransaction CRUD

    suspend fun insert(budgetTransaction: BudgetTransaction)
    suspend fun update(budgetTransaction: BudgetTransaction)
    suspend fun delete(budgetTransaction: BudgetTransaction)
    fun getBudgetTransactionById(id: Int): LiveData<BudgetTransaction>
    fun getBudgetTransactionsBy(categoryStatusId: Int): LiveData<List<BudgetTransaction>>
}