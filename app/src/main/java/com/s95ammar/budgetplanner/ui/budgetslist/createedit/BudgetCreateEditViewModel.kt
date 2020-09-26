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
import com.s95ammar.budgetplanner.ui.budgetslist.createedit.validation.BudgetValidationEntity
import com.s95ammar.budgetplanner.ui.common.BundleKey
import com.s95ammar.budgetplanner.ui.common.Constants
import com.s95ammar.budgetplanner.ui.common.CreateEditMode
import com.s95ammar.budgetplanner.ui.common.validation.*
import com.s95ammar.budgetplanner.util.EventMutableLiveData
import com.s95ammar.budgetplanner.util.asLiveData
import kotlinx.coroutines.launch

class BudgetCreateEditViewModel @ViewModelInject constructor(
    private val repository: Repository,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val budgetId = savedStateHandle.get<Int>(BundleKey.KEY_BUDGET_ID) ?: Constants.NO_ITEM


    private val _mode = MutableLiveData(CreateEditMode.getById(budgetId))
    private val _onViewValidationError = EventMutableLiveData<ValidationErrors>()

    val mode = _mode.asLiveData()
    val onViewValidationError = _onViewValidationError.asEventLiveData()

    fun onApply(budgetValidationEntity: BudgetValidationEntity) {
        val validator = createValidator(budgetValidationEntity)

        when (val result = validator.getValidationResult()) {
            is ValidationResult.Success -> {
                when (_mode.value) {
                    CreateEditMode.CREATE -> insert(result.outputData)
                    CreateEditMode.EDIT -> update(result.outputData)
                }
            }
            is ValidationResult.Error -> _onViewValidationError.call(result.throwable)
        }

    }

    private fun createValidator(budgetValidationEntity: BudgetValidationEntity): Validator<BudgetValidationEntity, Budget> {
        return object: Validator<BudgetValidationEntity, Budget>(budgetValidationEntity) {

            override fun provideOutputEntity(inputEntity: BudgetValidationEntity): Budget {
                return Budget(budgetValidationEntity.budgetTitle, budgetValidationEntity.budgetTotalBalance.toLongOrNull() ?: 0)
            }

            override fun provideViewValidationList(): List<ViewValidation> {

                val titleValidation = ViewValidation(
                    BudgetCreateEditViewKeys.VIEW_TITLE,
                    listOf(
                        ValidationCase(
                            { budgetValidationEntity.budgetTitle.isEmpty() },
                            BudgetCreateEditErrors.EMPTY_TITLE
                        )
                    )
                )

                val totalBalanceValidation = ViewValidation(
                    BudgetCreateEditViewKeys.VIEW_TOTAL_BALANCE,
                    listOf(
                        ValidationCase(
                            { budgetValidationEntity.budgetTotalBalance.isEmpty() },
                            BudgetCreateEditErrors.EMPTY_TOTAL_BALANCE
                        ),
                        ValidationCase(
                            { budgetValidationEntity.budgetTotalBalance.toLongOrNull() == null },
                            BudgetCreateEditErrors.INVALID_TOTAL_BALANCE
                        )
                    )
                )

                return listOf(titleValidation, totalBalanceValidation)
            }
        }
    }

    private fun insert(budget: Budget)  = viewModelScope.launch { repository.insert(budget) }

    private fun update(budget: Budget) = viewModelScope.launch { repository.update(budget) }

}