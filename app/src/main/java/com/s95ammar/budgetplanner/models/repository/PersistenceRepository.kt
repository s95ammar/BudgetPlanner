package com.s95ammar.budgetplanner.models.repository

import com.s95ammar.budgetplanner.models.data.Budget
import com.s95ammar.budgetplanner.models.data.BudgetTransaction
import com.s95ammar.budgetplanner.models.data.Category
import com.s95ammar.budgetplanner.models.persistence.BudgetDao
import com.s95ammar.budgetplanner.models.persistence.BudgetTransactionDao
import com.s95ammar.budgetplanner.models.persistence.CategoryDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PersistenceRepository @Inject constructor(
    private val budgetDao: BudgetDao,
    private val categoryDao: CategoryDao,
    private val budgetTransactionDao: BudgetTransactionDao
) : Repository {

    // Budget CRUD

    override suspend fun insert(budget: Budget) = withContext(Dispatchers.IO) { budgetDao.insert(budget) }
    override suspend fun update(budgets: List<Budget>) = withContext(Dispatchers.IO) { budgetDao.update(budgets) }
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

}