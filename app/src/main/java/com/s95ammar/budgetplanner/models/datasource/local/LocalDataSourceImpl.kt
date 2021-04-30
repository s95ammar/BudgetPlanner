package com.s95ammar.budgetplanner.models.datasource.local

import com.s95ammar.budgetplanner.models.IdWrapper
import com.s95ammar.budgetplanner.models.datasource.local.db.dao.BudgetTransactionDao
import com.s95ammar.budgetplanner.models.datasource.local.db.dao.CategoryDao
import com.s95ammar.budgetplanner.models.datasource.local.db.dao.PeriodDao
import com.s95ammar.budgetplanner.models.datasource.local.db.dao.PeriodicCategoryDao
import com.s95ammar.budgetplanner.models.datasource.local.db.entity.PeriodEntity
import com.s95ammar.budgetplanner.models.datasource.local.db.entity.join.PeriodJoinEntity
import com.s95ammar.budgetplanner.models.datasource.local.db.entity.join.PeriodicCategoryJoinEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalDataSourceImpl @Inject constructor(
    private val budgetTransactionDao: BudgetTransactionDao,
    private val categoryDao: CategoryDao,
    private val periodDao: PeriodDao,
    private val periodicCategoryDao: PeriodicCategoryDao
) : LocalDataSource {

    override fun getPeriodJoinEntityList(periodId: Int): Flow<List<PeriodJoinEntity>> {
        TODO()
    }

    override fun getPeriodicCategoryJoinEntityList(id: Int): Flow<List<PeriodicCategoryJoinEntity>> {
        TODO()
    }

    override fun getAllPeriods(): Flow<List<PeriodEntity>> {
        return periodDao.getAllPeriods()
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

    override fun getPeriodInsertTemplate(): Flow<*> {
        TODO("Not yet implemented")
    }
}
