package com.s95ammar.budgetplanner.models.persistence.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.s95ammar.budgetplanner.models.data.SavingJar

@Dao
interface SavingJarDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrReplace(savingsJar: SavingJar)

    @Delete
    suspend fun delete(savingsJar: SavingJar)

    @Query("SELECT * FROM savings_jar WHERE id=:id")
    fun getSavingJarById(id: Int): LiveData<SavingJar>

    @Query("SELECT * FROM savings_jar")
    fun getAllSavingJars(): LiveData<List<SavingJar>>
}