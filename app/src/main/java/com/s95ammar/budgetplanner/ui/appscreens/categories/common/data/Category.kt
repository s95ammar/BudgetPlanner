package com.s95ammar.budgetplanner.ui.appscreens.categories.common.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Category(
    val id: Int,
    val name: String
): Parcelable