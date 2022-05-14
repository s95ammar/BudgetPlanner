package com.s95ammar.budgetplanner.models.repository

import com.s95ammar.budgetplanner.models.datasource.local.LocalDataSource
import com.s95ammar.budgetplanner.models.datasource.local.db.entity.CategoryEntity
import com.s95ammar.budgetplanner.util.flowOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CategoriesRepository @Inject constructor(
    private val localDataSource: LocalDataSource
) {
    fun getAllUserCategoriesFlow() = localDataSource.getAllCategoriesFlow()
        .flowOn(Dispatchers.IO)

    fun getCategoryFlow(id: Int) = localDataSource.getCategoryFlow(id)
        .flowOn(Dispatchers.IO)

    fun insertCategoryFlow(category: CategoryEntity) = flowOf {
        localDataSource.insertCategory(category)
    }.flowOn(Dispatchers.IO)

    fun updateCategoryFlow(category: CategoryEntity) = flowOf {
        localDataSource.updateCategory(category)
    }.flowOn(Dispatchers.IO)

    fun deleteCategoryFlow(id: Int) = flowOf {
        localDataSource.deleteCategory(id)
    }.flowOn(Dispatchers.IO)

}
