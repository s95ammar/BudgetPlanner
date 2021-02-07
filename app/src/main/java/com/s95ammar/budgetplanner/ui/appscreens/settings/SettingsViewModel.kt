package com.s95ammar.budgetplanner.ui.appscreens.settings

import androidx.lifecycle.ViewModel
import com.s95ammar.budgetplanner.models.repository.AuthRepository
import com.s95ammar.budgetplanner.ui.appscreens.settings.data.SettingsUiEvent
import com.s95ammar.budgetplanner.util.lifecycleutil.EventMutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    private val _performUiEvent = EventMutableLiveData<SettingsUiEvent>()

    val performUiEvent = _performUiEvent.asEventLiveData()

    fun onDisplayLogoutConfirmationDialog() {
        _performUiEvent.call(SettingsUiEvent.DisplayLogoutConfirmationDialog)
    }

    fun logout() {
        repository.clearAuthToken()
        _performUiEvent.call(SettingsUiEvent.NavigateToLogin)
    }

}