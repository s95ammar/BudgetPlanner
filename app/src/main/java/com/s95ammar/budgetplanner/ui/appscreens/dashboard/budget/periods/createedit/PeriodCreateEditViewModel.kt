package com.s95ammar.budgetplanner.ui.appscreens.dashboard.budget.periods.createedit

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.s95ammar.budgetplanner.models.api.common.PeriodApiEntity
import com.s95ammar.budgetplanner.models.mappers.PeriodApiViewMapper
import com.s95ammar.budgetplanner.models.repository.LocalRepository
import com.s95ammar.budgetplanner.models.repository.RemoteRepository
import com.s95ammar.budgetplanner.models.view.PeriodViewEntity
import com.s95ammar.budgetplanner.ui.appscreens.auth.common.LoadingState
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.budget.periods.createedit.data.PeriodInputBundle
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.budget.periods.createedit.validation.PeriodCreateEditValidator
import com.s95ammar.budgetplanner.ui.common.CreateEditMode
import com.s95ammar.budgetplanner.ui.common.validation.ValidationErrors
import com.s95ammar.budgetplanner.util.NO_ITEM
import com.s95ammar.budgetplanner.util.lifecycleutil.EventMutableLiveData
import com.s95ammar.budgetplanner.util.lifecycleutil.EventMutableLiveDataVoid
import com.s95ammar.budgetplanner.util.lifecycleutil.LoaderMutableLiveData
import com.s95ammar.budgetplanner.util.lifecycleutil.asLiveData
import kotlinx.coroutines.launch

class PeriodCreateEditViewModel @ViewModelInject constructor(
    private val localRepository: LocalRepository,
    private val remoteRepository: RemoteRepository,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val editedPeriodId = savedStateHandle.get<Int>(PeriodCreateEditFragmentArgs::periodId.name) ?: Int.NO_ITEM

    private val _mode = MutableLiveData(CreateEditMode.getById(editedPeriodId))
    private val _editedPeriod = LoaderMutableLiveData<PeriodViewEntity> { if (_mode.value == CreateEditMode.EDIT) loadEditedPeriod() }
    private val _displayLoadingState = EventMutableLiveData<LoadingState>(LoadingState.Cold)
    private val _onApplySuccess = EventMutableLiveDataVoid()
    private val _displayValidationResults = EventMutableLiveData<ValidationErrors>()

    val mode = _mode.asLiveData()
    val editedPeriod = _editedPeriod.asLiveData()
    val displayLoadingState = _displayLoadingState.asEventLiveData()
    val onApplySuccess = _onApplySuccess.asEventLiveData()
    val displayValidationResults = _displayValidationResults.asEventLiveData()

    fun onApply(periodInputBundle: PeriodInputBundle) {
        val validator = PeriodCreateEditValidator(editedPeriodId, periodInputBundle)
        _displayValidationResults.call(validator.getValidationErrors(allBlank = true))

        validator.getValidationResult()
            .onSuccess { onValidationSuccessful(it) }
            .onError { onValidationError(it) }

    }

    private fun loadEditedPeriod() {
        viewModelScope.launch {
            _displayLoadingState.call(LoadingState.Loading)
            remoteRepository.getPeriod(editedPeriodId)
                .onSuccess { periodApiEntity ->
                    val period = periodApiEntity.orEmpty()
                        .map { apiEntity -> PeriodApiViewMapper.toViewEntity(apiEntity) }
                        .singleOrNull()
                    _editedPeriod.value = period
                    _displayLoadingState.call(LoadingState.Success)
                }
                .onError { _displayLoadingState.call(LoadingState.Error(it)) }
        }

    }

    private fun onValidationSuccessful(period: PeriodApiEntity) = viewModelScope.launch {
        _mode.value?.let { mode ->
            _displayLoadingState.call(LoadingState.Loading)
            val result = when (mode) {
                CreateEditMode.CREATE -> remoteRepository.insertPeriod(period)
                CreateEditMode.EDIT -> remoteRepository.updatePeriod(period)
            }

            result
                .onSuccess {
                    _onApplySuccess.call()
                    _displayLoadingState.call(LoadingState.Success)
                }
                .onError { throwable ->
                    _displayLoadingState.call(LoadingState.Error(throwable))
                }
        }
    }

    private fun onValidationError(validationErrors: ValidationErrors) {
        _displayValidationResults.call(validationErrors)
    }


}