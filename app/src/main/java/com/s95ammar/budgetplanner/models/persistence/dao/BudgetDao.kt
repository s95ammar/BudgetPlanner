package com.s95ammar.budgetplanner.models.persistence.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.s95ammar.budgetplanner.models.data.Budget

@Dao
interface BudgetDao {

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun insertOrReplaceBudget(budget: Budget): Long

	@Delete
	suspend fun deleteBudget(budget: Budget)

	@Query("SELECT * FROM budget WHERE id=:id")
	suspend fun getBudgetById(id: Int): Budget

	@Query("SELECT * FROM budget WHERE id=:id")
	fun getBudgetByIdLiveData(id: Int): LiveData<Budget>

	@Query("SELECT * FROM budget")
	fun getAllBudgetsLiveData(): LiveData<List<Budget>>
}