package com.s95ammar.budgetplanner.ui.base

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.s95ammar.budgetplanner.R
import com.s95ammar.budgetplanner.logFromHere
import com.s95ammar.budgetplanner.ui.common.KeyboardManager
import com.s95ammar.budgetplanner.ui.common.loading.LoadingManager
import com.s95ammar.budgetplanner.ui.main.MainViewModel

abstract class BaseFragment : Fragment, LoadingManager {

    constructor() : super()
    constructor(@LayoutRes layoutResId: Int) : super(layoutResId)

    protected val navController by lazy { findNavController() }
    protected val activityViewModel: MainViewModel by activityViewModels()

    private var loadingManager: LoadingManager? = null
    private var keyboardManager: KeyboardManager? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        loadingManager = context as? LoadingManager
        keyboardManager = context as? KeyboardManager
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViews()
        initObservers()
    }

    open fun setUpViews() {}

    open fun initObservers() {}

    override fun showLoading() {
        loadingManager?.showLoading()
    }

    override fun hideLoading() {
        loadingManager?.hideLoading()
    }

    fun showKeyboard() = keyboardManager?.showKeyboard()

    fun hideKeyboard() = keyboardManager?.hideKeyboard()

    fun showSnackbar(@StringRes messageResId: Int) {
        Snackbar.make(requireView(), messageResId, Snackbar.LENGTH_SHORT).show()
    }

    fun showSnackbar(message: String) {
        Snackbar.make(requireView(), message, Snackbar.LENGTH_SHORT).show()
    }

    fun showErrorSnackbar(throwable: Throwable) {
        Snackbar.make(requireView(), getString(getErrorStringId(throwable)), Snackbar.LENGTH_SHORT).show()
    }

    private fun getErrorStringId(throwable: Throwable) = when (throwable) {
        else -> R.string.error_occurred
    }

    protected fun displayErrorDialog(throwable: Throwable) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.error_something_went_wrong_title)
            .setMessage(getString(R.string.format_error_something_went_wrong_desc, throwable.message))
            .setPositiveButton(android.R.string.ok) { dialog, _ -> dialog.dismiss() }
            .show()
    }

    protected fun displayDeleteConfirmationDialog(deletedItemTitle: String, onConfirmed: () -> Unit) {
        displayConfirmationDialog(
            message = getString(R.string.format_delete_confirmation, deletedItemTitle),
            onConfirmed = onConfirmed
        )
    }

    protected fun displayConfirmationDialog(
        message: String,
        isCancelable: Boolean = false,
        title: String = getString(R.string.are_you_sure),
        icon: Drawable? = ContextCompat.getDrawable(requireContext(), R.drawable.ic_info),
        negativeButtonText: String = getString(R.string.no),
        positiveButtonText: String = getString(R.string.yes),
        onDismissed: (() -> Unit)? = null,
        onConfirmed: () -> Unit
    ) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(title)
            .setIcon(icon)
            .setMessage(message)
            .setPositiveButton(positiveButtonText) { _, _ -> onConfirmed() }
            .setCancelable(isCancelable)
            .setNegativeButton(negativeButtonText, onDismissed?.let { { _, _ -> it() } })
            .show()
    }

    protected inline fun executeIfViewIsAvailable(action: (View) -> Unit) {
        try {
            view?.let { action.invoke(it) }
        } catch (e: Throwable) {
            logFromHere(e)
        }
    }

}