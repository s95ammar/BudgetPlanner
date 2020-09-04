package com.s95ammar.budgetplanner.models.persistence.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.s95ammar.budgetplanner.models.data.Saving

@Dao
interface SavingDao {
	@Insert
	suspend fun insert(saving: Saving)

	@Update
	suspend fun update(saving: Saving)

	@Delete
	suspend fun delete(saving: Saving)

	@Query("DELETE FROM saving")
	suspend fun deleteAllSavings()

	@Query("SELECT * FROM saving WHERE id=:id")
	fun getSavingById(id: Int): LiveData<Saving>

	@Query("SELECT * FROM saving WHERE savings_jar_id=:categoryStatusId")
	fun getSavingsBy(categoryStatusId: Int): LiveData<List<Saving>>

}