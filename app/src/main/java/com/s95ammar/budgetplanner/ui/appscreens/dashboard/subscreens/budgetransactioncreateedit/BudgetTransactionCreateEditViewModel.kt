package com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.budgetransactioncreateedit

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.s95ammar.budgetplanner.models.api.responses.IntBudgetTransactionType
import com.s95ammar.budgetplanner.models.repository.PeriodRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BudgetTransactionCreateEditViewModel @Inject constructor(
    private val repository: PeriodRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _selectedType = MutableLiveData<@IntBudgetTransactionType Int>()

    fun setSelectedBudgetTransactionType(position: Int) {
        IntBudgetTransactionType.values().getOrNull(position)?.let { type ->
            _selectedType.value = type
        }
    }

/*
    private val editedPeriodId = savedStateHandle.get<Int>(PeriodCreateEditFragmentArgs::periodId.name) ?: Int.NO_ITEM

    private val _mode = MutableLiveData(CreateEditMode.getById(editedPeriodId))
    private val _period = LoaderMutableLiveData<PeriodViewEntity> { loadEditedPeriodOrInsertTemplate() }

    private val _name = MediatorLiveData<String>().apply {
        addSource(_period) { value = it.name }
    }
    private val _max = MediatorLiveData<Int>().apply {
        addSource(_period) { value = it.max }
    }
    private val _periodicCategories = MediatorLiveData<List<PeriodicCategoryViewEntity>>().apply {
        addSource(_period) { value = it.periodicCategories }
    }

    private val _performUiEvent = EventMutableLiveData<UiEvent>()

    val mode = _mode.asLiveData()
    val name = _name.asLiveData()
    val max = _max.asLiveData()
    val periodicCategories = _periodicCategories.asLiveData()
    val performUiEvent = _performUiEvent.asEventLiveData()

    fun onPeriodicCategorySelectionStateChanged(position: Int, isSelected: Boolean) {
        _periodicCategories.value = _periodicCategories.value.orEmpty().mapIndexed { i, periodicCategory ->
            periodicCategory.copy(isSelected = if (i == position) isSelected else periodicCategory.isSelected)
        }
    }

    fun onPeriodicCategoryMaxChanged(position: Int, maxInput: String?) {
        _periodicCategories.value?.getOrNull(position)?.let { periodicCategory ->
            periodicCategory.max = maxInput?.toIntOrNull()
        }
    }

    fun onApply(periodInputBundle: PeriodInputBundle) {
        val validator = createValidator(periodInputBundle)

        _performUiEvent.call(UiEvent.DisplayValidationResults(validator.getValidationErrors(allBlank = true)))

        validator.getValidationResult()
            .onSuccess { insertOrUpdatePeriod(it) }
            .onError { onValidationError(it) }

    }

    fun onBack() {
        _performUiEvent.call(UiEvent.Exit)
    }

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
                .collect { periodApiEntity ->
                    PeriodViewEntity.ApiMapper.toViewEntity(periodApiEntity)?.let { periodViewEntity ->
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