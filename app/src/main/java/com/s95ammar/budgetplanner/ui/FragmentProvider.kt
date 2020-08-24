package com.s95ammar.budgetplanner.ui

import androidx.fragment.app.Fragment

interface FragmentProvider {
    fun provideFragment(): Fragment
}

fun fragmentProvider(fragmentProvider: () -> Fragment): FragmentProvider {
    return object : FragmentProvider {
        override fun provideFragment(): Fragment {
            return fragmentProvider.invoke()
        }
    }
}