package com.s95ammar.budgetplanner.models.persistence.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.s95ammar.budgetplanner.models.data.Saving

@Dao
interface SavingDao {

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun insertOrReplaceSaving(saving: Saving)

	@Delete
	suspend fun deleteSaving(saving: Saving)

	@Query("SELECT * FROM saving WHERE id=:id")
	suspend fun getSavingById(id: Int): Saving

	@Query("SELECT * FROM saving WHERE id=:id")
	fun getSavingByIdLiveData(id: Int): LiveData<Saving>

	@Query("SELECT * FROM saving WHERE savings_jar_id=:categoryStatusId")
	fun getSavingsByLiveData(categoryStatusId: Int): LiveData<List<Saving>>

}