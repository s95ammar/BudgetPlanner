package com.s95ammar.budgetplanner.models.repository

import com.s95ammar.budgetplanner.models.datasource.local.LocalDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PeriodicCategoryRepository @Inject constructor(
    private val localDataSource: LocalDataSource
) {

    fun getPeriodicCategoriesFlow(periodId: Int) = localDataSource.getPeriodicCategoriesFlow(periodId)
        .flowOn(Dispatchers.IO)

    fun getPeriodicCategoryIdAndNameListFlow(periodId: Int) = localDataSource.getPeriodicCategoryIdAndNameListFlow(periodId)
        .flowOn(Dispatchers.IO)
}
