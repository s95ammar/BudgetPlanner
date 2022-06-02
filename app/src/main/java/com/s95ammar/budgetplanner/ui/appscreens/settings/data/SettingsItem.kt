package com.s95ammar.budgetplanner.ui.appscreens.settings.data

import androidx.annotation.StringRes

data class SettingsItem(
    @StringRes val titleResId: Int
) {
    val id
        get() = titleResId
}
