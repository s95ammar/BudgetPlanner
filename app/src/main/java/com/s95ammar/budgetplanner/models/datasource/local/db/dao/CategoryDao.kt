package com.s95ammar.budgetplanner.models.datasource.local.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.s95ammar.budgetplanner.models.IdWrapper
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

    @Delete(entity = CategoryEntity::class)
    suspend fun delete(id: IdWrapper)

    @Query("SELECT * FROM category WHERE id=:id")
    fun getCategoryByIdFlow(id: Int): Flow<CategoryEntity>

    @Query("SELECT * FROM category")
    fun getAllCategoriesFlow(): Flow<List<CategoryEntity>>
}