package com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.budgettransactionsmap

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.clustering.ClusterManager
import com.s95ammar.budgetplanner.R
import com.s95ammar.budgetplanner.databinding.BudgetTransactionsMapFragmentBinding
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.budgettransactionsmap.data.BudgetTransactionClusterItem
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.budgettransactionsmap.map.BudgetTransactionClusterInfoWindowAdapter
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.budgettransactionsmap.map.BudgetTransactionClusterRenderer
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.budgettransactionsmap.map.BudgetTransactionInfoWindowAdapter
import com.s95ammar.budgetplanner.ui.common.viewbinding.BaseViewBinderFragment
import com.s95ammar.budgetplanner.util.LAT_LNG_BOUNDS_PADDING
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BudgetTransactionsMapFragment : BaseViewBinderFragment<BudgetTransactionsMapFragmentBinding>(
    R.layout.budget_transactions_map_fragment
), OnMapReadyCallback {

    private val viewModel: BudgetTransactionsMapViewModel by viewModels()
    private lateinit var clusterManager: ClusterManager<BudgetTransactionClusterItem>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?)?.getMapAsync(this)
    }

    override fun initViewBinding(view: View): BudgetTransactionsMapFragmentBinding {
        return BudgetTransactionsMapFragmentBinding.bind(view)
    }

    override fun setUpViews() {
        binding.toolbar.setNavigationOnClickListener { navController.navigateUp() }
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        googleMap?.let { map ->
            setUpClusterManager(map)
            initMapObservers(map)
        }
    }

    private fun initMapObservers(map: GoogleMap) {
        viewModel.budgetTransactionClusterItems.observe(viewLifecycleOwner) {
            setClusterItems(it, map)
        }
    }

    private fun setClusterItems(clusterItems: List<BudgetTransactionClusterItem>, map: GoogleMap) {
        clusterManager.addItems(clusterItems)
        zoomIntoClusterItems(clusterItems, map)
    }

    private fun setUpClusterManager(map: GoogleMap) {
        clusterManager = ClusterManager(requireContext(), map)
        val itemInfoWindowAdapter = BudgetTransactionInfoWindowAdapter(requireContext())
        val clusterInfoWindowAdapter = BudgetTransactionClusterInfoWindowAdapter(requireContext())
        val clusterRenderer = BudgetTransactionClusterRenderer(requireContext().applicationContext, map, clusterManager)
        clusterManager.renderer = clusterRenderer
        clusterManager.markerCollection.setInfoWindowAdapter(itemInfoWindowAdapter)
        clusterManager.clusterMarkerCollection.setInfoWindowAdapter(clusterInfoWindowAdapter)
        clusterManager.setOnClusterItemClickListener { item ->
            itemInfoWindowAdapter.updateWindowForItem(item)
            return@setOnClusterItemClickListener false
        }
        clusterManager.setOnClusterClickListener { cluster ->
            clusterInfoWindowAdapter.updateWindowForItem(cluster)
            return@setOnClusterClickListener false
        }
        clusterManager.clusterMarkerCollection.setOnInfoWindowClickListener { marker ->
            zoomIntoClusterItems(clusterRenderer.getCluster(marker).items, map)
        }
        map.setOnCameraIdleListener(clusterManager)
    }

    private fun zoomIntoClusterItems(clusterItems: Collection<BudgetTransactionClusterItem>, map: GoogleMap) {
        val bounds = LatLngBounds.builder().apply {
            clusterItems.forEach { include(it.position) }
        }.build()
        map.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, LAT_LNG_BOUNDS_PADDING))
    }
}