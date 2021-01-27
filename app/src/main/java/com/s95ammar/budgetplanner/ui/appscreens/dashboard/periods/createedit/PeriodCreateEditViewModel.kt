package com.s95ammar.budgetplanner.ui.appscreens.dashboard.periods.createedit

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.s95ammar.budgetplanner.models.api.requests.PeriodRecordUpsertApiRequest
import com.s95ammar.budgetplanner.models.api.requests.PeriodUpsertApiRequest
import com.s95ammar.budgetplanner.models.mappers.PeriodApiViewMapper
import com.s95ammar.budgetplanner.models.repository.LocalRepository
import com.s95ammar.budgetplanner.models.repository.RemoteRepository
import com.s95ammar.budgetplanner.models.view.PeriodRecordViewEntity
import com.s95ammar.budgetplanner.models.view.PeriodViewEntity
import com.s95ammar.budgetplanner.ui.appscreens.auth.common.LoadingState
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.periods.createedit.data.PeriodInputBundle
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.periods.createedit.validation.PeriodCreateEditValidator
import com.s95ammar.budgetplanner.ui.common.CreateEditMode
import com.s95ammar.budgetplanner.ui.common.validation.ValidationErrors
import com.s95ammar.budgetplanner.util.NO_ITEM
import com.s95ammar.budgetplanner.util.lifecycleutil.EventMutableLiveData
import com.s95ammar.budgetplanner.util.lifecycleutil.LoaderMutableLiveData
import com.s95ammar.budgetplanner.util.lifecycleutil.asLiveData
import kotlinx.coroutines.launch
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.periods.createedit.data.PeriodCreateEditUiEvent as UiEvent

class PeriodCreateEditViewModel @ViewModelInject constructor(
    private val localRepository: LocalRepository,
    private val remoteRepository: RemoteRepository,
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
    private val _periodRecords = MediatorLiveData<List<PeriodRecordViewEntity>>().apply {
        addSource(_period) { value = it.periodRecords }
    }

    private val _performUiEvent = EventMutableLiveData<UiEvent>()

    val mode = _mode.asLiveData()
    val name = _name.asLiveData()
    val max = _max.asLiveData()
    val periodRecords = _periodRecords.asLiveData()
    val performUiEvent = _performUiEvent.asEventLiveData()

    fun onPeriodRecordSelectionStateChanged(position: Int, isSelected: Boolean) {
        _periodRecords.value = _periodRecords.value.orEmpty().mapIndexed { i, periodRecord ->
            periodRecord.copy(isSelected = if (i == position) isSelected else periodRecord.isSelected)
        }
    }

    fun onPeriodRecordMaxChanged(position: Int, maxInput: String?) {
        _periodRecords.value?.getOrNull(position)?.let { periodRecord ->
            periodRecord.max = maxInput?.toIntOrNull()
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
        val periodRecordUpsertApiRequestListGetter = {
            _periodRecords.value.orEmpty()
                .filter { it.isSelected }
                .map { PeriodRecordUpsertApiRequest(it.categoryId, it.max) }
        }

        return PeriodCreateEditValidator(editedPeriodId, periodRecordUpsertApiRequestListGetter, periodInputBundle)
    }

    private fun loadEditedPeriodOrInsertTemplate() {
        val mode = _mode.value ?: return

        viewModelScope.launch {
            _performUiEvent.call(UiEvent.DisplayLoadingState(LoadingState.Loading))
            val result = when (mode) {
                CreateEditMode.EDIT -> remoteRepository.getPeriod(editedPeriodId, includePeriodRecords = true)
                CreateEditMode.CREATE -> remoteRepository.getPeriodInsertTemplate()
            }

            result
                .onSuccess { periodApiEntity ->
                    PeriodApiViewMapper.toViewEntity(periodApiEntity)?.let { periodViewEntity ->
                        _period.value = periodViewEntity
                        _performUiEvent.call(UiEvent.DisplayLoadingState(LoadingState.Success))
                    }
                }
                .onError { throwable ->
                    _performUiEvent.call(UiEvent.DisplayLoadingState(LoadingState.Error(throwable)))
                }
        }
    }

    private fun insertOrUpdatePeriod(period: PeriodUpsertApiRequest) = viewModelScope.launch {
            _performUiEvent.call(UiEvent.DisplayLoadingState(LoadingState.Loading))
            val result = when (period) {
                is PeriodUpsertApiRequest.Insertion -> remoteRepository.insertPeriod(period)
                is PeriodUpsertApiRequest.Update -> remoteRepository.updatePeriod(period)
            }

            result
                .onSuccess {
                    _performUiEvent.call(UiEvent.SetResult)
                    _performUiEvent.call(UiEvent.DisplayLoadingState(LoadingState.Success))
                    _performUiEvent.call(UiEvent.Exit)
                }
                .onError { throwable ->
                    _performUiEvent.call(UiEvent.DisplayLoadingState(LoadingState.Error(throwable)))
                }
    }

    private fun onValidationError(validationErrors: ValidationErrors) {
        _performUiEvent.call(UiEvent.DisplayValidationResults(validationErrors))
    }

}