package com.s95ammar.budgetplanner.models.persistence.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.s95ammar.budgetplanner.models.data.Budget
import com.s95ammar.budgetplanner.models.persistence.dao.base.BaseDao

@Dao
interface BudgetDao: BaseDao<Budget> {

	@Query("SELECT * FROM budget WHERE id=:id")
	fun getBudgetById(id: Int): LiveData<Budget>

	@Query("SELECT * FROM budget")
	fun getAllBudgets(): LiveData<List<Budget>>
}