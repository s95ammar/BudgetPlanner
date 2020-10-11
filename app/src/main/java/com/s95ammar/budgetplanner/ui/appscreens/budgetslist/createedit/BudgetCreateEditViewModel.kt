package com.s95ammar.budgetplanner.ui.appscreens.budgetslist.createedit

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.s95ammar.budgetplanner.models.Resource
import com.s95ammar.budgetplanner.models.Result
import com.s95ammar.budgetplanner.models.ResultStateListener
import com.s95ammar.budgetplanner.models.data.Budget
import com.s95ammar.budgetplanner.models.repository.LocalRepository
import com.s95ammar.budgetplanner.ui.appscreens.budgetslist.createedit.validation.BudgetCreateEditErrors
import com.s95ammar.budgetplanner.ui.appscreens.budgetslist.createedit.validation.BudgetCreateEditViewKeys
import com.s95ammar.budgetplanner.ui.appscreens.budgetslist.createedit.data.BudgetInputBundle
import com.s95ammar.budgetplanner.ui.common.Keys
import com.s95ammar.budgetplanner.util.NO_ITEM
import com.s95ammar.budgetplanner.ui.common.CreateEditMode
import com.s95ammar.budgetplanner.ui.common.validation.*
import com.s95ammar.budgetplanner.util.lifecycleutil.EventMutableLiveData
import com.s95ammar.budgetplanner.util.lifecycleutil.asLiveData
import kotlinx.coroutines.launch
import java.lang.Exception

class BudgetCreateEditViewModel @ViewModelInject constructor(
    private val localRepository: LocalRepository,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val budgetId = savedStateHandle.get<Int>(Keys.KEY_BUDGET_ID) ?: Int.NO_ITEM
    private val activeBudgetId = localRepository.loadActiveBudgetId()
    private val activeBudgetExist = localRepository.doesActiveBudgetExist()

    private val _mode = MutableLiveData(CreateEditMode.getById(budgetId))
    private val _activeCheckboxCheckedState = MutableLiveData(budgetId == localRepository.loadActiveBudgetId())
    private val _activeWarningVisibility = MutableLiveData(false)
    private val _onViewValidationError = EventMutableLiveData<ValidationErrors>()
    private val _onCreateEditApply = EventMutableLiveData<Result>()
    private val _onActiveBudgetChanged = EventMutableLiveData<Int>()

    val mode = _mode.asLiveData()
    val activeCheckboxCheckedState = _activeCheckboxCheckedState.asLiveData()
    val activeWarningVisibility = _activeWarningVisibility.asLiveData()
    val onViewValidationError = _onViewValidationError.asEventLiveData()
    val onCreateEditApply = _onCreateEditApply.asEventLiveData()
    val onActiveBudgetChanged = _onActiveBudgetChanged.asEventLiveData()

    val editedBudget = liveData<Resource<Budget>?> {
        if (budgetId != Int.NO_ITEM) {
            emit(Resource.Loading())
            try {
                emitSource(localRepository.getBudgetByIdLiveData(budgetId).map { Resource.Success(it) })
            } catch (e: Exception) {
                emit(Resource.Error(e))
            }
        }
    }

    fun onInputIsActiveStateChanged(isChecked: Boolean) {
        _activeWarningVisibility.value = isChecked && activeBudgetExist && (budgetId != activeBudgetId)
    }

    fun onApply(budgetInputBundle: BudgetInputBundle) {
        val validator = createValidator(budgetInputBundle)

        when (val validationResult = validator.getValidationResult()) {
            is ValidationResult.Success -> onValidationSuccessful(validationResult.outputData, budgetInputBundle)
            is ValidationResult.Error -> onValidationError(validationResult.throwable)
        }
    }

    private fun onValidationSuccessful(budget: Budget, budgetInputBundle: BudgetInputBundle) {
        insertOrReplace(
            budget = budget,
            listener = object : ResultStateListener<Long> {
                override fun onSuccess(data: Long?) {
                    val id = data?.toInt() ?: return
                    handleActiveBudget(budgetInputBundle, id)
                    _onCreateEditApply.call(Result.Success)
                }

                override fun onLoading() = _onCreateEditApply.call(Result.Loading)
                override fun onError(throwable: Throwable) = _onCreateEditApply.call(Result.Error(throwable))
            }
        )
    }

    private fun onValidationError(validationErrors: ValidationErrors) {
        _onViewValidationError.call(validationErrors)
    }

    private fun handleActiveBudget(budget: BudgetInputBundle, id: Int) {
        if (_mode.value == CreateEditMode.CREATE && budget.isActive)
            saveNewActiveBudget(id)
        else
            if (budget.wasInactiveNowActive(id))
                saveNewActiveBudget(id)
            else if (budget.wasActiveNowInactive(id))
                saveNewActiveBudget(Int.NO_ITEM)
        }

    private fun saveNewActiveBudget(id: Int) {
        localRepository.saveActiveBudgetId(id)
        _onActiveBudgetChanged.call(id)
    }

    private fun BudgetInputBundle.wasInactiveNowActive(id: Int) = !wasActive(id) && isActive
    private fun BudgetInputBundle.wasActiveNowInactive(id: Int) = wasActive(id) && !isActive
    private fun wasActive(id: Int) = activeBudgetExist && (id == activeBudgetId)

    private fun createValidator(budgetInputBundle: BudgetInputBundle): Validator<BudgetInputBundle, Budget> {
        return object: Validator<BudgetInputBundle, Budget>(budgetInputBundle) {

            override fun provideOutputEntity(inputEntity: BudgetInputBundle): Budget {
                return Budget(inputEntity.title, inputEntity.totalBalance.toLongOrNull() ?: 0)
                    .apply { if (budgetId != Int.NO_ITEM) id = budgetId }
            }

            override fun provideViewValidationList(): List<ViewValidation> {

                val caseEmptyTitle = ViewValidation.Case(BudgetCreateEditErrors.EMPTY_TITLE) { budgetInputBundle.title.isEmpty() }

                val caseEmptyTotalBalance =
                    ViewValidation.Case(BudgetCreateEditErrors.EMPTY_TOTAL_BALANCE) { budgetInputBundle.totalBalance.isEmpty() }

                val caseInvalidTotalBalance =
                    ViewValidation.Case(BudgetCreateEditErrors.INVALID_TOTAL_BALANCE) { budgetInputBundle.totalBalance.toLongOrNull() == null }

                return listOf(
                    ViewValidation(BudgetCreateEditViewKeys.VIEW_TITLE, listOf(caseEmptyTitle)),
                    ViewValidation(BudgetCreateEditViewKeys.VIEW_TOTAL_BALANCE, listOf(caseEmptyTotalBalance, caseInvalidTotalBalance))
                )
            }
        }
    }

    private fun insertOrReplace(budget: Budget, listener: ResultStateListener<Long>)  = viewModelScope.launch {
        try {
            listener.onLoading()
            val id = localRepository.insertOrReplace(budget)
            listener.onSuccess(id)
        } catch (e: Exception) {
            listener.onError(e)
        }
    }

}