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

    fun getPeriodJoinEntityList(id: Int) = flowOnDispatcher(Dispatchers.IO) {
        localDataSource.getPeriodJoinEntityList(id)
    }

    fun getPeriodicCategoryJoinEntityList(id: Int) = localDataSource.getPeriodicCategoryJoinEntityList(id)

    fun getAllUserPeriods() = localDataSource.getAllPeriods()

    fun insertPeriod(period: PeriodEntity) = flowOnDispatcher(Dispatchers.IO) {
        localDataSource.insertPeriod(period)
    }

    fun updatePeriod(period: PeriodEntity) = flowOnDispatcher(Dispatchers.IO) {
        localDataSource.updatePeriod(period)
    }

    fun deletePeriod(id: Int) = flowOnDispatcher(Dispatchers.IO) {
        localDataSource.deletePeriod(id)
    }

    fun getPeriodInsertTemplate() = localDataSource.getPeriodInsertTemplate()

}