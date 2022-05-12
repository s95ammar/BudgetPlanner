package com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.budgetransactioncreateedit.subscreens.locationselection

import android.view.Gravity
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
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
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.budgetransactioncreateedit.subscreens.locationselection.data.LocationWithAddress
import com.s95ammar.budgetplanner.ui.common.Keys
import com.s95ammar.budgetplanner.ui.common.viewbinding.BaseViewBinderFragment
import com.s95ammar.budgetplanner.util.Optional
import com.s95ammar.budgetplanner.util.lifecycleutil.observeEvent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
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
        binding.textViewLocationValue.setOnClickListener { showSnackbar(R.string.choose_location_instruction) }
    }

    override fun initObservers() {
        viewModel.performUiEvent.observeEvent(viewLifecycleOwner) { performUiEvent(it) }
        viewModel.locationOptional.observe(viewLifecycleOwner) {
            setUpViewsByLocationAvailability(it)
            setAddress(it)
        }
    }

    private fun setAddress(locationOptional: Optional<LocationWithAddress>) {
        val isLocationAvailable = locationOptional.isNotEmpty
        binding.textViewLocationValue.text = if (isLocationAvailable) {
            locationOptional.value?.address ?: getString(R.string.unknown_location)
        } else {
            getString(R.string.no_location_selected)
        }
    }

    private fun setUpViewsByLocationAvailability(locationOptional: Optional<LocationWithAddress>) {
        binding.buttonRemove.isVisible = locationOptional.isNotEmpty
        binding.textViewLocationValue.gravity = if (locationOptional.isEmpty) Gravity.CENTER else Gravity.START
    }

    override fun onMapReady(map: GoogleMap?) {
        map?.let {
            googleMap = it
            viewModel.locationOptional.observe(viewLifecycleOwner) { locationOptional ->
                locationOptional?.value?.let { location ->
                    redrawSelectedLocationMarker(location.latLng)
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location.latLng, GOOGLE_MAPS_DEFAULT_ZOOM))
                } ?: removeSelectedLocationMarkerIfExists()
            }
            googleMap.setOnMapLongClickListener { latLng ->
                viewModel.setLocationByCoordinates(latLng)
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
            is LocationSelectionUiEvent.SetResult -> setResult(uiEvent.location)
        }
    }

    private fun setResult(location: LocationWithAddress?) {
        setFragmentResult(Keys.KEY_LOCATION_REQUEST, bundleOf(Keys.KEY_LOCATION to location))
    }
}