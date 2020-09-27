package com.s95ammar.budgetplanner.models.persistence.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.s95ammar.budgetplanner.models.data.CategoryStatus
import com.s95ammar.budgetplanner.models.persistence.dao.base.BaseDao

@Dao
interface CategoryStatusDao: BaseDao<CategoryStatus> {

    @Query("SELECT * FROM category_status WHERE id=:id")
    fun getCategoryStatusById(id: Int): LiveData<CategoryStatus>

    @Query("SELECT * FROM category_status")
    fun getAllCategoryStatuses(): LiveData<List<CategoryStatus>>
}