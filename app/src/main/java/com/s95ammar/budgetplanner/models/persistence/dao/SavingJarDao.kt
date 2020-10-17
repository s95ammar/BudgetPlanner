package com.s95ammar.budgetplanner.models.persistence.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.s95ammar.budgetplanner.models.data.SavingsJar

@Dao
interface SavingJarDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrReplaceSavingJar(savingsJar: SavingsJar)

    @Delete
    suspend fun deleteSavingJar(savingsJar: SavingsJar)

    @Query("SELECT * FROM savings_jar WHERE id=:id")
    suspend fun getSavingJarById(id: Int): SavingsJar

    @Query("SELECT * FROM savings_jar WHERE id=:id")
    fun getSavingJarByIdLiveData(id: Int): LiveData<SavingsJar>

    @Query("SELECT * FROM savings_jar")
    fun getAllSavingJarsLiveData(): LiveData<List<SavingsJar>>
}