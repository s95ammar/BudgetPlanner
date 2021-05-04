package com.s95ammar.budgetplanner.models.datasource.local.db.dao

import androidx.room.Dao
import androidx.room.Query
import com.s95ammar.budgetplanner.models.datasource.local.db.entity.join.PeriodicCategoryJoinEntity
import com.s95ammar.budgetplanner.util.INT_INVALID
import kotlinx.coroutines.flow.Flow

@Dao
interface JoinDao {

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
           IFNULL(btAmountSumGrouped.btAmountSum, 0) AS budgetTransactionsAmountSum
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
    fun getPeriodInsertTemplateFlow(): Flow<List<PeriodicCategoryJoinEntity>>

}