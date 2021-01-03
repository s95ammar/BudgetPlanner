package com.s95ammar.budgetplanner.ui.appscreens.settings

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.s95ammar.budgetplanner.models.repository.LocalRepository
import com.s95ammar.budgetplanner.ui.appscreens.settings.data.SettingsUiEvent
import com.s95ammar.budgetplanner.util.lifecycleutil.EventMutableLiveData

class SettingsViewModel @ViewModelInject constructor(
    private val localRepository: LocalRepository
) : ViewModel() {

    private val _performUiEvent = EventMutableLiveData<SettingsUiEvent>()

    val performUiEvent = _performUiEvent.asEventLiveData()

    fun onDisplayLogoutConfirmationDialog() {
        _performUiEvent.call(SettingsUiEvent.DisplayLogoutConfirmationDialog)
    }

    fun logout() {
        localRepository.clearAuthToken()
        _performUiEvent.call(SettingsUiEvent.NavigateToLogin)
    }

}