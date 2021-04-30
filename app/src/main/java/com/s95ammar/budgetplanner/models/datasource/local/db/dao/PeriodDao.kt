package com.s95ammar.budgetplanner.models.datasource.local.db.dao

import androidx.room.*
import com.s95ammar.budgetplanner.models.IdWrapper
import com.s95ammar.budgetplanner.models.datasource.local.db.BudgetPlannerDbConfig.TABLE_NAME_PERIOD
import com.s95ammar.budgetplanner.models.datasource.local.db.entity.PeriodEntity
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
    fun getPeriodById(id: Int): Flow<PeriodEntity>

/*
    @Query("""""") // TODO
    fun getPeriodJoinEntityList(periodId: Int): Flow<List<PeriodJoinEntity>>
*/

/*
    @Query( // TODO
        """
    SELECT periodic_category_id, periodId, category_name, category_id, max, budget_transactions_amount_sum
    FROM (
            SELECT p.id AS period_id, c.name AS category_name, c.id AS category_id, pc.id AS periodic_category_id, pc.max AS max, pc.id AS periodId
            FROM period p
                INNER JOIN periodicCategory pc
                    ON p.id = pc.periodId
                INNER JOIN category c
                    ON pc.categoryId = c.id
            WHERE p.id = :periodId
    ) AS data
    INNER JOIN (
        SELECT periodicCategoryId, SUM(amount) AS budget_transactions_amount_sum
        FROM budgetTransaction
        GROUP BY periodicCategoryId
    ) AS sum
    on data.periodId = sum.periodicCategoryId
        """
    )
    fun getPeriodicCategoryJoinEntityList(periodId: Int): Flow<List<PeriodicCategoryJoinEntity>>
*/

    @Query("SELECT * FROM period")
    fun getAllPeriods(): Flow<List<PeriodEntity>>
}