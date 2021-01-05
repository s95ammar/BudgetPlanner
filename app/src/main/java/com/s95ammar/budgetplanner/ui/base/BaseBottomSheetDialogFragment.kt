package com.s95ammar.budgetplanner.ui.base

import android.os.Bundle
import android.view.View
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.s95ammar.budgetplanner.ui.common.viewbinding.ViewBinder
import com.s95ammar.budgetplanner.ui.common.viewbinding.ViewBindingException

abstract class BaseBottomSheetDialogFragment : BottomSheetDialogFragment() {

    private var _binding: ViewBinding? = null

    internal inline fun <reified VB: ViewBinding> getBinding(): VB {
        val binding = _binding ?: throw ViewBindingException(ViewBindingException.MESSAGE_INAPPROPRIATE_LIFECYCLE_STATE)
        return binding as VB
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (this is ViewBinder<*>) _binding = initViewBinding(view)
        setUpViews()
        initObservers()
    }

    open fun setUpViews() {}

    open fun initObservers() {}

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

}