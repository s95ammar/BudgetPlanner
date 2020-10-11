package com.s95ammar.budgetplanner.models.persistence.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.s95ammar.budgetplanner.models.data.CategoryStatus

@Dao
interface CategoryStatusDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrReplace(entity: CategoryStatus)

    @Delete
    suspend fun delete(entity: CategoryStatus)

    @Query("SELECT * FROM category_status WHERE id=:id")
    fun getCategoryStatusById(id: Int): LiveData<CategoryStatus>

    @Query("SELECT * FROM category_status")
    fun getAllCategoryStatuses(): LiveData<List<CategoryStatus>>
}