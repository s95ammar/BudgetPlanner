package com.s95ammar.budgetplanner.ui.appscreens.settings.data

sealed class SettingsUiEvent {
    object NavigateToLogin : SettingsUiEvent()
    object DisplayLogoutConfirmationDialog: SettingsUiEvent()
}
