package com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.periodcreateedit

import androidx.lifecycle.*
import com.s95ammar.budgetplanner.models.datasource.local.db.entity.PeriodEntity
import com.s95ammar.budgetplanner.models.repository.PeriodRepository
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.common.data.PeriodWithPeriodicCategories
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.common.data.PeriodicCategory
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.periodcreateedit.data.PeriodCreateEditUiEvent
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.periodcreateedit.data.PeriodInputBundle
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.periodcreateedit.validation.PeriodCreateEditValidator
import com.s95ammar.budgetplanner.ui.common.CreateEditMode
import com.s95ammar.budgetplanner.ui.common.LoadingState
import com.s95ammar.budgetplanner.ui.common.validation.ValidationErrors
import com.s95ammar.budgetplanner.util.CalendarUtil
import com.s95ammar.budgetplanner.util.INVALID
import com.s95ammar.budgetplanner.util.lifecycleutil.EventMutableLiveData
import com.s95ammar.budgetplanner.util.lifecycleutil.LoaderMutableLiveData
import com.s95ammar.budgetplanner.util.lifecycleutil.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class PeriodCreateEditSharedViewModel @Inject constructor(
    private val repository: PeriodRepository,
    private val savedStateHandle: SavedStateHandle,
    private val locale: Locale
) : ViewModel() {

    private val editedPeriodId = savedStateHandle.get<Int>(PeriodCreateEditFragmentArgs::periodId.name) ?: Int.INVALID

    private val _mode = MutableLiveData(CreateEditMode.getById(editedPeriodId))
    private val _periodWithPeriodicCategories = LoaderMutableLiveData<PeriodWithPeriodicCategories> { loadEditedPeriodOrInsertTemplate() }

    private val _name = MediatorLiveData<String>().apply {
        addSource(_periodWithPeriodicCategories) { value = it.periodName ?: CalendarUtil.getNextMonthPeriodName(locale) }
    }
    private val _max = MediatorLiveData<Int>().apply {
        addSource(_periodWithPeriodicCategories) { value = it.max }
    }
    private val _periodicCategories = MediatorLiveData<List<PeriodicCategory>>().apply {
        addSource(_periodWithPeriodicCategories) { value = it.periodicCategories }
    }
    private val _allowCategorySelectionForAll = MediatorLiveData<Boolean>().apply {
        addSource(_mode) { value = it == CreateEditMode.CREATE }
    }
    private val _performUiEvent = EventMutableLiveData<PeriodCreateEditUiEvent>()

    val mode = _mode.asLiveData()
    val name = _name.asLiveData()
    val max = _max.asLiveData()
    val periodicCategories = _periodicCategories.asLiveData()
    val selectedPeriodicCategories = _periodicCategories.map { list -> list.filter { it.isSelected } }
    val allowCategorySelectionForAll = _allowCategorySelectionForAll.asLiveData()
    val performUiEvent = _performUiEvent.asEventLiveData()

    fun onPeriodicCategorySelectionStateChanged(position: Int, isSelected: Boolean) {
        _periodicCategories.value = _periodicCategories.value.orEmpty().mapIndexed { i, periodicCategory ->
//            periodicCategory.copy(isSelected = if (i == position) isSelected else periodicCategory.isSelected)
            if (i == position) periodicCategory.copy(isSelected = isSelected) else periodicCategory
        }
    }

    fun onPeriodicCategoryMaxChanged(position: Int, maxInput: String?) {
        _periodicCategories.value?.getOrNull(position)?.let { periodicCategory ->
            periodicCategory.max = maxInput?.toIntOrNull()
        }
    }

    fun onApply(periodInputBundle: PeriodInputBundle) {
        val validator = createValidator(periodInputBundle)
        _performUiEvent.call(PeriodCreateEditUiEvent.DisplayValidationResults(validator.getValidationErrors(allBlank = true)))

        validator.getValidationResult()
            .onSuccess { insertOrUpdatePeriod(it) }
            .onError { displayValidationResults(it) }

    }

    private fun createValidator(periodInputBundle: PeriodInputBundle): PeriodCreateEditValidator {
        val periodicCategoryUpsertApiRequestListGetter = {
            _periodicCategories.value.orEmpty()
                .filter { it.isSelected }
//                .map { PeriodicCategoryUpsertApiRequest(it.categoryId, it.max) }
        }

        return PeriodCreateEditValidator(editedPeriodId, /*periodicCategoryUpsertApiRequestListGetter,*/ periodInputBundle)
    }

    private fun loadEditedPeriodOrInsertTemplate() {
        val mode = _mode.value ?: return

        viewModelScope.launch {
            val flowRequest = when (mode) {
                CreateEditMode.EDIT -> repository.getPeriodicCategoryJoinEntityListFlow(editedPeriodId)
                CreateEditMode.CREATE -> repository.getPeriodInsertTemplate()
            }

            flowRequest
                .onStart {
                    _performUiEvent.call(PeriodCreateEditUiEvent.DisplayLoadingState(LoadingState.Loading))
                }
                .catch { throwable ->
                    _performUiEvent.call(PeriodCreateEditUiEvent.DisplayLoadingState(LoadingState.Error(throwable)))
                }
                .collect { periodicCategoryJoinEntityList ->
                    _periodWithPeriodicCategories.value = PeriodWithPeriodicCategories(
                        periodId = editedPeriodId,
                        periodName = null /*TODO: change editedPeriodId to periodSimple*/,
                        max = null /*TODO: change editedPeriodId to periodSimple*/,
                        periodicCategories = periodicCategoryJoinEntityList.mapNotNull(PeriodicCategory.Mapper::fromEntity)
                    )
                    _performUiEvent.call(PeriodCreateEditUiEvent.DisplayLoadingState(LoadingState.Success))
                }
        }
    }

    private fun insertOrUpdatePeriod(period: PeriodEntity) = viewModelScope.launch {
        val mode = _mode.value ?: return@launch

        val flowRequest = when (mode) {
            CreateEditMode.CREATE -> repository.insertPeriodFlow(period) // TODO: implement adding/updating/deleting periodic categories
            CreateEditMode.EDIT -> repository.updatePeriodFlow(period) // TODO: implement adding/updating/deleting periodic categories
        }

        flowRequest
            .onStart {
                _performUiEvent.call(PeriodCreateEditUiEvent.DisplayLoadingState(LoadingState.Loading))
            }
            .catch { throwable ->
                _performUiEvent.call(PeriodCreateEditUiEvent.DisplayLoadingState(LoadingState.Error(throwable)))
            }
            .collect {
                _performUiEvent.call(PeriodCreateEditUiEvent.SetResult)
                _performUiEvent.call(PeriodCreateEditUiEvent.DisplayLoadingState(LoadingState.Success))
                _performUiEvent.call(PeriodCreateEditUiEvent.Exit)
            }
    }

    private fun displayValidationResults(validationErrors: ValidationErrors) {
        _performUiEvent.call(PeriodCreateEditUiEvent.DisplayValidationResults(validationErrors))
    }

}