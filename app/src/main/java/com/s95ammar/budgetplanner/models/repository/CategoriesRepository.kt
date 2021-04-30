package com.s95ammar.budgetplanner.models.repository

import com.s95ammar.budgetplanner.models.datasource.local.LocalDataSource
import com.s95ammar.budgetplanner.models.datasource.local.db.entity.CategoryEntity
import com.s95ammar.budgetplanner.util.flowOnDispatcher
import kotlinx.coroutines.Dispatchers
//import com.s95ammar.budgetplanner.models.datasource.remote.RemoteDataSource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CategoriesRepository @Inject constructor(
    private val localDataSource: LocalDataSource,
//    private val remoteDataSource: RemoteDataSource,
) {
    fun deleteCategory(id: Int) = flowOnDispatcher(Dispatchers.IO) {
        localDataSource.deleteCategory(id)
    }

    fun getAllUserCategories() = localDataSource.getAllCategories()

    fun getCategory(id: Int) = flowOnDispatcher(Dispatchers.IO) {
        localDataSource.getCategory(id)
    }

    fun insertCategory(category: CategoryEntity) = flowOnDispatcher(Dispatchers.IO) {
        localDataSource.insertCategory(category)
    }

    fun updateCategory(category: CategoryEntity) = flowOnDispatcher(Dispatchers.IO) {
        localDataSource.updateCategory(category)
    }

}