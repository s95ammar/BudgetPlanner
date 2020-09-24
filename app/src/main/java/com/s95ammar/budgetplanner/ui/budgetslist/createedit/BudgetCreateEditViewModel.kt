package com.s95ammar.budgetplanner.ui.budgetslist.createedit

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.s95ammar.budgetplanner.models.data.Budget
import com.s95ammar.budgetplanner.models.repository.Repository
import com.s95ammar.budgetplanner.ui.budgetslist.createedit.validation.BudgetCreateEditErrors
import com.s95ammar.budgetplanner.ui.budgetslist.createedit.validation.BudgetCreateEditViewKeys
import com.s95ammar.budgetplanner.ui.common.BundleKey
import com.s95ammar.budgetplanner.ui.common.Constants
import com.s95ammar.budgetplanner.ui.common.CreateEditMode
import com.s95ammar.budgetplanner.ui.common.validation.ValidationData
import com.s95ammar.budgetplanner.ui.common.validation.ValidationErrorType
import com.s95ammar.budgetplanner.ui.common.validation.ValidationErrors
import com.s95ammar.budgetplanner.util.asLiveData
import kotlinx.coroutines.launch

class BudgetCreateEditViewModel @ViewModelInject constructor(
    private val repository: Repository,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val budgetId = savedStateHandle.get<Int>(BundleKey.KEY_BUDGET_ID) ?: Constants.NO_ITEM


    private val _mode = MutableLiveData(CreateEditMode.getById(budgetId))

    val mode = _mode.asLiveData()

    fun onApply(budgetTitle: String, budgetTotalBalance: String) {
        val budgetTotalBalanceLong = budgetTotalBalance.toLongOrNull()

        val validationData = createValidationData(budgetTitle, budgetTotalBalanceLong)
        if (validationData.isAllValid()) {

        } else {
            ValidationErrors(validationData)
        }

    }

    private fun createValidationData(budgetTitle: String, budgetTotalBalanceLong: Long?): ValidationData {
        val validationData = ValidationData()
        validationData[BudgetCreateEditViewKeys.VIEW_TITLE] =
            if (budgetTitle.isBlank()) ValidationErrorType(BudgetCreateEditErrors.EMPTY_TITLE)
            else ValidationErrorType.none()
        validationData[BudgetCreateEditViewKeys.VIEW_TOTAL_BALANCE] =
            if (budgetTotalBalanceLong == null) ValidationErrorType(BudgetCreateEditErrors.EMPTY_TOTAL_BALANCE)
            else ValidationErrorType.none()

        // TODO
        return validationData
    }

    private fun insert(budget: Budget)  = viewModelScope.launch { repository.insert(budget) }

    private fun update(budget: Budget) = viewModelScope.launch { repository.update(budget) }

}