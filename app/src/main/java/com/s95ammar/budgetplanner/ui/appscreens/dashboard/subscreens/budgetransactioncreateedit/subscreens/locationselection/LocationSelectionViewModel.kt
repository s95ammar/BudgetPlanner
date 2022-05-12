package com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.budgetransactioncreateedit.subscreens.locationselection

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.s95ammar.budgetplanner.models.repository.LocationRepository
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.budgetransactioncreateedit.subscreens.locationselection.data.LocationWithAddress
import com.s95ammar.budgetplanner.util.Optional
import com.s95ammar.budgetplanner.util.asOptional
import com.s95ammar.budgetplanner.util.lifecycleutil.EventMutableLiveData
import com.s95ammar.budgetplanner.util.lifecycleutil.MediatorLiveData
import com.s95ammar.budgetplanner.util.lifecycleutil.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.budgetransactioncreateedit.subscreens.locationselection.LocationSelectionFragmentArgs as FragmentArgs
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.budgetransactioncreateedit.subscreens.locationselection.data.LocationSelectionUiEvent as UiEvent

@HiltViewModel
class LocationSelectionViewModel @Inject constructor(
    val savedStateHandle: SavedStateHandle,
    val repository: LocationRepository
) : ViewModel() {

    private val _locationOptional = MediatorLiveData(
        savedStateHandle.get<LocationWithAddress>(FragmentArgs::currentLocation.name).asOptional()
    ).apply {
        addSource(this) { savedStateHandle.set(FragmentArgs::currentLocation.name, it.value) }
    }

    private val _performUiEvent = EventMutableLiveData<UiEvent>()

    val locationOptional = _locationOptional.asLiveData()
    val performUiEvent = _performUiEvent.asEventLiveData()

    fun setLocationByCoordinates(latLng: LatLng?) {
        if (latLng == null) {
            _locationOptional.value = Optional.empty()
        } else {
            viewModelScope.launch {
                val address = repository.getAddressByCoordinates(latLng.latitude, latLng.longitude)
                _locationOptional.value = LocationWithAddress(latLng, address).asOptional()
            }
        }
    }

    fun onRemoveLocation() {
        _locationOptional.value = Optional.empty()
    }

    fun onConfirmLocation() {
        _performUiEvent.call(UiEvent.SetResult(_locationOptional.value?.value))
        _performUiEvent.call(UiEvent.Exit)
    }

    fun onBack() {
        _performUiEvent.call(UiEvent.Exit)
    }
}