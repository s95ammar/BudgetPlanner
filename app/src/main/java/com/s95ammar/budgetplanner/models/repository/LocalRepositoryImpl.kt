package com.s95ammar.budgetplanner.models.repository

import com.s95ammar.budgetplanner.models.data.*
import com.s95ammar.budgetplanner.models.persistence.dao.*
import com.s95ammar.budgetplanner.models.sharedprefs.SharedPrefsManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalRepositoryImpl @Inject constructor(
    private val sharedPrefs: SharedPrefsManager,
    private val budgetDao: BudgetDao,
    private val categoryDao: CategoryDao,
    private val budgetTransactionDao: BudgetTransactionDao,
    private val savingsJarDao: SavingJarDao,
    private val savingDao: SavingDao
) : LocalRepository {

    // SharedPreferences

    override fun doesActiveBudgetExist() = sharedPrefs.doesActiveBudgetExist()
    override fun loadActiveBudgetId() = sharedPrefs.loadActiveBudgetId()
    override fun saveActiveBudgetId(id: Int) = sharedPrefs.saveActiveBudgetId(id)

    // Budget CRUD

    override suspend fun insertOrReplace(budget: Budget) = withContext(Dispatchers.IO) { budgetDao.insertOrReplace(budget) }
    override suspend fun delete(budget: Budget) = withContext(Dispatchers.IO) { budgetDao.delete(budget) }
    override suspend fun getBudgetById(id: Int) = withContext(Dispatchers.IO) { budgetDao.getBudgetById(id) }
    override fun getBudgetByIdLiveData(id: Int) = budgetDao.getBudgetByIdLiveData(id)
    override fun getAllBudgets() = budgetDao.getAllBudgets()

    // Category CRUD

    override suspend fun insertOrReplace(category: Category) = withContext(Dispatchers.IO) { categoryDao.insertOrReplace(category) }
    override suspend fun delete(category: Category) = withContext(Dispatchers.IO) { categoryDao.delete(category) }
    override fun getCategoryById(id: Int) = categoryDao.getCategoryById(id)
    override fun getAllCategories() = categoryDao.getAllCategories()

    // BudgetTransaction CRUD

    override suspend fun insertOrReplace(budgetTransaction: BudgetTransaction) = withContext(Dispatchers.IO) {
        budgetTransactionDao.insertOrReplace(budgetTransaction)
    }
    override suspend fun delete(budgetTransaction: BudgetTransaction) = withContext(Dispatchers.IO) {
        budgetTransactionDao.delete(budgetTransaction)
    }
    override fun getBudgetTransaction(id: Int) = budgetTransactionDao.getBudgetTransaction(id)
    override fun getBudgetTransactions(categoryStatusId: Int) = budgetTransactionDao.getBudgetTransactions(categoryStatusId)

    // SavingJar CRUD

    override suspend fun insertOrReplace(savingsJar: SavingJar) = withContext(Dispatchers.IO) { savingsJarDao.insertOrReplace(savingsJar) }
    override suspend fun delete(savingsJar: SavingJar) = withContext(Dispatchers.IO) { savingsJarDao.delete(savingsJar) }
    override fun getSavingJarById(id: Int) = savingsJarDao.getSavingJarById(id)
    override fun getAllSavingJars() = savingsJarDao.getAllSavingJars()

    // Savings CRUD

    override suspend fun insertOrReplace(saving: Saving) = withContext(Dispatchers.IO) { savingDao.insertOrReplace(saving) }
    override suspend fun delete(saving: Saving) = withContext(Dispatchers.IO) { savingDao.delete(saving) }
    override fun getSavingById(id: Int) = savingDao.getSavingById(id)
    override fun getSavingsBy(categoryStatusId: Int) = savingDao.getSavingsBy(categoryStatusId)

}