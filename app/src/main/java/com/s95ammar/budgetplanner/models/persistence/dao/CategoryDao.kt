package com.s95ammar.budgetplanner.models.persistence.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.s95ammar.budgetplanner.models.data.Category

@Dao
interface CategoryDao {

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun insertOrReplaceCategory(category: Category)

	@Delete
	suspend fun deleteCategory(category: Category)

	@Query("SELECT * FROM category WHERE id=:id")
	suspend fun getCategoryById(id: Int): Category

	@Query("SELECT * FROM category WHERE id=:id")
	fun getCategoryByIdLiveData(id: Int): LiveData<Category>

	@Query("SELECT * FROM category")
	fun getAllCategoriesLiveData(): LiveData<List<Category>>
}