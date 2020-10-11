package com.s95ammar.budgetplanner.models.persistence.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.s95ammar.budgetplanner.models.data.Saving

@Dao
interface SavingDao {

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun insertOrReplace(saving: Saving)

	@Delete
	suspend fun delete(saving: Saving)

	@Query("SELECT * FROM saving WHERE id=:id")
	fun getSavingById(id: Int): LiveData<Saving>

	@Query("SELECT * FROM saving WHERE savings_jar_id=:categoryStatusId")
	fun getSavingsBy(categoryStatusId: Int): LiveData<List<Saving>>

}