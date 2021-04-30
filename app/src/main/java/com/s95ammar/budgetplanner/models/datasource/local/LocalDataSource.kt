package com.s95ammar.budgetplanner.models.datasource.local

import com.s95ammar.budgetplanner.models.datasource.local.db.entity.CategoryEntity
import com.s95ammar.budgetplanner.models.datasource.local.db.entity.PeriodEntity
import com.s95ammar.budgetplanner.models.datasource.local.db.entity.join.PeriodJoinEntity
import com.s95ammar.budgetplanner.models.datasource.local.db.entity.join.PeriodicCategoryJoinEntity
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {

    fun getPeriodJoinEntityList(periodId: Int): Flow<List<PeriodJoinEntity>>
    fun getPeriodicCategoryJoinEntityList(id: Int, ): Flow<List<PeriodicCategoryJoinEntity>>
    fun getAllPeriods(): Flow<List<PeriodEntity>>
    suspend fun insertPeriod(period: PeriodEntity)
    suspend fun updatePeriod(period: PeriodEntity)
    suspend fun deletePeriod(id: Int)
    fun getPeriodInsertTemplate(): Flow<*> // TODO

    fun getAllCategories(): Flow<List<CategoryEntity>> { TODO("make abstract") }
    suspend fun getCategory(id: Int): CategoryEntity { TODO("make abstract") }
    suspend fun insertCategory(category: CategoryEntity) { TODO("make abstract") }
    suspend fun updateCategory(category: CategoryEntity) { TODO("make abstract") }
    suspend fun deleteCategory(id: Int) { TODO("make abstract") }

}