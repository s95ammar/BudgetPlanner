package com.s95ammar.budgetplanner.models.datasource.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.s95ammar.budgetplanner.models.datasource.local.db.entity.CurrencyEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CurrencyDao {

    @Query("SELECT * FROM currency")
    fun getAllCurrenciesFlow(): Flow<List<CurrencyEntity>>

    @Query("SELECT * FROM currency WHERE code=:code")
    fun getCurrencyFlow(code: String): Flow<CurrencyEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertIfDoesNotExist(currencyEntity: CurrencyEntity)
}
