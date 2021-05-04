package com.s95ammar.budgetplanner.models.repository

import com.s95ammar.budgetplanner.models.datasource.local.LocalDataSource
import com.s95ammar.budgetplanner.models.datasource.local.db.entity.PeriodEntity
import com.s95ammar.budgetplanner.util.flowOnDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PeriodRepository @Inject constructor(
    private val localDataSource: LocalDataSource,
//    private val remoteDataSource: RemoteDataSource,
) {

    fun getPeriodJoinEntityListFlow(id: Int) = localDataSource.getPeriodJoinEntityListFlow(id)

    fun getPeriodicCategoryJoinEntityListFlow(periodId: Int) = localDataSource.getPeriodicCategoryJoinEntityListFlow(periodId)

    fun getPeriodInsertTemplate() = localDataSource.getPeriodInsertTemplateFlow()

    fun getAllUserPeriodsFlow() = localDataSource.getAllPeriodsFlow()

    fun insertPeriodFlow(period: PeriodEntity) = flowOnDispatcher(Dispatchers.IO) {
        localDataSource.insertPeriod(period)
    }

    fun updatePeriodFlow(period: PeriodEntity) = flowOnDispatcher(Dispatchers.IO) {
        localDataSource.updatePeriod(period)
    }

    fun deletePeriodFlow(id: Int) = flowOnDispatcher(Dispatchers.IO) {
        localDataSource.deletePeriod(id)
    }

    fun getPeriodInsertTemplateFlow() = localDataSource.getPeriodInsertTemplateFlow()

}