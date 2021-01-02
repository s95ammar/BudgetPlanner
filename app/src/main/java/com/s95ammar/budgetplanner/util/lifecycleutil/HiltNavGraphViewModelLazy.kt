package com.s95ammar.budgetplanner.util.lifecycleutil

import androidx.annotation.IdRes
import androidx.annotation.MainThread
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.navigation.navGraphViewModels

@MainThread
inline fun <reified VM : ViewModel> Fragment.hiltNavGraphViewModels(
    @IdRes navGraphId: Int
): Lazy<VM> = navGraphViewModels(navGraphId) { defaultViewModelProviderFactory }