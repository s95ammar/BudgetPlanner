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
    override fun loadAuthToken(): String? = sharedPrefs.loadAuthToken()
    override fun saveAuthToken(token: String) = sharedPrefs.saveAuthToken(token)

    // Budget CRUD

    override suspend fun insertOrReplaceBudget(budget: Budget) = withContext(Dispatchers.IO) { budgetDao.insertOrReplaceBudget(budget) }
    override suspend fun deleteBudget(budget: Budget) = withContext(Dispatchers.IO) { budgetDao.deleteBudget(budget) }
    override suspend fun getBudgetById(id: Int) = withContext(Dispatchers.IO) { budgetDao.getBudgetById(id) }
    override fun getBudgetByIdLiveData(id: Int) = budgetDao.getBudgetByIdLiveData(id)
    override fun getAllBudgetsLiveData() = budgetDao.getAllBudgetsLiveData()

    // Category CRUD

    override suspend fun insertOrReplaceCategory(category: Category) = withContext(Dispatchers.IO) { categoryDao.insertOrReplaceCategory(category) }
    override suspend fun deleteCategory(category: Category) = withContext(Dispatchers.IO) { categoryDao.deleteCategory(category) }
    override suspend fun getCategoryById(id: Int) = withContext(Dispatchers.IO) { categoryDao.getCategoryById(id) }
    override fun getCategoryByIdLiveData(id: Int) = categoryDao.getCategoryByIdLiveData(id)
    override fun getAllCategoriesLiveData() = categoryDao.getAllCategoriesLiveData()

    // BudgetTransaction CRUD

    override suspend fun insertOrReplaceBudgetTransaction(budgetTransaction: BudgetTransaction) = withContext(Dispatchers.IO) {
        budgetTransactionDao.insertOrReplaceBudgetTransaction(budgetTransaction)
    }
    override suspend fun deleteBudgetTransaction(budgetTransaction: BudgetTransaction) = withContext(Dispatchers.IO) {
        budgetTransactionDao.deleteBudgetTransaction(budgetTransaction)
    }
    override suspend fun getBudgetTransaction(id: Int) = withContext(Dispatchers.IO) { budgetTransactionDao.getBudgetTransaction(id) }
    override fun getBudgetTransactionLiveData(id: Int) = budgetTransactionDao.getBudgetTransactionLiveData(id)
    override fun getBudgetTransactionsLiveData(categoryStatusId: Int) = budgetTransactionDao.getBudgetTransactionsLiveData(categoryStatusId)

    // SavingJar CRUD

    override suspend fun insertOrReplaceSavingJar(savingsJar: SavingsJar) = withContext(Dispatchers.IO) { savingsJarDao.insertOrReplaceSavingJar(savingsJar) }
    override suspend fun deleteSavingJar(savingsJar: SavingsJar) = withContext(Dispatchers.IO) { savingsJarDao.deleteSavingJar(savingsJar) }
    override suspend fun getSavingJarById(id: Int) = withContext(Dispatchers.IO) { savingsJarDao.getSavingJarById(id) }
    override fun getSavingJarByIdLiveData(id: Int) = savingsJarDao.getSavingJarByIdLiveData(id)
    override fun getAllSavingJarsLiveData() = savingsJarDao.getAllSavingJarsLiveData()

    // Savings CRUD

    override suspend fun insertOrReplaceSaving(saving: Saving) = withContext(Dispatchers.IO) { savingDao.insertOrReplaceSaving(saving) }
    override suspend fun deleteSaving(saving: Saving) = withContext(Dispatchers.IO) { savingDao.deleteSaving(saving) }
    override suspend fun getSavingById(id: Int) = withContext(Dispatchers.IO) { savingDao.getSavingById(id) }
    override fun getSavingByIdLiveData(id: Int) = savingDao.getSavingByIdLiveData(id)
    override fun getSavingsByLiveData(categoryStatusId: Int) = savingDao.getSavingsByLiveData(categoryStatusId)

}