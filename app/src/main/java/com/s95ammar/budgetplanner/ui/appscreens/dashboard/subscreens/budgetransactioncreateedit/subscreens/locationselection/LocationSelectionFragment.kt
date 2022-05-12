package com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.budgetransactioncreateedit.subscreens.locationselection

import android.view.Gravity
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import com.google.android.gms.maps.CameraUpdate
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
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.budgetransactioncreateedit.subscreens.locationselection.data.LocationUpdate
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
        const val GOOGLE_MAPS_DEFAULT_ZOOM = 16f
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
        viewModel.locationUpdateOptional.observe(viewLifecycleOwner) {
            setUpViewsByLocationAvailability(it)
            setAddress(it)
        }
    }

    private fun setAddress(locationUpdateOptional: Optional<LocationUpdate>) {
        val isLocationAvailable = locationUpdateOptional.isNotEmpty
        binding.textViewLocationValue.text = if (isLocationAvailable) {
            locationUpdateOptional.value?.locationWithAddress?.address ?: getString(R.string.unknown_location)
        } else {
            getString(R.string.no_location_selected)
        }
    }

    private fun setUpViewsByLocationAvailability(locationUpdateOptional: Optional<LocationUpdate>) {
        binding.buttonRemove.isVisible = locationUpdateOptional.isNotEmpty
        binding.textViewLocationValue.gravity = if (locationUpdateOptional.isEmpty) Gravity.CENTER else Gravity.START
    }

    override fun onMapReady(map: GoogleMap?) {
        map?.let {
            googleMap = it
            viewModel.locationUpdateOptional.observe(viewLifecycleOwner) { locationOptional ->
                locationOptional?.value?.let { locationUpdate ->
                    redrawSelectedLocationMarker(locationUpdate.locationWithAddress.latLng)
                    googleMap.animateCamera(getCameraUpdateForLocationUpdate(locationUpdate))
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

    private fun getCameraUpdateForLocationUpdate(locationUpdate: LocationUpdate): CameraUpdate {
        return if (locationUpdate.isFirstUpdate) {
            CameraUpdateFactory.newLatLngZoom(locationUpdate.locationWithAddress.latLng, GOOGLE_MAPS_DEFAULT_ZOOM)
        } else {
            CameraUpdateFactory.newLatLng(locationUpdate.locationWithAddress.latLng)
        }
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