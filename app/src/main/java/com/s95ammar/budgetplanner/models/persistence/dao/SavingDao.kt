package com.s95ammar.budgetplanner.models.persistence.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.s95ammar.budgetplanner.models.data.Saving
import com.s95ammar.budgetplanner.models.persistence.dao.base.BaseDao

@Dao
interface SavingDao: BaseDao<Saving> {

	@Query("DELETE FROM saving")
	suspend fun deleteAllSavings()

	@Query("SELECT * FROM saving WHERE id=:id")
	fun getSavingById(id: Int): LiveData<Saving>

	@Query("SELECT * FROM saving WHERE savings_jar_id=:categoryStatusId")
	fun getSavingsBy(categoryStatusId: Int): LiveData<List<Saving>>

}