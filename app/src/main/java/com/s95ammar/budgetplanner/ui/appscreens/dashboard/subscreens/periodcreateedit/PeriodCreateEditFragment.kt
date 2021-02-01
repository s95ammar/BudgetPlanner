package com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.periodcreateedit

import android.os.Bundle
import android.view.View
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.s95ammar.budgetplanner.R
import com.s95ammar.budgetplanner.databinding.FragmentPeriodCreateEditBinding
import com.s95ammar.budgetplanner.models.view.PeriodRecordViewEntity
import com.s95ammar.budgetplanner.ui.appscreens.auth.common.LoadingState
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.periodcreateedit.adapter.PeriodRecordsSelectionAdapter
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.periodcreateedit.data.PeriodCreateEditUiEvent
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.periodcreateedit.data.PeriodInputBundle
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.periodcreateedit.validation.PeriodCreateEditValidator
import com.s95ammar.budgetplanner.ui.base.BaseFragment
import com.s95ammar.budgetplanner.ui.common.CreateEditMode
import com.s95ammar.budgetplanner.ui.common.Keys
import com.s95ammar.budgetplanner.ui.common.validation.ValidationErrors
import com.s95ammar.budgetplanner.ui.common.viewbinding.ViewBinder
import com.s95ammar.budgetplanner.util.inputText
import com.s95ammar.budgetplanner.util.lifecycleutil.observeEvent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PeriodCreateEditFragment : BaseFragment(R.layout.fragment_period_create_edit), ViewBinder<FragmentPeriodCreateEditBinding> {

    private val viewModel: PeriodCreateEditViewModel by viewModels()

    private val adapter by lazy {
        PeriodRecordsSelectionAdapter(viewModel::onPeriodRecordSelectionStateChanged, viewModel::onPeriodRecordMaxChanged)
    }

    override val binding: FragmentPeriodCreateEditBinding
        get() = getBinding()

    override fun initViewBinding(view: View): FragmentPeriodCreateEditBinding {
        return FragmentPeriodCreateEditBinding.bind(view)
    }

    override fun setUpViews() {
        super.setUpViews()
        binding.toolbar.setNavigationOnClickListener { viewModel.onBack() }
        binding.buttonApply.setOnClickListener { viewModel.onApply(getPeriodInputBundle()) }
        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
    }

    override fun initObservers() {
        super.initObservers()
        viewModel.mode.observe(viewLifecycleOwner) { setViewsToMode(it) }
        viewModel.name.observe(viewLifecycleOwner) { setPeriodName(it) }
        viewModel.max.observe(viewLifecycleOwner) { setPeriodMax(it) }
        viewModel.periodRecords.observe(viewLifecycleOwner) { setPeriodRecords(it) }
        viewModel.performUiEvent.observeEvent(viewLifecycleOwner) { performUiEvent(it) }
    }

    private fun setViewsToMode(mode: CreateEditMode) {
        when (mode) {
            CreateEditMode.CREATE -> {
                adapter.isInsertionTemplate = true
                binding.toolbar.title = getString(R.string.create_period)
                binding.buttonApply.text = getString(R.string.create)
            }
            CreateEditMode.EDIT -> {
                adapter.isInsertionTemplate = false
                binding.toolbar.title = getString(R.string.edit_period)
                binding.buttonApply.text = getString(R.string.save)
            }
        }
    }

    private fun setPeriodName(name: String) {
        binding.inputLayoutName.editText?.apply {
            setText(name)
            setSelection(name.length)
        }
    }

    private fun setPeriodMax(max: Int?) {
        binding.inputLayoutMax.inputText = max?.toString()
    }

    private fun setPeriodRecords(periodRecords: List<PeriodRecordViewEntity>) {
        adapter.submitList(periodRecords)
    }

    private fun performUiEvent(uiEvent: PeriodCreateEditUiEvent) {
        when (uiEvent) {
            is PeriodCreateEditUiEvent.DisplayLoadingState -> handleLoadingState(uiEvent.loadingState)
            is PeriodCreateEditUiEvent.SetResult -> setResult()
            is PeriodCreateEditUiEvent.DisplayValidationResults -> handleValidationErrors(uiEvent.validationErrors)
            is PeriodCreateEditUiEvent.Exit -> navController.navigateUp()
        }
    }

    private fun handleLoadingState(loadingState: LoadingState) {
        when (loadingState) {
            is LoadingState.Cold,
            is LoadingState.Success -> hideLoading()
            is LoadingState.Loading -> showLoading()
            is LoadingState.Error -> {
                hideLoading()
                showErrorToast(loadingState.throwable)
            }
        }
    }

    private fun handleValidationErrors(validationErrors: ValidationErrors) {
        for (viewErrors in validationErrors.viewsErrors) {
            if (viewErrors.errorsIds.isNotEmpty())
                displayError(viewErrors.viewKey, viewErrors.highestPriorityOrNone)
        }
    }

    private fun displayError(viewKey: Int, errorId: Int) {
        when (viewKey) {
            PeriodCreateEditValidator.ViewKeys.VIEW_NAME -> binding.inputLayoutName.error = getErrorStringById(errorId)
        }
    }

    private fun getErrorStringById(errorId: Int) = when (errorId) {
        PeriodCreateEditValidator.Errors.EMPTY_NAME -> getString(R.string.error_empty_field)
        else -> null
    }

    private fun setResult() {
        setFragmentResult(Keys.KEY_PERIOD_RECORDS_SCREEN_ON_PERIODS_LIST_CHANGED, Bundle.EMPTY)
        setFragmentResult(Keys.KEY_ON_PERIOD_CREATE_EDIT, Bundle.EMPTY)
    }

    private fun getPeriodInputBundle() = PeriodInputBundle(
        name = binding.inputLayoutName.inputText.orEmpty().trim(),
        max = binding.inputLayoutMax.inputText?.trim()
    )

}