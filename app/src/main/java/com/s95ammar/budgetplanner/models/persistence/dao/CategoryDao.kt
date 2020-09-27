package com.s95ammar.budgetplanner.models.persistence.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.s95ammar.budgetplanner.models.data.Category
import com.s95ammar.budgetplanner.models.persistence.dao.base.BaseDao

@Dao
interface CategoryDao: BaseDao<Category> {

	@Query("SELECT * FROM category WHERE id=:id")
	fun getCategoryById(id: Int): LiveData<Category>

	@Query("SELECT * FROM category")
	fun getAllCategories(): LiveData<List<Category>>
}