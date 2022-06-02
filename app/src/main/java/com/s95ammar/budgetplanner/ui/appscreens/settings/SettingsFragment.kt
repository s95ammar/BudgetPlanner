package com.s95ammar.budgetplanner.ui.appscreens.settings

import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import com.s95ammar.budgetplanner.R
import com.s95ammar.budgetplanner.databinding.FragmentSettingsBinding
import com.s95ammar.budgetplanner.ui.appscreens.settings.data.SettingsItem
import com.s95ammar.budgetplanner.ui.appscreens.settings.data.SettingsItemViewEntity
import com.s95ammar.budgetplanner.ui.appscreens.settings.data.SettingsUiEvent
import com.s95ammar.budgetplanner.ui.common.simplemenu.SimpleMenuAdapter
import com.s95ammar.budgetplanner.ui.common.viewbinding.BaseViewBinderFragment
import com.s95ammar.budgetplanner.util.lifecycleutil.observeEvent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : BaseViewBinderFragment<FragmentSettingsBinding>(R.layout.fragment_settings) {

    private val viewModel: SettingsViewModel by viewModels()

    private val adapter by lazy { SimpleMenuAdapter(viewModel::onSettingsItemClick, onItemLongClick = null) }

    override fun initViewBinding(view: View): FragmentSettingsBinding {
        return FragmentSettingsBinding.bind(view)
    }

    override fun setUpViews() {
        super.setUpViews()
        binding.recyclerView.adapter = adapter
        binding.recyclerView.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
    }

    override fun initObservers() {
        super.initObservers()
        viewModel.settingsList.observe(viewLifecycleOwner) { setSettingsList(it) }
        viewModel.performUiEvent.observeEvent(viewLifecycleOwner) { performUiEvent(it) }
    }

    private fun setSettingsList(settingsList: List<SettingsItem>) {
        adapter.submitList(settingsList.map { SettingsItemViewEntity(title = getString(it.titleResId), id = it.id) })
    }

    private fun performUiEvent(uiEvent: SettingsUiEvent) {
        when (uiEvent) {
            is SettingsUiEvent.NavigateToMainCurrencySelection -> {
                activityViewModel.onNavigateToMainCurrencySelection()
            }
        }
    }
}
