package com.s95ammar.budgetplanner.models.repository

import com.s95ammar.budgetplanner.models.datasource.local.LocalDataSource
import com.s95ammar.budgetplanner.models.datasource.local.db.entity.CategoryOfPeriodEntity
import com.s95ammar.budgetplanner.models.datasource.local.db.entity.PeriodEntity
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

    fun insertPeriodWithCategoriesOfPeriodFlow(
        period: PeriodEntity,
        categoriesOfPeriod: List<CategoryOfPeriodEntity>
    ) = flowOf {
        localDataSource.insertPeriodWithCategoriesOfPeriod(period, categoriesOfPeriod)
    }.flowOn(Dispatchers.IO)

    fun updatePeriodWithCategoriesOfPeriodFlow(
        period: PeriodEntity,
        categoriesOfPeriodIdsToDelete: List<Int>,
        categoriesOfPeriodToUpdate: List<CategoryOfPeriodEntity>,
        categoriesOfPeriodToInsert: List<CategoryOfPeriodEntity>
    ) = flowOf {
        localDataSource.updatePeriodWithCategoriesOfPeriod(
            period,
            categoriesOfPeriodIdsToDelete,
            categoriesOfPeriodToUpdate,
            categoriesOfPeriodToInsert
        )
    }.flowOn(Dispatchers.IO)

    fun deletePeriodFlow(id: Int) = flowOf {
        localDataSource.deletePeriod(id)
    }.flowOn(Dispatchers.IO)

}
