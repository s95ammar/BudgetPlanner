package com.s95ammar.budgetplanner.models.persistence.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.s95ammar.budgetplanner.models.data.SavingJar

@Dao
interface SavingJarDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrReplaceSavingJar(savingsJar: SavingJar)

    @Delete
    suspend fun deleteSavingJar(savingsJar: SavingJar)

    @Query("SELECT * FROM savings_jar WHERE id=:id")
    suspend fun getSavingJarById(id: Int): SavingJar

    @Query("SELECT * FROM savings_jar WHERE id=:id")
    fun getSavingJarByIdLiveData(id: Int): LiveData<SavingJar>

    @Query("SELECT * FROM savings_jar")
    fun getAllSavingJarsLiveData(): LiveData<List<SavingJar>>
}