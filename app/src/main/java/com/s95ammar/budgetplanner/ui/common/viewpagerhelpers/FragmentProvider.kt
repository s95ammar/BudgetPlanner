package com.s95ammar.budgetplanner.ui.common.viewpagerhelpers

import androidx.fragment.app.Fragment

fun interface FragmentProvider {
    fun provideFragment(): Fragment
}