package com.s95ammar.budgetplanner.ui.appscreens.dashboard.common.data

import android.os.Parcelable
import com.google.android.gms.maps.model.LatLng
import com.s95ammar.budgetplanner.models.BaseEntityMapper
import com.s95ammar.budgetplanner.models.datasource.local.db.entity.join.BudgetTransactionJoinEntity
import kotlinx.parcelize.Parcelize

@Parcelize
data class BudgetTransaction(
    val id: Int,
    val name: String,
    val amount: Double,
    val currencyCode: String,
    val latLng: LatLng?,
    val creationUnixMs: Long,
    val periodId: Int,
    val categoryOfPeriodId: Int,
    val categoryName: String
) : Parcelable {
    object Mapper : BaseEntityMapper<BudgetTransaction, BudgetTransactionJoinEntity> {

        override fun fromEntity(entity: BudgetTransactionJoinEntity?): BudgetTransaction? {
            return entity?.let {
                BudgetTransaction(
                    it.id,
                    it.name,
                    it.amount,
                    it.currencyCode,
                    it.latLng?.let { latLng -> LatLng(latLng.lat, latLng.lng) },
                    it.creationUnixMs,
                    it.periodId,
                    it.categoryOfPeriodId,
                    it.categoryName
                )
            }
        }

    }
}