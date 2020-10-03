package com.s95ammar.budgetplanner.ui

import androidx.fragment.app.Fragment

fun interface FragmentProvider {
    fun provideFragment(): Fragment
}