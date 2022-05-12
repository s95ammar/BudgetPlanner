package com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.budgetransactioncreateedit.subscreens.locationselection

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.s95ammar.budgetplanner.util.Optional
import com.s95ammar.budgetplanner.util.asOptional
import com.s95ammar.budgetplanner.util.lifecycleutil.EventMutableLiveData
import com.s95ammar.budgetplanner.util.lifecycleutil.MediatorLiveData
import com.s95ammar.budgetplanner.util.lifecycleutil.asLiveData
import javax.inject.Inject
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.budgetransactioncreateedit.subscreens.locationselection.LocationSelectionFragmentArgs as FragmentArgs
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.budgetransactioncreateedit.subscreens.locationselection.data.LocationSelectionUiEvent as UiEvent

class LocationSelectionViewModel @Inject constructor(
    val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _latLngOptional = MediatorLiveData(
        savedStateHandle.get<LatLng>(FragmentArgs::currentLatLng.name).asOptional()
    ).apply {
        addSource(this) { savedStateHandle.set(FragmentArgs::currentLatLng.name, it.value) }
    }

    private val _performUiEvent = EventMutableLiveData<UiEvent>()

    val latLngOptional = _latLngOptional.asLiveData()
    val performUiEvent = _performUiEvent.asEventLiveData()

    fun setLocation(latLng: LatLng?) {
        _latLngOptional.value = latLng.asOptional()
    }

    fun onRemoveLocation() {
        _latLngOptional.value = Optional.empty()
    }

    fun onConfirmLocation() {
        _performUiEvent.call(UiEvent.SetResult(_latLngOptional.value?.value))
        _performUiEvent.call(UiEvent.Exit)
    }

    fun onBack() {
        _performUiEvent.call(UiEvent.Exit)
    }
}