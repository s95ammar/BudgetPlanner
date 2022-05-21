package com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.budgettransactionsmap

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.map
import androidx.lifecycle.switchMap
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.clustering.ClusterManager
import com.s95ammar.budgetplanner.R
import com.s95ammar.budgetplanner.databinding.FragmentBudgetTransactionsMapBinding
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.budgettransactionsmap.data.BudgetTransactionClusterItem
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.budgettransactionsmap.map.BudgetTransactionClusterInfoWindowAdapter
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.budgettransactionsmap.map.BudgetTransactionClusterRenderer
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.budgettransactionsmap.map.BudgetTransactionInfoWindowAdapter
import com.s95ammar.budgetplanner.ui.common.viewbinding.BaseViewBinderFragment
import com.s95ammar.budgetplanner.ui.main.data.CurrencyDetails
import com.s95ammar.budgetplanner.util.LAT_LNG_BOUNDS_PADDING
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BudgetTransactionsMapFragment : BaseViewBinderFragment<FragmentBudgetTransactionsMapBinding>(
    R.layout.fragment_budget_transactions_map
), OnMapReadyCallback {

    private val viewModel: BudgetTransactionsMapViewModel by viewModels()
    private lateinit var clusterManager: ClusterManager<BudgetTransactionClusterItem>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?)?.getMapAsync(this)
    }

    override fun initViewBinding(view: View): FragmentBudgetTransactionsMapBinding {
        return FragmentBudgetTransactionsMapBinding.bind(view)
    }

    override fun setUpViews() {
        binding.toolbar.setNavigationOnClickListener { navController.navigateUp() }
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        googleMap?.let { map ->
            initMapObservers(map)
        }
    }

    private fun initMapObservers(map: GoogleMap) {
        activityViewModel.mainCurrencyDetails.switchMap { mainCurrencyDetails ->
            viewModel.budgetTransactionClusterItems.map { budgetTransactionClusterItems ->
                mainCurrencyDetails to budgetTransactionClusterItems
            }
        }.observe(viewLifecycleOwner) { (mainCurrencyDetails, budgetTransactionClusterItems) ->
            setUpClusterManager(mainCurrencyDetails, map)
            setClusterItems(budgetTransactionClusterItems, map)
        }
    }

    private fun setClusterItems(clusterItems: List<BudgetTransactionClusterItem>, map: GoogleMap) {
        clusterManager.addItems(clusterItems)
        zoomIntoClusterItems(clusterItems, map)
    }

    private fun setUpClusterManager(mainCurrencyDetails: CurrencyDetails, map: GoogleMap) {
        clusterManager = ClusterManager(requireContext(), map)
        val itemInfoWindowAdapter = BudgetTransactionInfoWindowAdapter(requireContext())
        val clusterInfoWindowAdapter = BudgetTransactionClusterInfoWindowAdapter(requireContext(), mainCurrencyDetails)
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
