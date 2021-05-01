package com.s95ammar.budgetplanner.ui.common.viewbinding

import android.view.View
import androidx.viewbinding.ViewBinding

@Deprecated(
    message = "Extend from BaseViewBinderFragment instead of implementing this interface",
    replaceWith = ReplaceWith("BaseViewBinderFragment<FragmentBinding>", "com.s95ammar.budgetplanner.ui.common.viewbinding.BaseViewBinderFragment")
)
interface ViewBinder<VB : ViewBinding> {

    val binding: VB
    fun initViewBinding(view: View): VB
}