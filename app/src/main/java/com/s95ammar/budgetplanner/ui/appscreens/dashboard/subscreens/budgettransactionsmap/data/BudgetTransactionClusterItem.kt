package com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.budgettransactionsmap.data

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem
import com.s95ammar.budgetplanner.models.IntBudgetTransactionType

data class BudgetTransactionClusterItem(
    val name: String,
    @IntBudgetTransactionType val type: Int,
    val latLng: LatLng,
    val amount: Int
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
}
