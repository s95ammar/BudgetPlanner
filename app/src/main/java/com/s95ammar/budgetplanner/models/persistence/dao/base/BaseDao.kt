package com.s95ammar.budgetplanner.models.persistence.dao.base

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy

interface BaseDao<T> {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrReplace(entity: T)

    @Delete
    suspend fun delete(entity: T)

}