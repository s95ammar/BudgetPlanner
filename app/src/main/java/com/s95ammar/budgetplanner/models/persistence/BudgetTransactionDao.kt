package com.s95ammar.budgetplanner.models.persistence

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

	@Query("DELETE FROM budgetTransaction")
	suspend fun deleteAllBudgetTransactions()

	@Query("SELECT * FROM budgetTransaction WHERE id=:id")
	fun getBudgetTransaction(id: Int): LiveData<BudgetTransaction>

	@Query("SELECT * FROM budgetTransaction WHERE budget_id=:budgetId")
	fun getBudgetTransactions(budgetId: Int): LiveData<List<BudgetTransaction>>

	@Query("SELECT * FROM budgetTransaction WHERE budget_id=:budgetId AND category_Id=:categoryId")
	fun getBudgetTransactions(categoryId: Int, budgetId: Int): LiveData<List<BudgetTransaction>>

}