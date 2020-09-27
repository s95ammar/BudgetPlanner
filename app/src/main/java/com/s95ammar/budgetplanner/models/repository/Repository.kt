package com.s95ammar.budgetplanner.models.repository

import androidx.lifecycle.LiveData
import com.s95ammar.budgetplanner.models.data.*

interface Repository {

    // Budget CRUD

    suspend fun insertOrReplace(budget: Budget)
    suspend fun delete(budget: Budget)
    fun getBudgetById(id: Int): LiveData<Budget>
    fun getAllBudgets(): LiveData<List<Budget>>

     // Category CRUD

    suspend fun insertOrReplace(category: Category)
    suspend fun delete(category: Category)
    fun getCategoryById(id: Int): LiveData<Category>
    fun getAllCategories(): LiveData<List<Category>>

     // BudgetTransaction CRUD

    suspend fun insertOrReplace(budgetTransaction: BudgetTransaction)
    suspend fun delete(budgetTransaction: BudgetTransaction)
    fun getBudgetTransactionById(id: Int): LiveData<BudgetTransaction>
    fun getBudgetTransactionsBy(categoryStatusId: Int): LiveData<List<BudgetTransaction>>

    // SavingJar CRUD

    suspend fun insertOrReplace(savingsJar: SavingJar)
    suspend fun delete(savingsJar: SavingJar)
    fun getSavingJarById(id: Int): LiveData<SavingJar>
    fun getAllSavingJars(): LiveData<List<SavingJar>>

    // Savings CRUD

    suspend fun insertOrReplace(saving: Saving)
    suspend fun delete(saving: Saving)
    fun getSavingById(id: Int): LiveData<Saving>
    fun getSavingsBy(savingsJarId: Int): LiveData<List<Saving>>
}