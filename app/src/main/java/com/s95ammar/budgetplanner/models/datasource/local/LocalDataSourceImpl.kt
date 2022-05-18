package com.s95ammar.budgetplanner.models.datasource.local

import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.room.withTransaction
import com.s95ammar.budgetplanner.models.IdWrapper
import com.s95ammar.budgetplanner.models.datasource.local.db.BudgetPlannerDb
import com.s95ammar.budgetplanner.models.datasource.local.db.dao.BudgetTransactionDao
import com.s95ammar.budgetplanner.models.datasource.local.db.dao.CategoryDao
import com.s95ammar.budgetplanner.models.datasource.local.db.dao.CurrencyDao
import com.s95ammar.budgetplanner.models.datasource.local.db.dao.JoinDao
import com.s95ammar.budgetplanner.models.datasource.local.db.dao.PeriodDao
import com.s95ammar.budgetplanner.models.datasource.local.db.dao.PeriodicCategoryDao
import com.s95ammar.budgetplanner.models.datasource.local.db.entity.BudgetTransactionEntity
import com.s95ammar.budgetplanner.models.datasource.local.db.entity.CategoryEntity
import com.s95ammar.budgetplanner.models.datasource.local.db.entity.CurrencyEntity
import com.s95ammar.budgetplanner.models.datasource.local.db.entity.PeriodEntity
import com.s95ammar.budgetplanner.models.datasource.local.db.entity.PeriodicCategoryEntity
import com.s95ammar.budgetplanner.models.datasource.local.db.entity.join.BudgetTransactionJoinEntity
import com.s95ammar.budgetplanner.models.datasource.local.db.entity.join.PeriodicCategoryIdAndNameJoinEntity
import com.s95ammar.budgetplanner.models.datasource.local.db.entity.join.PeriodicCategoryJoinEntity
import com.s95ammar.budgetplanner.models.datasource.local.prefs.BudgetPlannerPrefsConfig
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalDataSourceImpl @Inject constructor(
    private val db: BudgetPlannerDb,
    private val periodDao: PeriodDao,
    private val categoryDao: CategoryDao,
    private val periodicCategoryDao: PeriodicCategoryDao,
    private val budgetTransactionDao: BudgetTransactionDao,
    private val joinDao: JoinDao,
    private val currencyDao: CurrencyDao,
    private val currencyPrefs: SharedPreferences
) : LocalDataSource {

    // Period & PeriodicCategory
    override fun getPeriodEditDataFlow(periodId: Int): Flow<List<PeriodicCategoryJoinEntity>> {
        return joinDao.getPeriodEditData(periodId)
    }

    override fun getPeriodInsertTemplateFlow(): Flow<List<PeriodicCategoryJoinEntity>> {
        return joinDao.getPeriodInsertTemplateFlow()
    }

    override fun getAllPeriodsFlow(): Flow<List<PeriodEntity>> {
        return periodDao.getAllPeriodsFlow()
    }

    override suspend fun insertPeriodWithPeriodicCategories(period: PeriodEntity, periodicCategories: List<PeriodicCategoryEntity>) {
        db.withTransaction {
            val periodId = periodDao.insert(period)
            periodicCategoryDao.insert(periodicCategories.map { it.copy(periodId = periodId.toInt()) })
        }
    }

    override suspend fun updatePeriodWithPeriodicCategories(
        period: PeriodEntity,
        periodicCategoriesIdsToDelete: List<Int>,
        periodicCategoriesToUpdate: List<PeriodicCategoryEntity>,
        periodicCategoriesToInsert: List<PeriodicCategoryEntity>
    ) {
        db.withTransaction {
            periodDao.update(period)
            periodicCategoryDao.delete(periodicCategoriesIdsToDelete.map { IdWrapper(it) })
            periodicCategoryDao.update(periodicCategoriesToUpdate)
            periodicCategoryDao.insert(periodicCategoriesToInsert.map { it.copy(periodId = period.id) })
        }
    }

    override suspend fun deletePeriod(id: Int) {
        periodDao.delete(IdWrapper(id))
    }

    override fun getPeriodicCategoriesFlow(periodId: Int): Flow<List<PeriodicCategoryJoinEntity>> {
        return joinDao.getPeriodicCategoriesFlow(periodId)
    }

    override fun getPeriodicCategoryIdAndNameListFlow(periodId: Int): Flow<List<PeriodicCategoryIdAndNameJoinEntity>> {
        return joinDao.getPeriodicCategoryIdAndNameListFlow(periodId)
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
}
