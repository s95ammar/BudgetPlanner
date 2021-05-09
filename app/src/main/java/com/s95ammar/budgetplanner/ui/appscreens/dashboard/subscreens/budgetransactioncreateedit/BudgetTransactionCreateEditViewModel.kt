package com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.budgetransactioncreateedit

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.s95ammar.budgetplanner.models.IntBudgetTransactionType
import com.s95ammar.budgetplanner.models.repository.BudgetTransactionRepository
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.common.data.BudgetTransaction
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.budgetransactioncreateedit.subscreens.categoryselection.data.PeriodicCategoryIdAndName
import com.s95ammar.budgetplanner.ui.common.CreateEditMode
import com.s95ammar.budgetplanner.util.INVALID
import com.s95ammar.budgetplanner.util.lifecycleutil.EventMutableLiveData
import com.s95ammar.budgetplanner.util.lifecycleutil.LoaderMutableLiveData
import com.s95ammar.budgetplanner.util.lifecycleutil.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.budgetransactioncreateedit.data.BudgetTransactionCreateEditUiEvent as UiEvent

@HiltViewModel
class BudgetTransactionCreateEditViewModel @Inject constructor(
    private val repository: BudgetTransactionRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val budgetTransactionId = savedStateHandle.get<Int>(BudgetTransactionCreateEditFragmentArgs::budgetTransactionId.name) ?: Int.INVALID
    private val periodId = savedStateHandle.get<Int>(BudgetTransactionCreateEditFragmentArgs::periodId.name) ?: Int.INVALID

    private val _mode = MutableLiveData(CreateEditMode.getById(budgetTransactionId))
    private val _budgetTransaction = LoaderMutableLiveData<BudgetTransaction> {
        if (_mode.value == CreateEditMode.EDIT) loadEditedBudgetTransaction()
    }

    private val _name = MediatorLiveData<String>().apply {
        addSource(_budgetTransaction) { value = it.name }
    }
    private val _type = MediatorLiveData<Int>().apply {
        addSource(_budgetTransaction) { value = it.type }
    }
    private val _amount = MediatorLiveData<Int>().apply {
        addSource(_budgetTransaction) { value = it.amount }
    }
    private val _periodicCategory = MediatorLiveData<PeriodicCategoryIdAndName>().apply {
        addSource(_budgetTransaction) { value = PeriodicCategoryIdAndName(it.periodicCategoryId, it.categoryName) }
    }
    private val _performUiEvent = EventMutableLiveData<UiEvent>()

    val mode = _mode.asLiveData()
    val periodicCategory = _periodicCategory.asLiveData()
    val performUiEvent = _performUiEvent.asEventLiveData()
/*
    val name = _name.asLiveData()

    fun onApply(periodInputBundle: PeriodInputBundle) {
        val validator = createValidator(periodInputBundle)

        _performUiEvent.call(UiEvent.DisplayValidationResults(validator.getValidationErrors(allBlank = true)))

        validator.getValidationResult()
            .onSuccess { insertOrUpdatePeriod(it) }
            .onError { onValidationError(it) }

    }
*/
    fun setSelectedBudgetTransactionType(position: Int) {
        IntBudgetTransactionType.values().getOrNull(position)?.let { type ->
            _type.value = type
        }
    }

    fun onChooseCategory() {
        _performUiEvent.call(UiEvent.ChooseCategory(periodId))
    }

    fun setPeriodicCategory(periodicCategory: PeriodicCategoryIdAndName) {
        _periodicCategory.value = periodicCategory
    }

    fun onBack() {
        _performUiEvent.call(UiEvent.Exit)
    }

    private fun loadEditedBudgetTransaction() {
        // TODO
    }

/*
    private fun createValidator(periodInputBundle: PeriodInputBundle): PeriodCreateEditValidator {
        val periodicCategoryUpsertApiRequestListGetter = {
            _periodicCategories.value.orEmpty()
                .filter { it.isSelected }
                .map { PeriodicCategoryUpsertApiRequest(it.categoryId, it.max) }
        }

        return PeriodCreateEditValidator(editedPeriodId, periodicCategoryUpsertApiRequestListGetter, periodInputBundle)
    }

    private fun loadEditedPeriodOrInsertTemplate() {
        val mode = _mode.value ?: return

        viewModelScope.launch {
            val flowRequest = when (mode) {
                CreateEditMode.EDIT -> repository.getPeriod(editedPeriodId, includePeriodicCategories = true)
                CreateEditMode.CREATE -> repository.getPeriodInsertTemplate()
            }

            flowRequest
                .onStart {
                    _performUiEvent.call(UiEvent.DisplayLoadingState(LoadingState.Loading))
                }
                .catch { throwable ->
                    _performUiEvent.call(UiEvent.DisplayLoadingState(LoadingState.Error(throwable)))
                }
                .collect { periodEntity ->
                    PeriodViewEntity.EntityMapper.toViewEntity(periodEntity)?.let { periodViewEntity ->
                        _period.value = periodViewEntity
                        _performUiEvent.call(UiEvent.DisplayLoadingState(LoadingState.Success))
                    }
                }
        }
    }

    private fun insertOrUpdatePeriod(request: PeriodUpsertApiRequest) = viewModelScope.launch {
            val flowRequest = when (request) {
                is PeriodUpsertApiRequest.Insertion -> repository.insertPeriod(request)
                is PeriodUpsertApiRequest.Update -> repository.updatePeriod(request)
            }

            flowRequest
                .onStart {
                    _performUiEvent.call(UiEvent.DisplayLoadingState(LoadingState.Loading))
                }
                .catch { throwable ->
                    _performUiEvent.call(UiEvent.DisplayLoadingState(LoadingState.Error(throwable)))
                }
                .collect {
                    _performUiEvent.call(UiEvent.SetResult)
                    _performUiEvent.call(UiEvent.DisplayLoadingState(LoadingState.Success))
                    _performUiEvent.call(UiEvent.Exit)
                }
    }

    private fun onValidationError(validationErrors: ValidationErrors) {
        _performUiEvent.call(UiEvent.DisplayValidationResults(validationErrors))
    }
*/

}