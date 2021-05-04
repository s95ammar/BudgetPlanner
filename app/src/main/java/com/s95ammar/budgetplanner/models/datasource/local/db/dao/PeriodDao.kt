package com.s95ammar.budgetplanner.models.datasource.local.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.s95ammar.budgetplanner.models.IdWrapper
import com.s95ammar.budgetplanner.models.datasource.local.db.BudgetPlannerDbConfig.TABLE_NAME_PERIOD
import com.s95ammar.budgetplanner.models.datasource.local.db.entity.PeriodEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PeriodDao {
    @Insert
    suspend fun insert(period: PeriodEntity): Long

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

    @Query("SELECT * FROM period")
    fun getAllPeriodsFlow(): Flow<List<PeriodEntity>>
}