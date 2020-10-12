package com.s95ammar.budgetplanner.models.persistence.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.s95ammar.budgetplanner.models.data.BudgetTransaction

@Dao
interface BudgetTransactionDao {

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun insertOrReplaceBudgetTransaction(budgetTransaction: BudgetTransaction)

	@Delete
	suspend fun delete(budgetTransaction: BudgetTransaction)

	@Query("SELECT * FROM budget_transaction WHERE id=:id")
	suspend fun getBudgetTransaction(id: Int): BudgetTransaction

	@Query("SELECT * FROM budget_transaction WHERE id=:id")
	fun getBudgetTransactionLiveData(id: Int): LiveData<BudgetTransaction>

	@Query("SELECT * FROM budget_transaction WHERE category_status_id=:categoryStatusId")
	fun getBudgetTransactionsLiveData(categoryStatusId: Int): LiveData<List<BudgetTransaction>>

}