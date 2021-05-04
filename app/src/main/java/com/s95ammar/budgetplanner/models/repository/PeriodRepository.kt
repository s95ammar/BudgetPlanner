package com.s95ammar.budgetplanner.models.repository

import com.s95ammar.budgetplanner.models.datasource.local.LocalDataSource
import com.s95ammar.budgetplanner.models.datasource.local.db.entity.PeriodEntity
import com.s95ammar.budgetplanner.models.datasource.local.db.entity.PeriodicCategoryEntity
import com.s95ammar.budgetplanner.util.flowOnDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PeriodRepository @Inject constructor(
    private val localDataSource: LocalDataSource
) {

    fun getPeriodJoinEntityListFlow(id: Int) = localDataSource.getPeriodJoinEntityListFlow(id)

    fun getPeriodicCategoryJoinEntityListFlow(periodId: Int) = localDataSource.getPeriodicCategoryJoinEntityListFlow(periodId)

    fun getPeriodInsertTemplate() = localDataSource.getPeriodInsertTemplateFlow()

    fun getAllUserPeriodsFlow() = localDataSource.getAllPeriodsFlow()

    fun insertPeriodWithPeriodicCategoriesFlow(
        period: PeriodEntity,
        periodicCategories: List<PeriodicCategoryEntity>
    ) = flowOnDispatcher(Dispatchers.IO) {
        localDataSource.insertPeriodWithPeriodicCategories(period, periodicCategories)
    }

    fun updatePeriodWithPeriodicCategoriesFlow(
        period: PeriodEntity,
        periodicCategoriesIdsToDelete: List<Int>,
        periodicCategoriesToUpdate: List<PeriodicCategoryEntity>,
        periodicCategoriesToInsert: List<PeriodicCategoryEntity>
    ) = flowOnDispatcher(Dispatchers.IO) {
        localDataSource.updatePeriodWithPeriodicCategoriesFlow(
            period,
            periodicCategoriesIdsToDelete,
            periodicCategoriesToUpdate,
            periodicCategoriesToInsert
        )
    }

    fun deletePeriodFlow(id: Int) = flowOnDispatcher(Dispatchers.IO) {
        localDataSource.deletePeriod(id)
    }

    fun getPeriodInsertTemplateFlow() = localDataSource.getPeriodInsertTemplateFlow()

}