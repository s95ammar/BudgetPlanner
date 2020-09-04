package com.s95ammar.budgetplanner.models.persistence.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.s95ammar.budgetplanner.models.data.BudgetTransaction

@Dao
interface BudgetTransactionDao {
	@Insert
	suspend fun insert(budgetTransaction: BudgetTransaction)

	@Update
	suspend fun update(budgetTransaction: BudgetTransaction)

	@Delete
	suspend fun delete(budgetTransaction: BudgetTransaction)

	@Query("DELETE FROM budget_transaction")
	suspend fun deleteAllBudgetTransactions()

	@Query("SELECT * FROM budget_transaction WHERE id=:id")
	fun getBudgetTransaction(id: Int): LiveData<BudgetTransaction>

	@Query("SELECT * FROM budget_transaction WHERE category_status_id=:categoryStatusId")
	fun getBudgetTransactions(categoryStatusId: Int): LiveData<List<BudgetTransaction>>

}