package com.s95ammar.budgetplanner.models.datasource.local.db.dao

import androidx.room.*
import com.s95ammar.budgetplanner.models.IdWrapper
import com.s95ammar.budgetplanner.models.datasource.local.db.BudgetPlannerDbConfig.TABLE_NAME_PERIOD
import com.s95ammar.budgetplanner.models.datasource.local.db.entity.PeriodEntity
import com.s95ammar.budgetplanner.models.datasource.local.db.entity.join.PeriodicCategoryJoinEntity
import com.s95ammar.budgetplanner.util.INT_INVALID
import kotlinx.coroutines.flow.Flow

@Dao
interface PeriodDao {
    @Insert
    suspend fun insert(period: PeriodEntity)

    @Update
    suspend fun update(period: PeriodEntity)

    @Delete(entity = PeriodEntity::class)
    suspend fun delete(id: IdWrapper)

    @Query("SELECT * FROM $TABLE_NAME_PERIOD WHERE id=:id")
    fun getPeriodByIdFlow(id: Int): Flow<PeriodEntity>

/*
    @Query("""""") // TODO
    fun getPeriodJoinEntityListFlow(periodId: Int): Flow<List<PeriodJoinEntity>>
*/

    @Query(
        """
    SELECT IFNULL(pcOfPeriod.id, $INT_INVALID) as periodicCategoryId,
           IFNULL(pcOfPeriod.periodId, $INT_INVALID) as periodId,
           category.id as categoryId,
           category.name as categoryName,
           pcOfPeriod.max as max,
           IFNULL(btAmountSumGrouped.btAmountSum, 0) as budgetTransactionsAmountSum
    FROM category
    LEFT JOIN (
        SELECT * FROM periodicCategory
    	WHERE periodId = :periodId
    ) AS pcOfPeriod
        ON category.id = pcOfPeriod.categoryId
    LEFT JOIN (
    	SELECT periodicCategoryId, SUM(amount) AS btAmountSum
    	FROM budgetTransaction
    	GROUP BY periodicCategoryId
    ) AS btAmountSumGrouped
        ON pcOfPeriod.id = btAmountSumGrouped.periodicCategoryId
    """
    )
    fun getPeriodicCategoryJoinEntityListFlow(periodId: Int): Flow<List<PeriodicCategoryJoinEntity>>

    @Query(
        """
    SELECT IFNULL(pcOfPeriod.id, $INT_INVALID) as periodicCategoryId,
           IFNULL(pcOfPeriod.periodId, $INT_INVALID) as periodId,
           category.id as categoryId,
           category.name as categoryName,
           pcOfPeriod.max as max,
           IFNULL(btAmountSumGrouped.btAmountSum, 0) as budgetTransactionsAmountSum
    FROM category
    LEFT JOIN (
        SELECT * FROM periodicCategory
    	WHERE periodId = (SELECT MAX(id) FROM period)
    ) AS pcOfPeriod
        ON category.id = pcOfPeriod.categoryId
    LEFT JOIN (
    	SELECT periodicCategoryId, SUM(amount) AS btAmountSum
    	FROM budgetTransaction
    	GROUP BY periodicCategoryId
    ) AS btAmountSumGrouped
        ON pcOfPeriod.id = btAmountSumGrouped.periodicCategoryId
    """
    )
    fun getPeriodInsertTemplate(): Flow<List<PeriodicCategoryJoinEntity>>

    @Query("SELECT * FROM period")
    fun getAllPeriodsFlow(): Flow<List<PeriodEntity>>
}