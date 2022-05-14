package com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.budgettransactionsmap

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.maps.android.clustering.ClusterManager
import com.s95ammar.budgetplanner.R
import com.s95ammar.budgetplanner.databinding.BudgetTransactionsMapFragmentBinding
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.budgettransactionsmap.data.BudgetTransactionClusterItem
import com.s95ammar.budgetplanner.ui.common.viewbinding.BaseViewBinderFragment
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
            initMapObservers()
        }
    }

    private fun initMapObservers() {
        viewModel.budgetTransactionClusterItems.observe(viewLifecycleOwner) { setClusterItems(it) }
    }

    private fun setClusterItems(clusterItems: List<BudgetTransactionClusterItem>) {
        clusterManager.addItems(clusterItems)
    }

    private fun setUpClusterManager(map: GoogleMap) {
        clusterManager = ClusterManager(requireContext(), map)
        map.setOnCameraIdleListener(clusterManager)
    }
}