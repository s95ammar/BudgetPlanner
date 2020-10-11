package com.s95ammar.budgetplanner.models.persistence.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.s95ammar.budgetplanner.models.data.Budget

@Dao
interface BudgetDao {

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun insertOrReplace(budget: Budget): Long

	@Delete
	suspend fun delete(budget: Budget)

	@Query("SELECT * FROM budget WHERE id=:id")
	suspend fun getBudgetById(id: Int): Budget

	@Query("SELECT * FROM budget WHERE id=:id")
	fun getBudgetByIdLiveData(id: Int): LiveData<Budget>

	@Query("SELECT * FROM budget")
	fun getAllBudgets(): LiveData<List<Budget>>
}