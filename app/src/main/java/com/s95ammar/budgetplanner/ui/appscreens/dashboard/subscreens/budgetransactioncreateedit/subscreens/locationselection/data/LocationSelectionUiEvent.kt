package com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.budgetransactioncreateedit.subscreens.locationselection.data

sealed class LocationSelectionUiEvent {
    class SetResult(val location: LocationWithAddress?): LocationSelectionUiEvent()
    object Exit: LocationSelectionUiEvent()
}
