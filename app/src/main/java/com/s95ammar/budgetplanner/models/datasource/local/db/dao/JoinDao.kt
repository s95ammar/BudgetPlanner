package com.s95ammar.budgetplanner.models.datasource.local.db.dao

import androidx.room.Dao
import androidx.room.Query
import com.s95ammar.budgetplanner.models.datasource.local.db.entity.join.BudgetTransactionJoinEntity
import com.s95ammar.budgetplanner.models.datasource.local.db.entity.join.PeriodicCategoryJoinEntity
import com.s95ammar.budgetplanner.models.datasource.local.db.entity.join.PeriodicCategorySimpleJoinEntity
import com.s95ammar.budgetplanner.util.INT_INVALID
import kotlinx.coroutines.flow.Flow

@Dao
interface JoinDao {

    // load all categories (not just the ones already added) to allow to add them to the period
    // values, which are not yet added to the period, will have a null value
    @Query(
        """
    SELECT IFNULL(pcOfPeriod.id, $INT_INVALID) AS periodicCategoryId,
           IFNULL(pcOfPeriod.periodId, $INT_INVALID) AS periodId,
           category.id AS categoryId,
           category.name AS categoryName,
           pcOfPeriod.estimate AS estimate,
           IFNULL(pcOfPeriod.currencyCode, :mainCurrencyCode) AS currencyCode,
           IFNULL(btAmountSumGrouped.btAmountSum, 0.0) AS budgetTransactionsAmountSum
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
    fun getPeriodEditData(periodId: Int, mainCurrencyCode: String): Flow<List<PeriodicCategoryJoinEntity>>

    // load data from last period as a template
    // values, which were not added to the last period, will have a null value
    @Query(
        """
    SELECT IFNULL(pcOfPeriod.id, $INT_INVALID) AS periodicCategoryId,
           IFNULL(pcOfPeriod.periodId, $INT_INVALID) AS periodId,
           category.id AS categoryId,
           category.name AS categoryName,
           pcOfPeriod.estimate AS estimate,
           IFNULL(pcOfPeriod.currencyCode, :mainCurrencyCode) AS currencyCode,
           0.0 AS budgetTransactionsAmountSum
    FROM category
    LEFT JOIN (
        SELECT * FROM periodicCategory
    	WHERE periodId = (SELECT MAX(id) FROM period)
    ) AS pcOfPeriod
        ON category.id = pcOfPeriod.categoryId
    """
    )
    fun getPeriodInsertTemplateFlow(mainCurrencyCode: String): Flow<List<PeriodicCategoryJoinEntity>>

    @Query(
        """
	SELECT periodicCategory.id AS periodicCategoryId,
	       periodicCategory.periodId AS periodId,
	       category.id AS categoryId,
	       category.name AS categoryName,
	       btAmountSumGrouped.btAmountSum AS budgetTransactionsAmountSum,
	       periodicCategory.estimate AS estimate,
           periodicCategory.currencyCode AS currencyCode 
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
	SELECT periodicCategory.id AS id,
           periodicCategory.currencyCode AS currencyCode,
	       category.name AS categoryName
	FROM category
	INNER JOIN periodicCategory ON category.id = periodicCategory.categoryId
	WHERE periodId = :periodId
        """
    )
    fun getPeriodicCategorySimple(periodId: Int): Flow<List<PeriodicCategorySimpleJoinEntity>>

    @Query(
        """
	SELECT budgetTransaction.id AS id,
	       budgetTransaction.name AS name,
	       budgetTransaction.amount AS amount,
	       budgetTransaction.lat AS lat,
	       budgetTransaction.lng AS lng,
	       budgetTransaction.creationUnixMs AS creationUnixMs,
	       periodicCategory.id AS periodicCategoryId,
           periodicCategory.periodId AS periodId,
           periodicCategory.currencyCode AS currencyCode,
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
	       budgetTransaction.amount AS amount,
	       budgetTransaction.lat AS lat,
	       budgetTransaction.lng AS lng,
	       budgetTransaction.creationUnixMs AS creationUnixMs,
	       periodicCategory.id AS periodicCategoryId,
           periodicCategory.periodId AS periodId,
           periodicCategory.currencyCode AS currencyCode,
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