package com.s95ammar.budgetplanner.models.repository

import com.s95ammar.budgetplanner.models.datasource.local.LocalDataSource
import com.s95ammar.budgetplanner.models.datasource.local.db.entity.CategoryOfPeriodEntity
import com.s95ammar.budgetplanner.util.flowOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CategoryOfPeriodRepository @Inject constructor(
    private val localDataSource: LocalDataSource
) {

    fun getCategoriesOfPeriodFlow(periodId: Int) = localDataSource.getCategoriesOfPeriodFlow(periodId)
        .flowOn(Dispatchers.IO)

    fun getCategoryOfPeriodSimple(periodId: Int) = localDataSource.getCategoryOfPeriodSimple(periodId)
        .flowOn(Dispatchers.IO)

    fun updateCategoryOfPeriod(categoryOfPeriod: CategoryOfPeriodEntity) = flowOf {
        localDataSource.updateCategoryOfPeriodEstimate(categoryOfPeriod)
    }.flowOn(Dispatchers.IO)
}
