package com.s95ammar.budgetplanner.models.datasource.local.db.dao

import androidx.room.*
import com.s95ammar.budgetplanner.models.IdWrapper
import com.s95ammar.budgetplanner.models.datasource.local.db.entity.PeriodicCategoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PeriodicCategoryDao {
    @Insert
    suspend fun insert(periodicCategory: PeriodicCategoryEntity)

    @Update
    suspend fun update(periodicCategory: PeriodicCategoryEntity)

    @Delete
    suspend fun delete(periodicCategory: PeriodicCategoryEntity)

    @Delete(entity = PeriodicCategoryEntity::class)
    suspend fun delete(id: IdWrapper)

    @Query("SELECT * FROM periodicCategory WHERE id=:id")
    fun getPeriodicCategoryByIdFlow(id: Int): Flow<PeriodicCategoryEntity>

    @Query("SELECT * FROM periodicCategory")
    fun getAllPeriodicCategoriesFlow(): Flow<List<PeriodicCategoryEntity>>

}