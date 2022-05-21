package com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.budgettransactionsmap.data

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem
import com.s95ammar.budgetplanner.models.BaseEntityMapper
import com.s95ammar.budgetplanner.models.datasource.local.db.entity.join.BudgetTransactionJoinEntity

data class BudgetTransactionClusterItem(
    val name: String,
    val latLng: LatLng,
    val amount: Double,
    val currencyCode: String,
    val creationUnixMs: Long,
    val categoryName: String
) : ClusterItem {

    override fun getPosition(): LatLng {
        return latLng
    }

    override fun getTitle(): String {
        return "$amount"
    }

    override fun getSnippet(): String {
        return name
    }

    object EntityMapper : BaseEntityMapper<BudgetTransactionClusterItem, BudgetTransactionJoinEntity> {
        override fun fromEntity(entity: BudgetTransactionJoinEntity?): BudgetTransactionClusterItem? {
            return entity?.let {
                BudgetTransactionClusterItem(
                    entity.name,
                    entity.latLng?.let { (lat, lng) -> LatLng(lat, lng) } ?: return null,
                    entity.amount,
                    entity.currencyCode,
                    entity.creationUnixMs,
                    entity.categoryName
                )
            }
        }
    }
}
