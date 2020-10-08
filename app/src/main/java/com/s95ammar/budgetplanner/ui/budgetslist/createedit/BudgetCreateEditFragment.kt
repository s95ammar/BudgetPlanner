package com.s95ammar.budgetplanner.ui.budgetslist.createedit

import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.s95ammar.budgetplanner.R
import com.s95ammar.budgetplanner.models.Resource
import com.s95ammar.budgetplanner.models.Result
import com.s95ammar.budgetplanner.models.data.Budget
import com.s95ammar.budgetplanner.ui.base.BaseFragment
import com.s95ammar.budgetplanner.ui.budgetslist.createedit.validation.BudgetCreateEditErrors
import com.s95ammar.budgetplanner.ui.budgetslist.createedit.validation.BudgetCreateEditViewKeys
import com.s95ammar.budgetplanner.ui.budgetslist.createedit.validation.BudgetValidationBundle
import com.s95ammar.budgetplanner.ui.common.CreateEditMode
import com.s95ammar.budgetplanner.ui.common.Keys
import com.s95ammar.budgetplanner.ui.common.validation.ValidationErrors
import com.s95ammar.budgetplanner.util.inputText
import com.s95ammar.budgetplanner.util.lifecycleutil.observeEvent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.budget_create_edit_fragment.*

@AndroidEntryPoint
class BudgetCreateEditFragment : BaseFragment(R.layout.budget_create_edit_fragment) {

    private val viewModel: BudgetCreateEditViewModel by viewModels()

    override fun setUpViews() {
        super.setUpViews()
        toolbar_budgets_create_edit.setNavigationOnClickListener { navController.navigateUp() }
        button_budget_create_edit.setOnClickListener { onApply() }
        checkbox_active_budget_create_edit.setOnCheckedChangeListener { _, isChecked -> viewModel.onIsActiveStateChanged(isChecked) }
    }

    override fun initObservers() {
        super.initObservers()
        viewModel.mode.observe(viewLifecycleOwner) { setViewsToMode(it) }
        viewModel.activeCheckboxCheckedState.observe(viewLifecycleOwner) { setActiveCheckboxIsChecked(it) }
        viewModel.activeWarningVisibility.observe(viewLifecycleOwner) { setActiveWarningVisibility(it) }
        viewModel.editedBudget.observe(viewLifecycleOwner) { handleEditedBudgetLoading(it) }
        viewModel.onViewValidationError.observeEvent(viewLifecycleOwner) { handleValidationErrors(it) }
        viewModel.onCreateEditApply.observeEvent(viewLifecycleOwner) { handleCreateEditResult(it) }
        viewModel.onActiveBudgetChanged.observeEvent(viewLifecycleOwner) { sendOnActiveBudgetChangedResult(it) }
    }

    private fun sendOnActiveBudgetChangedResult(id: Int) {
        sendResult(Keys.KEY_RESULT_ACTIVE_BUDGET_CHANGED, id)
    }

    private fun setViewsToMode(mode: CreateEditMode) {
        when (mode) {
            CreateEditMode.CREATE -> {
                toolbar_budgets_create_edit.title = getString(R.string.create_budget)
                button_budget_create_edit.text = getString(R.string.create)
            }
            CreateEditMode.EDIT -> {
                toolbar_budgets_create_edit.title = getString(R.string.edit_budget)
                button_budget_create_edit.text = getString(R.string.save)
            }
        }
    }

    private fun setActiveCheckboxIsChecked(isChecked: Boolean) {
        checkbox_active_budget_create_edit.isChecked = isChecked
    }

    private fun setActiveWarningVisibility(isVisible: Boolean) {
        text_view_active_warning_budget_create_edit.isVisible = isVisible
    }

    private fun handleEditedBudgetLoading(budgetResource: Resource<Budget>?) {
        when (budgetResource) {
            is Resource.Loading -> loadingManager?.showLoading()
            is Resource.Error -> {
                loadingManager?.hideLoading()
                displayError(budgetResource.throwable)
            }
            is Resource.Success -> {
                loadingManager?.hideLoading()
                setViewsToEditedBudget(budgetResource.data)
            }
        }

    }

    private fun setViewsToEditedBudget(budget: Budget) {
        input_layout_budget_create_edit_title.editText?.setText(budget.name)
        input_layout_budget_create_edit_total_balance.editText?.setText(budget.totalBalance.toString())
    }

    private fun handleValidationErrors(validationErrors: ValidationErrors) {
        for (viewErrors in validationErrors.viewsErrors) {
            if (viewErrors.errorsIds.isNotEmpty())
                displayError(viewErrors.viewKey, viewErrors.errorsIds.first())
        }
    }

    private fun handleCreateEditResult(result: Result) {
        when (result) {
            is Result.Loading -> loadingManager?.showLoading()
            is Result.Error -> {
                loadingManager?.hideLoading()
                displayError(result.throwable)
            }
            is Result.Success -> {
                loadingManager?.hideLoading()
                navController.navigateUp()
            }
        }
    }

    private fun displayError(viewKey: Int, errorId: Int) {
        when (viewKey) {
            BudgetCreateEditViewKeys.VIEW_TITLE ->
                input_layout_budget_create_edit_title.error = getErrorStringById(errorId)
            BudgetCreateEditViewKeys.VIEW_TOTAL_BALANCE ->
                input_layout_budget_create_edit_total_balance.error = getErrorStringById(errorId)
        }
    }

    private fun getErrorStringById(errorId: Int) = when (errorId) {
        BudgetCreateEditErrors.EMPTY_TITLE -> getString(R.string.error_empty_title)
        BudgetCreateEditErrors.EMPTY_TOTAL_BALANCE -> getString(R.string.error_empty_total_balance)
        BudgetCreateEditErrors.INVALID_TOTAL_BALANCE -> getString(R.string.error_invalid_total_balance)
        else -> null
    }

    private fun onApply() {
        clearViewsValidation()
        viewModel.onApply(
            BudgetValidationBundle(
                title = input_layout_budget_create_edit_title.inputText.trim(),
                totalBalance = input_layout_budget_create_edit_total_balance.inputText.trim(),
                isActive = checkbox_active_budget_create_edit.isChecked
            )
        )
    }

    private fun clearViewsValidation() {
        BudgetCreateEditViewKeys.values().forEach { displayError(it, ValidationErrors.ERROR_NONE) }
    }
}