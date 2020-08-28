package com.s95ammar.budgetplanner.models.persistence

import androidx.lifecycle.LiveData
import androidx.room.*
import com.s95ammar.budgetplanner.models.data.Budget

@Dao
interface BudgetDao {
	@Insert
	suspend fun insert(budget: Budget)

	@Update
	suspend fun update(budgets: List<Budget>)

	@Update
	suspend fun update(budget: Budget)

	@Delete
	suspend fun delete(budget: Budget)

	@Query("SELECT * FROM budget WHERE id=:id")
	fun getBudgetById(id: Int): LiveData<Budget>

	@Query("SELECT * FROM budget")
	fun getAllBudgets(): LiveData<List<Budget>>
}