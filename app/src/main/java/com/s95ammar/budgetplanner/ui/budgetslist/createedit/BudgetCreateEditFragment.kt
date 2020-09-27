package com.s95ammar.budgetplanner.ui.budgetslist.createedit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.s95ammar.budgetplanner.R
import com.s95ammar.budgetplanner.ui.base.BaseFragment
import com.s95ammar.budgetplanner.ui.budgetslist.createedit.validation.BudgetCreateEditErrors
import com.s95ammar.budgetplanner.ui.budgetslist.createedit.validation.BudgetCreateEditViewKeys
import com.s95ammar.budgetplanner.ui.budgetslist.createedit.validation.BudgetValidationEntity
import com.s95ammar.budgetplanner.ui.common.CreateEditMode
import com.s95ammar.budgetplanner.ui.common.validation.ValidationErrors
import com.s95ammar.budgetplanner.util.inputText
import com.s95ammar.budgetplanner.util.observeEvent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.budget_create_edit_fragment.*

@AndroidEntryPoint
class BudgetCreateEditFragment : BaseFragment() {

    companion object {
        fun newInstance() = BudgetCreateEditFragment()
    }

    private val viewModel: BudgetCreateEditViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.budget_create_edit_fragment, container, false)
    }

    override fun setUpViews() {
        super.setUpViews()
        toolbar_budgets_create_edit.setNavigationOnClickListener { navController.navigateUp() }
        button_budget_create_edit.setOnClickListener { onApply() }
    }

    override fun initObservers() {
        super.initObservers()
        viewModel.mode.observe(viewLifecycleOwner) { setViewsToMode(it) }
        viewModel.onViewValidationError.observeEvent(viewLifecycleOwner) { handleValidationErrors(it) }
        viewModel.onApplySuccess.observeEvent(viewLifecycleOwner) { navController.navigateUp() }
    }

    private fun handleValidationErrors(validationErrors: ValidationErrors) {
        for (viewErrors in validationErrors.viewsErrors) {
            if (viewErrors.errorsIds.isNotEmpty())
                displayError(viewErrors.viewKey, viewErrors.errorsIds.first())
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

    private fun onApply() {
        clearViewsValidation()
        viewModel.onApply(
            BudgetValidationEntity(
                budgetTitle = input_layout_budget_create_edit_title.inputText.trim(),
                budgetTotalBalance = input_layout_budget_create_edit_total_balance.inputText.trim()
            )
        )
    }

    private fun clearViewsValidation() {
        BudgetCreateEditViewKeys.values().forEach { displayError(it, ValidationErrors.ERROR_NONE) }
    }
}