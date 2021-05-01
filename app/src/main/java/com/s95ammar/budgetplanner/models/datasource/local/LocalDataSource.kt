package com.s95ammar.budgetplanner.models.datasource.local

import com.s95ammar.budgetplanner.models.datasource.local.db.entity.CategoryEntity
import com.s95ammar.budgetplanner.models.datasource.local.db.entity.PeriodEntity
import com.s95ammar.budgetplanner.models.datasource.local.db.entity.join.PeriodJoinEntity
import com.s95ammar.budgetplanner.models.datasource.local.db.entity.join.PeriodicCategoryJoinEntity
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {

    fun getPeriodJoinEntityListFlow(periodId: Int): Flow<List<PeriodJoinEntity>>
    fun getPeriodicCategoryJoinEntityListFlow(id: Int): Flow<List<PeriodicCategoryJoinEntity>>
    fun getAllPeriodsFlow(): Flow<List<PeriodEntity>>
    suspend fun insertPeriod(period: PeriodEntity)
    suspend fun updatePeriod(period: PeriodEntity)
    suspend fun deletePeriod(id: Int)
    fun getPeriodInsertTemplateFlow(): Flow<*> // TODO

    fun getAllCategoriesFlow(): Flow<List<CategoryEntity>>
    fun getCategoryFlow(id: Int): Flow<CategoryEntity>
    suspend fun insertCategory(category: CategoryEntity)
    suspend fun updateCategory(category: CategoryEntity)
    suspend fun deleteCategory(id: Int)

}