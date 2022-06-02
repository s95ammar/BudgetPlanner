package com.s95ammar.budgetplanner.ui.appscreens.settings

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.s95ammar.budgetplanner.R
import com.s95ammar.budgetplanner.ui.appscreens.settings.data.SettingsItem
import com.s95ammar.budgetplanner.ui.appscreens.settings.data.SettingsUiEvent
import com.s95ammar.budgetplanner.util.lifecycleutil.EventMutableLiveData
import com.s95ammar.budgetplanner.util.lifecycleutil.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor() : ViewModel() {

    private val _settingsList = MutableLiveData(createSettingsList())
    private val _performUiEvent = EventMutableLiveData<SettingsUiEvent>()

    val settingsList = _settingsList.asLiveData()
    val performUiEvent = _performUiEvent.asEventLiveData()

    fun onSettingsItemClick(index: Int) {
        _settingsList.value.orEmpty().getOrNull(index)?.let {
            when (it.id) {
                R.string.main_currency -> {
                    _performUiEvent.call(SettingsUiEvent.NavigateToMainCurrencySelection)
                }
            }
        }
    }

    private fun createSettingsList(): List<SettingsItem> {
        return listOf(
            SettingsItem(R.string.main_currency)
        )
    }
}
