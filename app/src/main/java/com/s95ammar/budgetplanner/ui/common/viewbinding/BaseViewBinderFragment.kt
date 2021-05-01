package com.s95ammar.budgetplanner.ui.common.viewbinding

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.viewbinding.ViewBinding
import com.s95ammar.budgetplanner.ui.base.BaseFragment

abstract class BaseViewBinderFragment<VB : ViewBinding> : BaseFragment {

    constructor() : super()
    constructor(@LayoutRes layoutResId: Int) : super(layoutResId)

    private var _binding: VB? = null
    val binding: VB
        get() = _binding ?: throw ViewBindingException()

    abstract fun initViewBinding(view: View): VB

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = initViewBinding(view)
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
