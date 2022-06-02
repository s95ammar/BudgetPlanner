package com.s95ammar.budgetplanner.ui.appscreens.settings.data

import android.os.Parcelable
import com.s95ammar.budgetplanner.ui.common.simplemenu.SimpleMenuItem
import kotlinx.parcelize.Parcelize

@Parcelize
data class SettingsItemViewEntity(
    val title: String,
    val id: Int
) : Parcelable, SimpleMenuItem {

    override fun getMenuItemTitle(): String {
        return title
    }

    override fun getMenuItemId(): Int {
        return id
    }
}
