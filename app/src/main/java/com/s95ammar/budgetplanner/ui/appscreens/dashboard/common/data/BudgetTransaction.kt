package com.s95ammar.budgetplanner.ui.appscreens.dashboard.common.data

import com.google.android.gms.maps.model.LatLng
import com.s95ammar.budgetplanner.models.BaseEntityMapper
import com.s95ammar.budgetplanner.models.IntBudgetTransactionType
import com.s95ammar.budgetplanner.models.datasource.local.db.entity.join.BudgetTransactionJoinEntity

data class BudgetTransaction(
    val id: Int,
    val name: String,
    @IntBudgetTransactionType val type: Int,
    val amount: Int,
    val latLng: LatLng?,
    val creationUnixMs: Long,
    val periodId: Int,
    val periodicCategoryId: Int,
    val categoryName: String
) {
    object Mapper : BaseEntityMapper<BudgetTransaction, BudgetTransactionJoinEntity> {

        override fun fromEntity(entity: BudgetTransactionJoinEntity?): BudgetTransaction? {
            return entity?.let {
                BudgetTransaction(
                    it.id,
                    it.name,
                    it.type,
                    it.amount,
                    it.latLng?.let { latLng -> LatLng(latLng.lat, latLng.lng) },
                    it.creationUnixMs,
                    it.periodId,
                    it.periodicCategoryId,
                    it.categoryName
                )
            }
        }

    }
}