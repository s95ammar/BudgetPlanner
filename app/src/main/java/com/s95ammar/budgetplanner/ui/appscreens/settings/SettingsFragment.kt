package com.s95ammar.budgetplanner.ui.appscreens.settings

import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.s95ammar.budgetplanner.R
import com.s95ammar.budgetplanner.databinding.FragmentSettingsBinding
import com.s95ammar.budgetplanner.ui.appscreens.settings.data.SettingsUiEvent
import com.s95ammar.budgetplanner.ui.base.BaseFragment
import com.s95ammar.budgetplanner.ui.common.viewbinding.ViewBinder
import com.s95ammar.budgetplanner.util.lifecycleutil.observeEvent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : BaseFragment(R.layout.fragment_settings), ViewBinder<FragmentSettingsBinding> {

    override val binding: FragmentSettingsBinding
        get() = getBinding()

    private val viewModel: SettingsViewModel by viewModels()

    override fun initViewBinding(view: View): FragmentSettingsBinding {
        return FragmentSettingsBinding.bind(view)
    }

    override fun setUpViews() {
        super.setUpViews()
        binding.textViewLogout.setOnClickListener { viewModel.onDisplayLogoutConfirmationDialog() }
    }

    override fun initObservers() {
        super.initObservers()
        viewModel.performUiEvent.observeEvent(viewLifecycleOwner) { performUiEvent(it) }
    }

    private fun performUiEvent(uiEvent: SettingsUiEvent) {
        when (uiEvent) {
            is SettingsUiEvent.NavigateToLogin -> navigateToLogin()
            is SettingsUiEvent.DisplayLogoutConfirmationDialog -> displayLogoutConfirmationDialog { viewModel.logout() }
        }
    }

    private fun navigateToLogin() {
        navController.navigate(SettingsFragmentDirections.actionNavigationSettingsToLoginFragment())
    }

    private fun displayLogoutConfirmationDialog(onConfirmed: () -> Unit) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.are_you_sure))
            .setIcon(ContextCompat.getDrawable(requireContext(), R.drawable.ic_info))
            .setMessage(getString(R.string.logout_confirmation))
            .setPositiveButton(R.string.yes) { _, _ -> onConfirmed() }
            .setCancelable(false)
            .setNegativeButton(R.string.no, null)
            .show()
    }

}