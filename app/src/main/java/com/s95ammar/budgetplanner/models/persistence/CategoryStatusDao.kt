package com.s95ammar.budgetplanner.models.persistence

import androidx.lifecycle.LiveData
import androidx.room.*
import com.s95ammar.budgetplanner.models.data.CategoryStatus

@Dao
interface CategoryStatusDao {
    @Insert
    suspend fun insert(categoryStatus: CategoryStatus)

    @Update
    suspend fun update(categoryStatus: CategoryStatus)

    @Delete
    suspend fun delete(categoryStatus: CategoryStatus)

    @Query("SELECT * FROM category_status WHERE id=:id")
    fun getCategoryStatusById(id: Int): LiveData<CategoryStatus>

    @Query("SELECT * FROM category_status")
    fun getAllCategoryStatuses(): LiveData<List<CategoryStatus>>
}