package com.s95ammar.budgetplanner.models.persistence.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.s95ammar.budgetplanner.models.data.SavingJar
import com.s95ammar.budgetplanner.models.persistence.dao.base.BaseDao

@Dao
interface SavingJarDao: BaseDao<SavingJar> {

    @Query("SELECT * FROM savings_jar WHERE id=:id")
    fun getSavingJarById(id: Int): LiveData<SavingJar>

    @Query("SELECT * FROM savings_jar")
    fun getAllSavingJars(): LiveData<List<SavingJar>>
}