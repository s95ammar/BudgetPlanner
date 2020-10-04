package com.s95ammar.budgetplanner.ui.base

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.s95ammar.budgetplanner.R
import com.s95ammar.budgetplanner.ui.common.loading.LoadingManager


abstract class BaseFragment : Fragment {

    constructor() : super()
    constructor(@LayoutRes layoutResId: Int) : super(layoutResId)

    val navController by lazy { findNavController() }

    protected var loadingManager: LoadingManager? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        loadingManager = context as? LoadingManager
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViews()
        initObservers()
    }

    open fun setUpViews() {}

    open fun initObservers() {}

    fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    fun showToast(throwable: Throwable) {
        Toast.makeText(requireContext(), throwable.message, Toast.LENGTH_SHORT).show()
    }

    protected fun displayError(throwable: Throwable) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.error_something_went_wrong_title)
            .setMessage(getString(R.string.format_error_something_went_wrong_desc, throwable.message))
            .setPositiveButton(android.R.string.ok) { dialog, _ -> dialog.dismiss()}
            .show()
    }

}