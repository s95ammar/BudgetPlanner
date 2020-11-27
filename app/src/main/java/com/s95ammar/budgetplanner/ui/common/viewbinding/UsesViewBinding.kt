package com.s95ammar.budgetplanner.ui.common.viewbinding

import android.view.View
import androidx.viewbinding.ViewBinding

interface UsesViewBinding<VB : ViewBinding> {

    val binding: VB
    fun initViewBinding(view: View): VB
}