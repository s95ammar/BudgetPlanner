package com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.budgettransactionsmap.map

import android.content.Context
import android.graphics.Color
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer
import com.s95ammar.budgetplanner.models.IntBudgetTransactionType
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.budgettransactionsmap.data.BudgetTransactionClusterItem
import com.s95ammar.budgetplanner.util.COLOR_PRIMARY_HEX

class BudgetTransactionClusterRenderer(
    applicationContext: Context,
    map: GoogleMap,
    clusterManager: ClusterManager<BudgetTransactionClusterItem>
) : DefaultClusterRenderer<BudgetTransactionClusterItem>(applicationContext, map, clusterManager) {

    override fun onBeforeClusterItemRendered(item: BudgetTransactionClusterItem, markerOptions: MarkerOptions) {
        markerOptions.icon(getMarkerIconDescriptor(item))
    }

    override fun onBeforeClusterRendered(cluster: Cluster<BudgetTransactionClusterItem>, markerOptions: MarkerOptions) {
        super.onBeforeClusterRendered(cluster, markerOptions)
    }

    override fun onClusterItemUpdated(item: BudgetTransactionClusterItem, marker: Marker) {
        marker.setIcon(getMarkerIconDescriptor(item))
    }

    override fun shouldRenderAsCluster(cluster: Cluster<BudgetTransactionClusterItem>): Boolean {
        return cluster.size > 1
    }

    override fun onClusterUpdated(cluster: Cluster<BudgetTransactionClusterItem>, marker: Marker) {
        super.onClusterUpdated(cluster, marker)
    }

    private fun getMarkerIconDescriptor(item: BudgetTransactionClusterItem): BitmapDescriptor {
        return BitmapDescriptorFactory.defaultMarker(
            if (item.type == IntBudgetTransactionType.INCOME) {
                BitmapDescriptorFactory.HUE_GREEN
            } else {
                BitmapDescriptorFactory.HUE_RED
            }
        )
    }

    override fun getColor(clusterSize: Int): Int {
        return Color.parseColor(COLOR_PRIMARY_HEX)
    }
}