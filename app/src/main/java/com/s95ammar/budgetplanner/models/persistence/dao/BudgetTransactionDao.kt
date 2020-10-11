package com.s95ammar.budgetplanner.models.persistence.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.s95ammar.budgetplanner.models.data.BudgetTransaction

@Dao
interface BudgetTransactionDao {

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun insertOrReplace(budgetTransaction: BudgetTransaction)

	@Delete
	suspend fun delete(budgetTransaction: BudgetTransaction)

	@Query("SELECT * FROM budget_transaction WHERE id=:id")
	fun getBudgetTransaction(id: Int): LiveData<BudgetTransaction>

	@Query("SELECT * FROM budget_transaction WHERE category_status_id=:categoryStatusId")
	fun getBudgetTransactions(categoryStatusId: Int): LiveData<List<BudgetTransaction>>

}