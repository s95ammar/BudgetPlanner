package com.s95ammar.budgetplanner.ui.appscreens.dashboard.budget.periods.createedit

import android.view.View
import androidx.fragment.app.viewModels
import com.s95ammar.budgetplanner.R
import com.s95ammar.budgetplanner.databinding.FragmentPeriodCreateEditBinding
import com.s95ammar.budgetplanner.models.view.PeriodViewEntity
import com.s95ammar.budgetplanner.ui.appscreens.auth.common.LoadingState
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.budget.periods.createedit.data.PeriodInputBundle
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.budget.periods.createedit.validation.PeriodCreateEditValidator
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

    override val binding: FragmentPeriodCreateEditBinding
        get() = getBinding()

    override fun initViewBinding(view: View): FragmentPeriodCreateEditBinding {
        return FragmentPeriodCreateEditBinding.bind(view)
    }

    override fun setUpViews() {
        super.setUpViews()
        binding.toolbar.setNavigationOnClickListener { navController.navigateUp() }
        binding.buttonApply.setOnClickListener { onApply() }
    }

    override fun initObservers() {
        super.initObservers()
        viewModel.mode.observe(viewLifecycleOwner) { setViewsToMode(it) }
        viewModel.editedPeriod.observe(viewLifecycleOwner) { setViewsToEditedPeriod(it) }
        viewModel.displayLoadingState.observeEvent(viewLifecycleOwner) { handleLoadingState(it) }
        viewModel.displayValidationResults.observeEvent(viewLifecycleOwner) { handleValidationErrors(it) }
        viewModel.onApplySuccess.observeEvent(viewLifecycleOwner) { onApplySuccess() }
    }

    private fun setViewsToMode(mode: CreateEditMode) {
        when (mode) {
            CreateEditMode.CREATE -> {
                binding.toolbar.title = getString(R.string.create_period)
                binding.buttonApply.text = getString(R.string.create)
            }
            CreateEditMode.EDIT -> {
                binding.toolbar.title = getString(R.string.edit_period)
                binding.buttonApply.text = getString(R.string.save)
            }
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

    private fun setViewsToEditedPeriod(period: PeriodViewEntity) {
        binding.inputLayoutTitle.editText?.apply {
            setText(period.name)
            setSelection(period.name.length)
        }
        binding.inputLayoutMax.inputText = period.max?.toString()
    }

    private fun handleValidationErrors(validationErrors: ValidationErrors) {
        for (viewErrors in validationErrors.viewsErrors) {
            if (viewErrors.errorsIds.isNotEmpty())
                displayError(viewErrors.viewKey, viewErrors.highestPriorityOrNone)
        }
    }

    private fun displayError(viewKey: Int, errorId: Int) {
        when (viewKey) {
            PeriodCreateEditValidator.ViewKeys.VIEW_TITLE -> binding.inputLayoutTitle.error = getErrorStringById(errorId)
            PeriodCreateEditValidator.Errors.NAME_TAKEN -> binding.inputLayoutTitle.error = getErrorStringById(errorId)
        }
    }

    private fun getErrorStringById(errorId: Int) = when (errorId) {
        PeriodCreateEditValidator.Errors.EMPTY_TITLE -> getString(R.string.error_empty_field)
        else -> null
    }

    private fun onApplySuccess() {
        sendResult(Keys.KEY_ON_PERIOD_CREATE_EDIT, true)
        navController.navigateUp()
    }

    private fun onApply() {
        viewModel.onApply(
            PeriodInputBundle(
                title = binding.inputLayoutTitle.inputText.orEmpty().trim(),
                max = binding.inputLayoutMax.inputText?.trim()
            )
        )
    }

}