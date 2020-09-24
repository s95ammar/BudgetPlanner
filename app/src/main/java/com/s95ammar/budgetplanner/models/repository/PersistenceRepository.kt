package com.s95ammar.budgetplanner.models.repository

import com.s95ammar.budgetplanner.models.data.*
import com.s95ammar.budgetplanner.models.persistence.dao.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PersistenceRepository @Inject constructor(
    private val budgetDao: BudgetDao,
    private val categoryDao: CategoryDao,
    private val budgetTransactionDao: BudgetTransactionDao,
    private val savingsJarDao: SavingJarDao,
    private val savingDao: SavingDao
) : Repository {

    // Budget CRUD

    override suspend fun insert(budget: Budget) = withContext(Dispatchers.IO) { budgetDao.insert(budget) }
    override suspend fun update(budget: Budget) = withContext(Dispatchers.IO) { budgetDao.update(budget) }
    override suspend fun delete(budget: Budget) = withContext(Dispatchers.IO) { budgetDao.delete(budget) }
    override fun getBudgetById(id: Int) = budgetDao.getBudgetById(id)
    override fun getAllBudgets() = budgetDao.getAllBudgets()

    // Category CRUD

    override suspend fun insert(category: Category) = withContext(Dispatchers.IO) { categoryDao.insert(category) }
    override suspend fun update(category: Category) = withContext(Dispatchers.IO) { categoryDao.update(category) }
    override suspend fun delete(category: Category) = withContext(Dispatchers.IO) { categoryDao.delete(category) }
    override fun getCategoryById(id: Int) = categoryDao.getCategoryById(id)
    override fun getAllCategories() = categoryDao.getAllCategories()

    // BudgetTransaction CRUD

    override suspend fun insert(budgetTransaction: BudgetTransaction) = withContext(Dispatchers.IO) { budgetTransactionDao.insert(budgetTransaction) }
    override suspend fun update(budgetTransaction: BudgetTransaction) = withContext(Dispatchers.IO) { budgetTransactionDao.update(budgetTransaction) }
    override suspend fun delete(budgetTransaction: BudgetTransaction) = withContext(Dispatchers.IO) { budgetTransactionDao.delete(budgetTransaction) }
    override fun getBudgetTransactionById(id: Int) = budgetTransactionDao.getBudgetTransaction(id)
    override fun getBudgetTransactionsBy(categoryStatusId: Int) = budgetTransactionDao.getBudgetTransactions(categoryStatusId)

    // SavingJar CRUD

    override suspend fun insert(savingsJar: SavingJar) = withContext(Dispatchers.IO) { savingsJarDao.insert(savingsJar) }
    override suspend fun update(savingsJar: SavingJar) = withContext(Dispatchers.IO) { savingsJarDao.update(savingsJar) }
    override suspend fun delete(savingsJar: SavingJar) = withContext(Dispatchers.IO) { savingsJarDao.delete(savingsJar) }
    override fun getSavingJarById(id: Int) = savingsJarDao.getSavingJarById(id)
    override fun getAllSavingJars() = savingsJarDao.getAllSavingJars()

    // Savings CRUD

    override suspend fun insert(saving: Saving) = withContext(Dispatchers.IO) { savingDao.insert(saving) }
    override suspend fun update(saving: Saving) = withContext(Dispatchers.IO) { savingDao.update(saving) }
    override suspend fun delete(saving: Saving) = withContext(Dispatchers.IO) { savingDao.delete(saving) }
    override fun getSavingById(id: Int) = savingDao.getSavingById(id)
    override fun getSavingsBy(savingsJarId: Int) = savingDao.getSavingsBy(savingsJarId)



}