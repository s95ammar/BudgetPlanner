package com.s95ammar.budgetplanner.models.datasource.local

import com.s95ammar.budgetplanner.models.IdWrapper
import com.s95ammar.budgetplanner.models.datasource.local.db.BudgetPlannerDb
import com.s95ammar.budgetplanner.models.datasource.local.db.dao.BudgetTransactionDao
import com.s95ammar.budgetplanner.models.datasource.local.db.dao.CategoryDao
import com.s95ammar.budgetplanner.models.datasource.local.db.dao.PeriodDao
import com.s95ammar.budgetplanner.models.datasource.local.db.dao.PeriodicCategoryDao
import com.s95ammar.budgetplanner.models.datasource.local.db.entity.CategoryEntity
import com.s95ammar.budgetplanner.models.datasource.local.db.entity.PeriodEntity
import com.s95ammar.budgetplanner.models.datasource.local.db.entity.PeriodicCategoryEntity
import com.s95ammar.budgetplanner.models.datasource.local.db.entity.join.PeriodJoinEntity
import com.s95ammar.budgetplanner.models.datasource.local.db.entity.join.PeriodicCategoryJoinEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalDataSourceImpl @Inject constructor(
    private val db: BudgetPlannerDb,
    private val periodDao: PeriodDao,
    private val categoryDao: CategoryDao,
    private val periodicCategoryDao: PeriodicCategoryDao,
    private val budgetTransactionDao: BudgetTransactionDao
) : LocalDataSource {

    // Period
    override fun getPeriodJoinEntityListFlow(periodId: Int): Flow<List<PeriodJoinEntity>> {
        TODO("Not yet implemented")
    }

    override fun getPeriodicCategoryJoinEntityListFlow(periodId: Int): Flow<List<PeriodicCategoryJoinEntity>> {
        return periodDao.getPeriodicCategoryJoinEntityListFlow(periodId)
    }

    override fun getPeriodInsertTemplateFlow(): Flow<List<PeriodicCategoryJoinEntity>> {
        return periodDao.getPeriodInsertTemplate()
    }

    override fun getAllPeriodsFlow(): Flow<List<PeriodEntity>> {
        return periodDao.getAllPeriodsFlow()
    }

    override suspend fun insertPeriod(period: PeriodEntity) {
        periodDao.insert(period)
    }

    override suspend fun updatePeriod(period: PeriodEntity) {
        periodDao.update(period)
    }

    override suspend fun deletePeriod(id: Int) {
        periodDao.delete(IdWrapper(id))
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

    // PeriodicCategory
    override fun getAllPeriodicCategoriesFlow(): Flow<List<PeriodicCategoryEntity>> {
        return periodicCategoryDao.getAllPeriodicCategoriesFlow()
    }

    override fun getPeriodicCategoryFlow(id: Int): Flow<PeriodicCategoryEntity> {
        return periodicCategoryDao.getPeriodicCategoryByIdFlow(id)
    }

    override suspend fun insertPeriodicCategory(periodicCategory: PeriodicCategoryEntity) {
        periodicCategoryDao.insert(periodicCategory)
    }

    override suspend fun updatePeriodicCategory(periodicCategory: PeriodicCategoryEntity) {
        periodicCategoryDao.update(periodicCategory)
    }

    override suspend fun deletePeriodicCategory(id: Int) {
        periodicCategoryDao.delete(IdWrapper(id))
    }

}
