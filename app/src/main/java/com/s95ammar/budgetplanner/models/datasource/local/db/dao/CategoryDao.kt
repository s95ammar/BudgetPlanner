package com.s95ammar.budgetplanner.models.datasource.local.db.dao

import androidx.room.*
import com.s95ammar.budgetplanner.models.datasource.local.db.entity.CategoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {
    @Insert
    suspend fun insert(category: CategoryEntity)

    @Update
    suspend fun update(category: CategoryEntity)

    @Delete
    suspend fun delete(category: CategoryEntity)

    @Query("SELECT * FROM category WHERE id=:id")
    fun getCategoryEntityById(id: Int): Flow<CategoryEntity>

    @Query("SELECT * FROM category")
    fun getAllCategories(): Flow<List<CategoryEntity>>
}