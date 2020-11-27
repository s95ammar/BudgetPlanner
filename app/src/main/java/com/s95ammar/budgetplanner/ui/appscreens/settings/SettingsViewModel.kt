package com.s95ammar.budgetplanner.ui.appscreens.settings

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.s95ammar.budgetplanner.models.repository.LocalRepository

class SettingsViewModel @ViewModelInject constructor(
    private val localRepository: LocalRepository
) : ViewModel()