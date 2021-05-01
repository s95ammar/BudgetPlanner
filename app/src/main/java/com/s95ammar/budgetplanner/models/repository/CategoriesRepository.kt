package com.s95ammar.budgetplanner.models.repository

import com.s95ammar.budgetplanner.models.datasource.local.LocalDataSource
import com.s95ammar.budgetplanner.models.datasource.local.db.entity.CategoryEntity
import com.s95ammar.budgetplanner.util.flowOnDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CategoriesRepository @Inject constructor(
    private val localDataSource: LocalDataSource
) {
    fun deleteCategoryFlow(id: Int) = flowOnDispatcher(Dispatchers.IO) {
        localDataSource.deleteCategory(id)
    }

    fun getAllUserCategoriesFlow() = localDataSource.getAllCategoriesFlow()

    fun getCategoryFlow(id: Int) = localDataSource.getCategoryFlow(id)

    fun insertCategoryFlow(category: CategoryEntity) = flowOnDispatcher(Dispatchers.IO) {
        localDataSource.insertCategory(category)
    }

    fun updateCategoryFlow(category: CategoryEntity) = flowOnDispatcher(Dispatchers.IO) {
        localDataSource.updateCategory(category)
    }

}