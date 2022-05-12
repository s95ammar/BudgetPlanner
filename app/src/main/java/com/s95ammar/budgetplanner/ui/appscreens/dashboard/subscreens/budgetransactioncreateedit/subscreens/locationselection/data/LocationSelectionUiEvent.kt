package com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.budgetransactioncreateedit.subscreens.locationselection.data

import com.google.android.gms.maps.model.LatLng

sealed class LocationSelectionUiEvent {
    class SetResult(val latLng: LatLng?): LocationSelectionUiEvent()
    object Exit: LocationSelectionUiEvent()
}
