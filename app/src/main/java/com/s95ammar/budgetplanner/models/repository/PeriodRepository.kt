package com.s95ammar.budgetplanner.models.repository

import com.s95ammar.budgetplanner.models.datasource.local.LocalDataSource
import com.s95ammar.budgetplanner.models.datasource.local.db.entity.PeriodEntity
import com.s95ammar.budgetplanner.models.datasource.local.db.entity.PeriodicCategoryEntity
import com.s95ammar.budgetplanner.util.flowOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PeriodRepository @Inject constructor(
    private val localDataSource: LocalDataSource
) {

    fun getPeriodEditDataFlow(periodId: Int) = localDataSource.getPeriodEditDataFlow(periodId)
        .flowOn(Dispatchers.IO)

    fun getPeriodInsertTemplateFlow() = localDataSource.getPeriodInsertTemplateFlow()
        .flowOn(Dispatchers.IO)

    fun getAllUserPeriodsFlow() = localDataSource.getAllPeriodsFlow()
        .flowOn(Dispatchers.IO)

    fun insertPeriodWithPeriodicCategoriesFlow(
        period: PeriodEntity,
        periodicCategories: List<PeriodicCategoryEntity>
    ) = flowOf {
        localDataSource.insertPeriodWithPeriodicCategories(period, periodicCategories)
    }.flowOn(Dispatchers.IO)

    fun updatePeriodWithPeriodicCategoriesFlow(
        period: PeriodEntity,
        periodicCategoriesIdsToDelete: List<Int>,
        periodicCategoriesToUpdate: List<PeriodicCategoryEntity>,
        periodicCategoriesToInsert: List<PeriodicCategoryEntity>
    ) = flowOf {
        localDataSource.updatePeriodWithPeriodicCategories(
            period,
            periodicCategoriesIdsToDelete,
            periodicCategoriesToUpdate,
            periodicCategoriesToInsert
        )
    }.flowOn(Dispatchers.IO)

    fun deletePeriodFlow(id: Int) = flowOf {
        localDataSource.deletePeriod(id)
    }.flowOn(Dispatchers.IO)

}
