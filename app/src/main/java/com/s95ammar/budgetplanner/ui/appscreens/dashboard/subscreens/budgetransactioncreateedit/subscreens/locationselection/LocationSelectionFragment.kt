package com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.budgetransactioncreateedit.subscreens.locationselection

import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.s95ammar.budgetplanner.R
import com.s95ammar.budgetplanner.databinding.LocationSelectionFragmentBinding
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.budgetransactioncreateedit.subscreens.locationselection.data.LocationSelectionUiEvent
import com.s95ammar.budgetplanner.ui.common.Keys
import com.s95ammar.budgetplanner.ui.common.viewbinding.BaseViewBinderFragment
import com.s95ammar.budgetplanner.util.Optional
import com.s95ammar.budgetplanner.util.lifecycleutil.observeEvent


class LocationSelectionFragment : BaseViewBinderFragment<LocationSelectionFragmentBinding>(
    R.layout.location_selection_fragment
), OnMapReadyCallback {

    companion object {
        const val GOOGLE_MAPS_DEFAULT_ZOOM = 18f
    }

    private val viewModel: LocationSelectionViewModel by viewModels()
    private lateinit var googleMap: GoogleMap
    private var selectedLocationMarker: Marker? = null

    override fun initViewBinding(view: View): LocationSelectionFragmentBinding {
        return LocationSelectionFragmentBinding.bind(view)
    }

    override fun setUpViews() {
        binding.toolbar.setNavigationOnClickListener { viewModel.onBack() }
        (childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?)?.getMapAsync(this)
        binding.buttonRemove.setOnClickListener { viewModel.onRemoveLocation() }
        binding.buttonApply.setOnClickListener { viewModel.onConfirmLocation() }
    }

    override fun initObservers() {
        viewModel.performUiEvent.observeEvent(viewLifecycleOwner) { performUiEvent(it) }
        viewModel.latLngOptional.observe(viewLifecycleOwner) { setUpRemoveButton(it) }
    }

    private fun setUpRemoveButton(optionalLatLng: Optional<LatLng>) {
        binding.buttonRemove.isEnabled = optionalLatLng.isNotEmpty
    }

    override fun onMapReady(map: GoogleMap?) {
        map?.let {
            googleMap = it
            viewModel.latLngOptional.observe(viewLifecycleOwner) { latLngOptional ->
                latLngOptional?.value?.let { latLng ->
                    redrawSelectedLocationMarker(latLng)
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, GOOGLE_MAPS_DEFAULT_ZOOM))
                } ?: removeSelectedLocationMarkerIfExists()
            }
            googleMap.setOnMapLongClickListener { latLng ->
                viewModel.setLocation(latLng)
            }
        }
    }

    private fun redrawSelectedLocationMarker(latLng: LatLng) {
        removeSelectedLocationMarkerIfExists()
        selectedLocationMarker = googleMap.addMarker(MarkerOptions().position(latLng))
    }

    private fun removeSelectedLocationMarkerIfExists() {
        selectedLocationMarker?.remove()
        selectedLocationMarker = null
    }

    private fun performUiEvent(uiEvent: LocationSelectionUiEvent) {
        when (uiEvent) {
            is LocationSelectionUiEvent.Exit -> navController.navigateUp()
            is LocationSelectionUiEvent.SetResult -> setResult(uiEvent.latLng)
        }
    }

    private fun setResult(latLng: LatLng?) {
        setFragmentResult(Keys.KEY_LOCATION_REQUEST, bundleOf(Keys.KEY_LOCATION to latLng))
    }
}