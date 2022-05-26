package com.s95ammar.budgetplanner.models.datasource.local.db.dao

import androidx.room.Dao
import androidx.room.Query
import com.s95ammar.budgetplanner.models.datasource.local.db.entity.join.BudgetTransactionJoinEntity
import com.s95ammar.budgetplanner.models.datasource.local.db.entity.join.CategoryOfPeriodJoinEntity
import com.s95ammar.budgetplanner.models.datasource.local.db.entity.join.CategoryOfPeriodSimpleJoinEntity
import com.s95ammar.budgetplanner.util.INT_INVALID
import kotlinx.coroutines.flow.Flow

@Dao
interface JoinDao {

    // load all categories (not just the ones already added) to allow to add them to the period
    // values, which are not yet added to the period, will have a null value
    @Query(
        """
    SELECT IFNULL(pcOfPeriod.id, $INT_INVALID) AS categoryOfPeriodId,
           IFNULL(pcOfPeriod.periodId, $INT_INVALID) AS periodId,
           category.id AS categoryId,
           category.name AS categoryName,
           pcOfPeriod.estimate AS estimate,
           IFNULL(pcOfPeriod.currencyCode, :mainCurrencyCode) AS currencyCode,
           IFNULL(btAmountSumGrouped.btAmountSum, 0.0) AS budgetTransactionsAmountSum
    FROM category
    LEFT JOIN (
        SELECT * FROM categoryOfPeriod
    	WHERE periodId = :periodId
    ) AS pcOfPeriod
        ON category.id = pcOfPeriod.categoryId
    LEFT JOIN (
    	SELECT categoryOfPeriodId, SUM(amount) AS btAmountSum
    	FROM budgetTransaction
    	GROUP BY categoryOfPeriodId
    ) AS btAmountSumGrouped
        ON pcOfPeriod.id = btAmountSumGrouped.categoryOfPeriodId
    """
    )
    fun getPeriodEditData(periodId: Int, mainCurrencyCode: String): Flow<List<CategoryOfPeriodJoinEntity>>

    // load data from last period as a template
    // values, which were not added to the last period, will have a null value
    @Query(
        """
    SELECT IFNULL(pcOfPeriod.id, $INT_INVALID) AS categoryOfPeriodId,
           IFNULL(pcOfPeriod.periodId, $INT_INVALID) AS periodId,
           category.id AS categoryId,
           category.name AS categoryName,
           pcOfPeriod.estimate AS estimate,
           IFNULL(pcOfPeriod.currencyCode, :mainCurrencyCode) AS currencyCode,
           0.0 AS budgetTransactionsAmountSum
    FROM category
    LEFT JOIN (
        SELECT * FROM categoryOfPeriod
    	WHERE periodId = (SELECT MAX(id) FROM period)
    ) AS pcOfPeriod
        ON category.id = pcOfPeriod.categoryId
    """
    )
    fun getPeriodInsertTemplateFlow(mainCurrencyCode: String): Flow<List<CategoryOfPeriodJoinEntity>>

    @Query(
        """
	SELECT categoryOfPeriod.id AS categoryOfPeriodId,
	       categoryOfPeriod.periodId AS periodId,
	       category.id AS categoryId,
	       category.name AS categoryName,
	       btAmountSumGrouped.btAmountSum AS budgetTransactionsAmountSum,
	       categoryOfPeriod.estimate AS estimate,
           categoryOfPeriod.currencyCode AS currencyCode 
	FROM category
	INNER JOIN categoryOfPeriod ON category.id = categoryOfPeriod.categoryId
	LEFT JOIN (
		SELECT categoryOfPeriodId, SUM(amount) AS btAmountSum
		FROM budgetTransaction
		GROUP BY categoryOfPeriodId
	) AS btAmountSumGrouped
	ON categoryOfPeriod.id = btAmountSumGrouped.categoryOfPeriodId
	WHERE periodId = :periodId
        """
    )
    fun getCategoriesOfPeriodFlow(periodId: Int): Flow<List<CategoryOfPeriodJoinEntity>>

    @Query(
        """
	SELECT categoryOfPeriod.id AS id,
           categoryOfPeriod.currencyCode AS currencyCode,
	       category.name AS categoryName
	FROM category
	INNER JOIN categoryOfPeriod ON category.id = categoryOfPeriod.categoryId
	WHERE periodId = :periodId
        """
    )
    fun getCategoryOfPeriodSimple(periodId: Int): Flow<List<CategoryOfPeriodSimpleJoinEntity>>

    @Query(
        """
	SELECT budgetTransaction.id AS id,
	       budgetTransaction.name AS name,
	       budgetTransaction.amount AS amount,
           budgetTransaction.currencyCode AS currencyCode,
	       budgetTransaction.lat AS lat,
	       budgetTransaction.lng AS lng,
	       budgetTransaction.creationUnixMs AS creationUnixMs,
	       categoryOfPeriod.id AS categoryOfPeriodId,
           categoryOfPeriod.periodId AS periodId,
	       category.name AS categoryName
	FROM budgetTransaction
	    INNER JOIN categoryOfPeriod
	        ON budgetTransaction.categoryOfPeriodId = categoryOfPeriod.id
	    INNER JOIN category
	        ON categoryOfPeriod.categoryId = category.id
	WHERE periodId = :periodId
        """
    )
    fun getPeriodBudgetTransactionsFlow(periodId: Int): Flow<List<BudgetTransactionJoinEntity>>

    @Query(
        """
	SELECT budgetTransaction.id AS id,
	       budgetTransaction.name AS name,
	       budgetTransaction.amount AS amount,
           budgetTransaction.currencyCode AS currencyCode,
	       budgetTransaction.lat AS lat,
	       budgetTransaction.lng AS lng,
	       budgetTransaction.creationUnixMs AS creationUnixMs,
	       categoryOfPeriod.id AS categoryOfPeriodId,
           categoryOfPeriod.periodId AS periodId,
	       category.name AS categoryName
	FROM budgetTransaction
	    INNER JOIN categoryOfPeriod
	        ON budgetTransaction.categoryOfPeriodId = categoryOfPeriod.id
	    INNER JOIN category
	        ON categoryOfPeriod.categoryId = category.id
	WHERE budgetTransaction.id = :id
        """
    )
    fun getBudgetTransactionFlow(id: Int): Flow<BudgetTransactionJoinEntity>

}