package com.s95ammar.budgetplanner.models.datasource.local

import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.room.withTransaction
import com.s95ammar.budgetplanner.models.IdWrapper
import com.s95ammar.budgetplanner.models.datasource.local.db.BudgetPlannerDb
import com.s95ammar.budgetplanner.models.datasource.local.db.dao.BudgetTransactionDao
import com.s95ammar.budgetplanner.models.datasource.local.db.dao.CategoryDao
import com.s95ammar.budgetplanner.models.datasource.local.db.dao.CategoryOfPeriodDao
import com.s95ammar.budgetplanner.models.datasource.local.db.dao.CurrencyDao
import com.s95ammar.budgetplanner.models.datasource.local.db.dao.JoinDao
import com.s95ammar.budgetplanner.models.datasource.local.db.dao.PeriodDao
import com.s95ammar.budgetplanner.models.datasource.local.db.entity.BudgetTransactionEntity
import com.s95ammar.budgetplanner.models.datasource.local.db.entity.CategoryEntity
import com.s95ammar.budgetplanner.models.datasource.local.db.entity.CategoryOfPeriodEntity
import com.s95ammar.budgetplanner.models.datasource.local.db.entity.CurrencyEntity
import com.s95ammar.budgetplanner.models.datasource.local.db.entity.PeriodEntity
import com.s95ammar.budgetplanner.models.datasource.local.db.entity.join.BudgetTransactionJoinEntity
import com.s95ammar.budgetplanner.models.datasource.local.db.entity.join.CategoryOfPeriodJoinEntity
import com.s95ammar.budgetplanner.models.datasource.local.db.entity.join.CategoryOfPeriodSimpleJoinEntity
import com.s95ammar.budgetplanner.models.datasource.local.prefs.BudgetPlannerPrefsConfig
import com.s95ammar.budgetplanner.models.datasource.local.prefs.BudgetPlannerPrefsConfig.CURRENCY_PREFS
import com.s95ammar.budgetplanner.models.datasource.local.prefs.BudgetPlannerPrefsConfig.GENERAL_PREFS
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class LocalDataSourceImpl @Inject constructor(
    private val db: BudgetPlannerDb,
    private val periodDao: PeriodDao,
    private val categoryDao: CategoryDao,
    private val categoryOfPeriodDao: CategoryOfPeriodDao,
    private val budgetTransactionDao: BudgetTransactionDao,
    private val joinDao: JoinDao,
    private val currencyDao: CurrencyDao,
    @Named(GENERAL_PREFS) private val generalPrefs: SharedPreferences,
    @Named(CURRENCY_PREFS) private val currencyPrefs: SharedPreferences,
) : LocalDataSource {

    override fun getAndSetIsFirstLaunch(): Boolean {
        val isFirstLaunch = generalPrefs.getBoolean(BudgetPlannerPrefsConfig.KEY_IS_FIRST_LAUNCH, true)
        generalPrefs.edit { putBoolean(BudgetPlannerPrefsConfig.KEY_IS_FIRST_LAUNCH, false) }
        return isFirstLaunch
    }

    // Period & CategoryOfPeriod
    override fun getPeriodEditDataFlow(periodId: Int): Flow<List<CategoryOfPeriodJoinEntity>> {
        return joinDao.getPeriodEditData(periodId, getMainCurrencyCode())
    }

    override fun getPeriodInsertTemplateFlow(): Flow<List<CategoryOfPeriodJoinEntity>> {
        return joinDao.getPeriodInsertTemplateFlow(getMainCurrencyCode())
    }

    override fun getAllPeriodsFlow(): Flow<List<PeriodEntity>> {
        return periodDao.getAllPeriodsFlow()
    }

    override suspend fun insertPeriodWithCategoriesOfPeriod(period: PeriodEntity, categoriesOfPeriod: List<CategoryOfPeriodEntity>) {
        db.withTransaction {
            val periodId = periodDao.insert(period)
            categoryOfPeriodDao.insert(categoriesOfPeriod.map { it.copy(periodId = periodId.toInt()) })
        }
    }

    override suspend fun updatePeriodWithCategoriesOfPeriod(
        period: PeriodEntity,
        categoriesOfPeriodIdsToDelete: List<Int>,
        categoriesOfPeriodToUpdate: List<CategoryOfPeriodEntity>,
        categoriesOfPeriodToInsert: List<CategoryOfPeriodEntity>
    ) {
        db.withTransaction {
            periodDao.update(period)
            categoryOfPeriodDao.delete(categoriesOfPeriodIdsToDelete.map { IdWrapper(it) })
            categoryOfPeriodDao.update(categoriesOfPeriodToUpdate)
            categoryOfPeriodDao.insert(categoriesOfPeriodToInsert.map { it.copy(periodId = period.id) })
        }
    }

    override suspend fun deletePeriod(id: Int) {
        periodDao.delete(IdWrapper(id))
    }

    override fun getCategoriesOfPeriodFlow(periodId: Int): Flow<List<CategoryOfPeriodJoinEntity>> {
        return joinDao.getCategoriesOfPeriodFlow(periodId)
    }

    override fun getCategoryOfPeriodSimple(periodId: Int): Flow<List<CategoryOfPeriodSimpleJoinEntity>> {
        return joinDao.getCategoryOfPeriodSimple(periodId)
    }

    // Category
    override fun getAllCategoriesFlow(): Flow<List<CategoryEntity>> {
        return categoryDao.getAllCategoriesFlow()
    }

    override fun getCategoryFlow(id: Int): Flow<CategoryEntity> {
        return categoryDao.getCategoryByIdFlow(id)
    }

    override suspend fun insertCategory(category: CategoryEntity) {
        categoryDao.insert(category)
    }

    override suspend fun updateCategory(category: CategoryEntity) {
        categoryDao.update(category)
    }

    override suspend fun deleteCategory(id: Int) {
        categoryDao.delete(IdWrapper(id))
    }

    // Budget transaction
    override fun getPeriodBudgetTransactionsFlow(periodId: Int): Flow<List<BudgetTransactionJoinEntity>> {
        return joinDao.getPeriodBudgetTransactionsFlow(periodId)
    }

    override fun getBudgetTransactionFlow(id: Int): Flow<BudgetTransactionJoinEntity> {
        return joinDao.getBudgetTransactionFlow(id)
    }

    override suspend fun insertBudgetTransaction(budgetTransaction: BudgetTransactionEntity) {
        budgetTransactionDao.insert(budgetTransaction)
    }

    override suspend fun updateBudgetTransaction(budgetTransaction: BudgetTransactionEntity) {
        budgetTransactionDao.update(budgetTransaction)
    }

    override suspend fun deleteBudgetTransaction(id: Int) {
        budgetTransactionDao.delete(IdWrapper(id))
    }

    // Currency
    override suspend fun setMainCurrencyCode(code: String) {
        currencyPrefs.edit(commit = true) {
            putString(BudgetPlannerPrefsConfig.KEY_MAIN_CURRENCY_CODE, code)
        }
    }

    override fun getMainCurrencyCode(): String {
        return currencyPrefs.getString(BudgetPlannerPrefsConfig.KEY_MAIN_CURRENCY_CODE, "").orEmpty()
    }

    override fun getMainCurrencyFlow(): Flow<CurrencyEntity?> {
        val mainCurrencyCode = getMainCurrencyCode()
        return if (mainCurrencyCode.isNotEmpty()) {
            currencyDao.getCurrency(mainCurrencyCode)
        } else {
            flowOf(null)
        }
    }

    override fun getAllCurrenciesFlow(): Flow<List<CurrencyEntity>> {
        return currencyDao.getAllCurrencies()
    }

    override suspend fun insertCurrencyIfDoesNotExist(currencyEntity: CurrencyEntity) {
        currencyDao.insertIfDoesNotExist(currencyEntity)
    }
}
