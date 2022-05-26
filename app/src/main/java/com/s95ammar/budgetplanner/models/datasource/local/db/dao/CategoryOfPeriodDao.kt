package com.s95ammar.budgetplanner.models.datasource.local.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.s95ammar.budgetplanner.models.IdWrapper
import com.s95ammar.budgetplanner.models.datasource.local.db.entity.CategoryOfPeriodEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryOfPeriodDao {
    @Insert
    suspend fun insert(categoriesOfPeriod: List<CategoryOfPeriodEntity>)

    @Update
    suspend fun update(categoryOfPeriod: List<CategoryOfPeriodEntity>)

    @Delete(entity = CategoryOfPeriodEntity::class)
    suspend fun delete(ids: List<IdWrapper>)

    @Query("SELECT * FROM categoryOfPeriod WHERE id=:id")
    fun getCategoryOfPeriodByIdFlow(id: Int): Flow<CategoryOfPeriodEntity>

    @Query("SELECT * FROM categoryOfPeriod")
    fun getAllCategoriesOfPeriodFlow(): Flow<List<CategoryOfPeriodEntity>>

}