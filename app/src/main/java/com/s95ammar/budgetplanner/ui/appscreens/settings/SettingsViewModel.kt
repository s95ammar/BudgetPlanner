package com.s95ammar.budgetplanner.ui.appscreens.settings

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.s95ammar.budgetplanner.models.repository.LocalRepository
import com.s95ammar.budgetplanner.util.lifecycleutil.EventMutableLiveDataVoid

class SettingsViewModel @ViewModelInject constructor(
    private val localRepository: LocalRepository
) : ViewModel() {

    private val _navigateToLogin = EventMutableLiveDataVoid()

    val navigateToLogin = _navigateToLogin.asEventLiveData()

    fun logout() {
        localRepository.clearAuthToken()
        _navigateToLogin.call()
    }

}