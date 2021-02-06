package com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.periodcreateedit

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.s95ammar.budgetplanner.models.api.requests.PeriodUpsertApiRequest
import com.s95ammar.budgetplanner.models.api.requests.PeriodicCategoryUpsertApiRequest
import com.s95ammar.budgetplanner.models.repository.PeriodRepository
import com.s95ammar.budgetplanner.ui.appscreens.auth.common.LoadingState
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.common.data.PeriodViewEntity
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.common.data.PeriodicCategoryViewEntity
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.periodcreateedit.data.PeriodInputBundle
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.periodcreateedit.validation.PeriodCreateEditValidator
import com.s95ammar.budgetplanner.ui.common.CreateEditMode
import com.s95ammar.budgetplanner.ui.common.validation.ValidationErrors
import com.s95ammar.budgetplanner.util.NO_ITEM
import com.s95ammar.budgetplanner.util.lifecycleutil.EventMutableLiveData
import com.s95ammar.budgetplanner.util.lifecycleutil.LoaderMutableLiveData
import com.s95ammar.budgetplanner.util.lifecycleutil.asLiveData
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.periodcreateedit.data.PeriodCreateEditUiEvent as UiEvent

class PeriodCreateEditViewModel @ViewModelInject constructor(
    private val repository: PeriodRepository,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {

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

}