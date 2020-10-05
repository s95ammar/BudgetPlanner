package com.s95ammar.budgetplanner.ui.budgetslist.createedit

import android.content.Context
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.s95ammar.budgetplanner.models.Resource
import com.s95ammar.budgetplanner.models.Result
import com.s95ammar.budgetplanner.models.ResultStateListener
import com.s95ammar.budgetplanner.models.data.Budget
import com.s95ammar.budgetplanner.models.repository.Repository
import com.s95ammar.budgetplanner.ui.base.SharedPreferencesViewModel
import com.s95ammar.budgetplanner.ui.budgetslist.createedit.validation.BudgetCreateEditErrors
import com.s95ammar.budgetplanner.ui.budgetslist.createedit.validation.BudgetCreateEditViewKeys
import com.s95ammar.budgetplanner.ui.budgetslist.createedit.validation.BudgetInputEntity
import com.s95ammar.budgetplanner.ui.common.BundleKey
import com.s95ammar.budgetplanner.util.NO_ITEM
import com.s95ammar.budgetplanner.ui.common.CreateEditMode
import com.s95ammar.budgetplanner.ui.common.validation.*
import com.s95ammar.budgetplanner.util.EventMutableLiveData
import com.s95ammar.budgetplanner.util.asLiveData
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import java.lang.Exception

class BudgetCreateEditViewModel @ViewModelInject constructor(
    @ApplicationContext context: Context,
    private val repository: Repository,
    @Assisted private val savedStateHandle: SavedStateHandle
) : SharedPreferencesViewModel(context) {
    private val budgetId = savedStateHandle.get<Int>(BundleKey.KEY_BUDGET_ID) ?: Int.NO_ITEM

    private val _mode = MutableLiveData(CreateEditMode.getById(budgetId))
    private val _activeCheckboxCheckedState = MutableLiveData(budgetId == activeBudgetId)
    private val _activeWarningVisibility = MutableLiveData(false)
    private val _onViewValidationError = EventMutableLiveData<ValidationErrors>()
    private val _createEditResult = EventMutableLiveData<Result>()

    val mode = _mode.asLiveData()
    val activeCheckboxCheckedState = _activeCheckboxCheckedState.asLiveData()
    val activeWarningVisibility = _activeWarningVisibility.asLiveData()
    val onViewValidationError = _onViewValidationError.asEventLiveData()
    val createEditResult = _createEditResult.asEventLiveData()

    val editedBudget = liveData<Resource<Budget>?> {
        if (budgetId != Int.NO_ITEM) {
            emit(Resource.Loading())
            try {
                emitSource(repository.getBudgetById(budgetId).map { Resource.Success(it) })
            } catch (e: Exception) {
                emit(Resource.Error(e))
            }
        }
    }

    fun onApply(budgetInputEntity: BudgetInputEntity) {
        val validator = createValidator(budgetInputEntity)

        when (val validationResult = validator.getValidationResult()) {
            is ValidationResult.Success -> {
                val budget = validationResult.outputData
                insertOrReplace(
                    budget = budget,
                    listener = object : ResultStateListener {
                        override fun onSuccess() {
                            handleActiveBudget(budget.id, budgetInputEntity.isActive)
                            _createEditResult.call(Result.Success)
                        }
                        override fun onLoading() { _createEditResult.call(Result.Loading) }
                        override fun onError(throwable: Throwable) { _createEditResult.call(Result.Error(throwable)) }
                    }
                )
            }
            is ValidationResult.Error -> _onViewValidationError.call(validationResult.throwable)
        }

    }

    private fun handleActiveBudget(id: Int, isActiveInput: Boolean) {
        val wasThisBudgetInitiallyActive = (id == activeBudgetId)

        if (isActiveInput) {
            if (!wasThisBudgetInitiallyActive)
                saveActiveBudgetId(id)
        } else {
            if (wasThisBudgetInitiallyActive)
                saveActiveBudgetId(Int.NO_ITEM)
        }

    }

    private fun createValidator(budgetInputEntity: BudgetInputEntity): Validator<BudgetInputEntity, Budget> {
        return object: Validator<BudgetInputEntity, Budget>(budgetInputEntity) {

            override fun provideOutputEntity(inputEntity: BudgetInputEntity): Budget {
                return Budget(inputEntity.title, inputEntity.totalBalance.toLongOrNull() ?: 0)
                    .apply { if (budgetId != Int.NO_ITEM) id = budgetId }
            }

            override fun provideViewValidationList(): List<ViewValidation> {

                val titleValidation = ViewValidation(
                    BudgetCreateEditViewKeys.VIEW_TITLE,
                    listOf(
                        ViewValidation.Case(
                            { budgetInputEntity.title.isEmpty() },
                            BudgetCreateEditErrors.EMPTY_TITLE
                        )
                    )
                )

                val totalBalanceValidation = ViewValidation(
                    BudgetCreateEditViewKeys.VIEW_TOTAL_BALANCE,
                    listOf(
                        ViewValidation.Case(
                            { budgetInputEntity.totalBalance.isEmpty() },
                            BudgetCreateEditErrors.EMPTY_TOTAL_BALANCE
                        ),
                        ViewValidation.Case(
                            { budgetInputEntity.totalBalance.toLongOrNull() == null },
                            BudgetCreateEditErrors.INVALID_TOTAL_BALANCE
                        )
                    )
                )

                return listOf(titleValidation, totalBalanceValidation)
            }
        }
    }

    private fun insertOrReplace(budget: Budget, listener: ResultStateListener)  = viewModelScope.launch {
        try {
            listener.onLoading()
            repository.insertOrReplace(budget)
            listener.onSuccess()
        } catch (e: Exception) {
            listener.onError(e)
        }
    }

    fun onIsActiveStateChanged(isChecked: Boolean) {
        _activeWarningVisibility.value = isChecked && activeBudgetExist && budgetId != activeBudgetId
    }

}