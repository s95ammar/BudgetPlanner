package com.s95ammar.budgetplanner.models.datasource.local.db.dao

import androidx.room.*
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

    @Query("SELECT * FROM periodicCategory WHERE id=:id")
    fun getPeriodicCategoryEntityById(id: Int): Flow<PeriodicCategoryEntity>

    @Query("SELECT * FROM periodicCategory")
    fun getAllCategories(): Flow<List<PeriodicCategoryEntity>>

}