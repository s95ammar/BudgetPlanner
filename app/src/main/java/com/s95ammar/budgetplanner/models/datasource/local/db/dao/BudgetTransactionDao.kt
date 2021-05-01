package com.s95ammar.budgetplanner.models.datasource.local.db.dao

import androidx.room.*
import com.s95ammar.budgetplanner.models.datasource.local.db.entity.BudgetTransactionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BudgetTransactionDao {
    @Insert
    suspend fun insert(budgetTransaction: BudgetTransactionEntity)

    @Update
    suspend fun update(budgetTransaction: BudgetTransactionEntity)

    @Delete
    suspend fun delete(budgetTransaction: BudgetTransactionEntity)

    @Query("SELECT * FROM budgetTransaction WHERE id=:id")
    fun getBudgetTransactionByIdFlow(id: Int): Flow<BudgetTransactionEntity>

    @Query("SELECT * FROM budgetTransaction")
    fun getAllBudgetTransactionsFlow(): Flow<List<BudgetTransactionEntity>>

}