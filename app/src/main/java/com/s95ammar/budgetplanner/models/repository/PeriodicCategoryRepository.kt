package com.s95ammar.budgetplanner.models.repository

import com.s95ammar.budgetplanner.models.datasource.local.LocalDataSource
import com.s95ammar.budgetplanner.models.datasource.local.db.entity.PeriodicCategoryEntity
import com.s95ammar.budgetplanner.util.flowOnDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PeriodicCategoryRepository @Inject constructor(
    private val localDataSource: LocalDataSource,
//    private val remoteDataSource: RemoteDataSource,
) {

    fun getAllPeriodicCategoriesFlow() = localDataSource.getAllPeriodicCategoriesFlow()

    fun insertPeriodicCategoryFlow(periodicCategory: PeriodicCategoryEntity) = flowOnDispatcher(Dispatchers.IO) {
        localDataSource.insertPeriodicCategory(periodicCategory)
    }

    fun updatePeriodicCategoryFlow(periodicCategory: PeriodicCategoryEntity) = flowOnDispatcher(Dispatchers.IO) {
        localDataSource.updatePeriodicCategory(periodicCategory)
    }

    fun deletePeriodicCategoryFlow(id: Int) = flowOnDispatcher(Dispatchers.IO) {
        localDataSource.deletePeriodicCategory(id)
    }

}
