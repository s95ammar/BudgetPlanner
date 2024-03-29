package com.s95ammar.budgetplanner.models.datasource.local.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update
import com.s95ammar.budgetplanner.models.IdWrapper
import com.s95ammar.budgetplanner.models.datasource.local.db.entity.BudgetTransactionEntity

@Dao
interface BudgetTransactionDao {
    @Insert
    suspend fun insert(budgetTransaction: BudgetTransactionEntity)

    @Update
    suspend fun update(budgetTransaction: BudgetTransactionEntity)

    @Delete
    suspend fun delete(budgetTransaction: BudgetTransactionEntity)

    @Delete(entity = BudgetTransactionEntity::class)
    suspend fun delete(id: IdWrapper)

}