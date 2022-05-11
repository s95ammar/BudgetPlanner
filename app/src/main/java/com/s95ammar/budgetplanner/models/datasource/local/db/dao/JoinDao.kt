package com.s95ammar.budgetplanner.models.datasource.local.db.dao

import androidx.room.Dao
import androidx.room.Query
import com.s95ammar.budgetplanner.models.datasource.local.db.entity.join.BudgetTransactionJoinEntity
import com.s95ammar.budgetplanner.models.datasource.local.db.entity.join.PeriodicCategoryIdAndNameJoinEntity
import com.s95ammar.budgetplanner.models.datasource.local.db.entity.join.PeriodicCategoryJoinEntity
import com.s95ammar.budgetplanner.util.INT_INVALID
import kotlinx.coroutines.flow.Flow

@Dao
interface JoinDao {

    @Query(
        """
    SELECT IFNULL(pcOfPeriod.id, $INT_INVALID) AS periodicCategoryId,
           IFNULL(pcOfPeriod.periodId, $INT_INVALID) AS periodId,
           category.id AS categoryId,
           category.name AS categoryName,
           pcOfPeriod.max AS max,
           IFNULL(btAmountSumGrouped.btAmountSum, 0) AS budgetTransactionsAmountSum
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
    fun getPeriodEditData(periodId: Int): Flow<List<PeriodicCategoryJoinEntity>>

    @Query(
        """
    SELECT IFNULL(pcOfPeriod.id, $INT_INVALID) AS periodicCategoryId,
           IFNULL(pcOfPeriod.periodId, $INT_INVALID) AS periodId,
           category.id AS categoryId,
           category.name AS categoryName,
           pcOfPeriod.max AS max,
           0 AS budgetTransactionsAmountSum
    FROM category
    LEFT JOIN (
        SELECT * FROM periodicCategory
    	WHERE periodId = (SELECT MAX(id) FROM period)
    ) AS pcOfPeriod
        ON category.id = pcOfPeriod.categoryId
    """
    )
    fun getPeriodInsertTemplateFlow(): Flow<List<PeriodicCategoryJoinEntity>>

    @Query(
        """
	SELECT periodicCategory.id AS periodicCategoryId,
	       periodicCategory.periodId AS periodId,
	       category.id AS categoryId,
	       category.name AS categoryName,
	       btAmountSumGrouped.btAmountSum AS budgetTransactionsAmountSum,
	       periodicCategory.max AS max
	FROM category
	INNER JOIN periodicCategory ON category.id = periodicCategory.categoryId
	LEFT JOIN (
		SELECT periodicCategoryId, SUM(amount) AS btAmountSum
		FROM budgetTransaction
		GROUP BY periodicCategoryId
	) AS btAmountSumGrouped
	ON periodicCategory.id = btAmountSumGrouped.periodicCategoryId
	WHERE periodId = :periodId
        """
    )
    fun getPeriodicCategoriesFlow(periodId: Int): Flow<List<PeriodicCategoryJoinEntity>>

    @Query(
        """
	SELECT periodicCategory.id AS periodicCategoryId,
	       category.name AS categoryName
	FROM category
	INNER JOIN periodicCategory ON category.id = periodicCategory.categoryId
	WHERE periodId = :periodId
        """
    )
    fun getPeriodicCategoryIdAndNameListFlow(periodId: Int): Flow<List<PeriodicCategoryIdAndNameJoinEntity>>

    @Query(
        """
	SELECT budgetTransaction.id AS id,
	       budgetTransaction.name AS name,
	       budgetTransaction.type AS type,
	       budgetTransaction.amount AS amount,
	       budgetTransaction.lat AS lat,
	       budgetTransaction.lng AS lng,
	       budgetTransaction.creationUnixMs AS creationUnixMs,
	       periodicCategory.id AS periodicCategoryId,
           periodicCategory.periodId AS periodId,
	       category.name AS categoryName
	FROM budgetTransaction
	    INNER JOIN periodicCategory
	        ON budgetTransaction.periodicCategoryId = periodicCategory.id
	    INNER JOIN category
	        ON periodicCategory.categoryId = category.id
	WHERE periodId = :periodId
        """
    )
    fun getPeriodBudgetTransactionsFlow(periodId: Int): Flow<List<BudgetTransactionJoinEntity>>

    @Query(
        """
	SELECT budgetTransaction.id AS id,
	       budgetTransaction.name AS name,
	       budgetTransaction.type AS type,
	       budgetTransaction.amount AS amount,
	       budgetTransaction.lat AS lat,
	       budgetTransaction.lng AS lng,
	       budgetTransaction.creationUnixMs AS creationUnixMs,
	       periodicCategory.id AS periodicCategoryId,
           periodicCategory.periodId AS periodId,
	       category.name AS categoryName
	FROM budgetTransaction
	    INNER JOIN periodicCategory
	        ON budgetTransaction.periodicCategoryId = periodicCategory.id
	    INNER JOIN category
	        ON periodicCategory.categoryId = category.id
	WHERE budgetTransaction.id = :id
        """
    )
    fun getBudgetTransactionFlow(id: Int): Flow<BudgetTransactionJoinEntity>

}