package com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.periodcreateedit

import androidx.lifecycle.*
import com.s95ammar.budgetplanner.models.datasource.local.db.entity.PeriodEntity
import com.s95ammar.budgetplanner.models.repository.PeriodRepository
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.common.data.PeriodFull
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.common.data.PeriodicCategory
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.periodcreateedit.data.PeriodCreateEditUiEvent
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.periodcreateedit.data.PeriodInputBundle
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.periodcreateedit.validation.PeriodCreateEditValidator
import com.s95ammar.budgetplanner.ui.common.CreateEditMode
import com.s95ammar.budgetplanner.ui.common.validation.ValidationErrors
import com.s95ammar.budgetplanner.util.NO_ITEM
import com.s95ammar.budgetplanner.util.lifecycleutil.EventMutableLiveData
import com.s95ammar.budgetplanner.util.lifecycleutil.LoaderMutableLiveData
import com.s95ammar.budgetplanner.util.lifecycleutil.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PeriodCreateEditSharedViewModel @Inject constructor(
    private val repository: PeriodRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val editedPeriodId = savedStateHandle.get<Int>(PeriodCreateEditFragmentArgs::periodId.name) ?: Int.NO_ITEM

    private val _mode = MutableLiveData(CreateEditMode.getById(editedPeriodId))
    private val _period = LoaderMutableLiveData<PeriodFull> { loadEditedPeriodOrInsertTemplate() }

    private val _name = MediatorLiveData<String>().apply {
        addSource(_period) { value = it.name }
    }
    private val _max = MediatorLiveData<Int>().apply {
        addSource(_period) { value = it.max }
    }
    private val _periodicCategories = MediatorLiveData<List<PeriodicCategory>>().apply {
        addSource(_period) { value = it.periodicCategories }
    }

    private val _alwaysAllowCategorySelection = MediatorLiveData<Boolean>().apply {
        addSource(_mode) { value = it == CreateEditMode.CREATE }
    }

    private val _performUiEvent = EventMutableLiveData<PeriodCreateEditUiEvent>()

    val mode = _mode.asLiveData()
    val name = _name.asLiveData()
    val max = _max.asLiveData()
    val periodicCategories = _periodicCategories.asLiveData()
    val selectedPeriodicCategories = _periodicCategories.map { list -> list.filter { it.isSelected } }
    val alwaysAllowCategorySelection = _alwaysAllowCategorySelection.asLiveData()
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
            .onError { onValidationError(it) }

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
        // TODO
/*
        val mode = _mode.value ?: return

        viewModelScope.launch {
            val flowRequest = when (mode) {
                CreateEditMode.EDIT -> repository.getPeriodicCategoryJoinEntityList(editedPeriodId)
                CreateEditMode.CREATE -> repository.getPeriodInsertTemplate()
            }

            flowRequest
                .onStart {
                    _performUiEvent.call(PeriodCreateEditUiEvent.DisplayLoadingState(LoadingState.Loading))
                }
                .catch { throwable ->
                    _performUiEvent.call(PeriodCreateEditUiEvent.DisplayLoadingState(LoadingState.Error(throwable)))
                }
                .collect { periodEntity ->
                    PeriodViewEntity.EntityMapper.toViewEntity(periodEntity)?.let { periodViewEntity ->
                        _period.value = periodViewEntity
                        _performUiEvent.call(PeriodCreateEditUiEvent.DisplayLoadingState(LoadingState.Success))
                    }
                }
        }
*/
    }

    private fun insertOrUpdatePeriod(period: PeriodEntity) = viewModelScope.launch {
        // TODO
/*
            val flowRequest = when (request) {
                is PeriodUpsertApiRequest.Insertion -> repository.insertPeriod(request)
                is PeriodUpsertApiRequest.Update -> repository.updatePeriod(request)
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
*/
    }

    private fun onValidationError(validationErrors: ValidationErrors) {
        _performUiEvent.call(PeriodCreateEditUiEvent.DisplayValidationResults(validationErrors))
    }

}