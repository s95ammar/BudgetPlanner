package com.s95ammar.budgetplanner.ui.base

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.s95ammar.budgetplanner.R
import com.s95ammar.budgetplanner.models.api.responses.errors.InternalServerError
import com.s95ammar.budgetplanner.ui.common.KeyboardManager
import com.s95ammar.budgetplanner.ui.common.viewbinding.ViewBindingException
import com.s95ammar.budgetplanner.ui.common.loading.LoadingManager
import com.s95ammar.budgetplanner.ui.common.viewbinding.ViewBinder


abstract class BaseFragment : Fragment {

    constructor() : super()
    constructor(@LayoutRes layoutResId: Int) : super(layoutResId)

    val navController by lazy { findNavController() }
    private var _binding: ViewBinding? = null

    private var loadingManager: LoadingManager? = null
    private var keyboardManager: KeyboardManager? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        loadingManager = context as? LoadingManager
        keyboardManager = context as? KeyboardManager
    }

    internal inline fun <reified VB: ViewBinding> getBinding(): VB {
        _binding?.let { binding ->
            return binding as VB
        }
        throw ViewBindingException("binding is not initialized or is accessed from outside of the fragment's view lifecycle")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (this is ViewBinder<*>) _binding = initViewBinding(view)
        setUpViews()
        initObservers()
    }

    open fun setUpViews() {}

    open fun initObservers() {}

    open fun showLoading() = loadingManager?.showLoading()

    open fun hideLoading() = loadingManager?.hideLoading()

    fun showKeyboard() = keyboardManager?.showKeyboard()

    fun hideKeyboard() = keyboardManager?.hideKeyboard()

    fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    fun showErrorToast(throwable: Throwable) {
        Toast.makeText(requireContext(), getString(getErrorStringId(throwable)), Toast.LENGTH_SHORT).show()
    }

    private fun getErrorStringId(throwable: Throwable) = when (throwable) {
        is InternalServerError -> R.string.error_internal_server
        else -> R.string.error_occurred
    }

    protected fun <T> sendResult(key: String, result: T) {
        navController.previousBackStackEntry?.savedStateHandle?.set(key, result)
    }

    protected fun <T> observeResultLiveData(key: String, onResultReceived: (T) -> Unit) {
        navController.currentBackStackEntry?.savedStateHandle?.getLiveData<T>(key)?.observe(viewLifecycleOwner) { result ->
            onResultReceived(result)
        }
    }

    protected fun displayErrorDialog(throwable: Throwable) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.error_something_went_wrong_title)
            .setMessage(getString(R.string.format_error_something_went_wrong_desc, throwable.message))
            .setPositiveButton(android.R.string.ok) { dialog, _ -> dialog.dismiss() }
            .show()
    }

    protected fun displayDeleteConfirmationDialog(deletedItemTitle: String, onConfirmed: () -> Unit) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.are_you_sure))
            .setIcon(ContextCompat.getDrawable(requireContext(), R.drawable.ic_info))
            .setMessage(getString(R.string.format_delete_confirmation, deletedItemTitle))
            .setPositiveButton(R.string.yes) { _, _ -> onConfirmed() }
            .setCancelable(false)
            .setNegativeButton(R.string.no, null)
            .show()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}