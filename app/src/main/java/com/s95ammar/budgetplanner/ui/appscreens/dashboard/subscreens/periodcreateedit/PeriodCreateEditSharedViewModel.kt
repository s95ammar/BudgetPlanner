package com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.periodcreateedit

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.s95ammar.budgetplanner.models.datasource.local.db.entity.PeriodEntity
import com.s95ammar.budgetplanner.models.datasource.local.db.entity.PeriodicCategoryEntity
import com.s95ammar.budgetplanner.models.repository.PeriodRepository
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.common.data.PeriodSimple
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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.periodcreateedit.PeriodCreateEditFragmentArgs as FragmentArgs

@HiltViewModel
class PeriodCreateEditSharedViewModel @Inject constructor(
    private val repository: PeriodRepository,
    private val savedStateHandle: SavedStateHandle,
    private val locale: Locale
) : ViewModel() {

    private val editedPeriod = savedStateHandle.get<PeriodSimple?>(FragmentArgs::period.name)
    private val editedPeriodId
        get() = editedPeriod?.id ?: Int.INVALID

    private val _mode = MutableLiveData(CreateEditMode.getById(editedPeriodId))
    private val _periodWithPeriodicCategories = LoaderMutableLiveData<PeriodWithPeriodicCategories> { loadEditedPeriodOrInsertTemplate() }

    private val _name = MediatorLiveData<String>().apply {
        addSource(_periodWithPeriodicCategories) { value = it.periodName ?: CalendarUtil.getNextMonthPeriodName(locale) }
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
    val periodicCategories = _periodicCategories.asLiveData()
    val selectedPeriodicCategories = _periodicCategories.map { list -> list.filter { it.isSelected } }
    val allowCategorySelectionForAll = _allowCategorySelectionForAll.asLiveData()
    val performUiEvent = _performUiEvent.asEventLiveData()

    fun onPeriodicCategorySelectionStateChanged(periodicCategory: PeriodicCategory, isSelected: Boolean) {
        _periodicCategories.value = _periodicCategories.value.orEmpty().map { listItem ->
            if (listItem.id == periodicCategory.id) listItem.copy(isSelected = isSelected) else listItem
        }
    }

    fun onPeriodicCategoryEstimateChanged(periodicCategory: PeriodicCategory, estimate: Double?) {
        _periodicCategories.value = _periodicCategories.value.orEmpty().map { listItem ->
            if (listItem.id == periodicCategory.id) listItem.copy(estimate = estimate) else listItem
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
        return PeriodCreateEditValidator(editedPeriodId, periodInputBundle)
    }

    private fun loadEditedPeriodOrInsertTemplate() {
        val mode = _mode.value ?: return

        viewModelScope.launch {
            val flowRequest = when (mode) {
                CreateEditMode.EDIT -> repository.getPeriodEditDataFlow(editedPeriodId)
                CreateEditMode.CREATE -> repository.getPeriodInsertTemplateFlow()
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
                        periodName = editedPeriod?.name,
                        periodicCategories = periodicCategoryJoinEntityList.mapNotNull(PeriodicCategory.JoinEntityMapper::fromEntity)
                    )
                    _performUiEvent.call(PeriodCreateEditUiEvent.DisplayLoadingState(LoadingState.Success))
                }
        }
    }

    private fun insertOrUpdatePeriod(period: PeriodEntity) = viewModelScope.launch {
        val mode = _mode.value ?: return@launch

        val flowRequest = when (mode) {
            CreateEditMode.CREATE -> getInsertFlow(period)
            CreateEditMode.EDIT -> getUpdateFlow(period)
        }

        flowRequest
            .onStart {
                _performUiEvent.call(PeriodCreateEditUiEvent.DisplayLoadingState(LoadingState.Loading))
            }
            .catch { throwable ->
                _performUiEvent.call(PeriodCreateEditUiEvent.DisplayLoadingState(LoadingState.Error(throwable)))
            }
            .collect {
                _performUiEvent.call(PeriodCreateEditUiEvent.DisplayLoadingState(LoadingState.Success))
                _performUiEvent.call(PeriodCreateEditUiEvent.Exit)
            }
    }

    private fun getInsertFlow(period: PeriodEntity) = repository.insertPeriodWithPeriodicCategoriesFlow(
        period = period,
        periodicCategories = _periodicCategories.value.orEmpty()
            .filter { it.isSelected }
            .mapNotNull(PeriodicCategory.EntityMapper::toEntity)
    )

    private fun getUpdateFlow(period: PeriodEntity): Flow<Unit> {
        val initialPcList = _periodWithPeriodicCategories.value?.periodicCategories.orEmpty().filter { it.isSelected }
        val finalPcList = periodicCategories.value.orEmpty().filter { it.isSelected }

        val initialPcCategoryIdsList = initialPcList.map { it.categoryId }
        val finalPcCategoryIdsList = finalPcList.map { it.categoryId }

        val pcCategoryIdsToDelete = mutableListOf<Int>()
        val periodicCategoriesToUpdate = mutableListOf<PeriodicCategoryEntity>()
        val periodicCategoriesToInsert = mutableListOf<PeriodicCategoryEntity>()
        val periodicCategoriesIdsUnion = initialPcCategoryIdsList.union(finalPcCategoryIdsList)

        for (categoryId in periodicCategoriesIdsUnion) {
            
            if (categoryId in initialPcCategoryIdsList && categoryId !in finalPcCategoryIdsList) {
                pcCategoryIdsToDelete.add(initialPcList.first { it.categoryId == categoryId }.id)
                continue
            }

            if (categoryId !in initialPcCategoryIdsList && categoryId in finalPcCategoryIdsList) {
                val pc = finalPcList.first { it.categoryId == categoryId }
                PeriodicCategory.EntityMapper.toEntity(pc)?.let { periodicCategoriesToInsert.add(it) }
                continue
            }
            
            if (categoryId in initialPcCategoryIdsList && categoryId in finalPcCategoryIdsList) {
                val pcInitial = initialPcList.first { it.categoryId == categoryId }
                val pcFinal = finalPcList.first { it.categoryId == categoryId }
                if (pcInitial != pcFinal)
                    PeriodicCategory.EntityMapper.toEntity(pcFinal)?.let { periodicCategoriesToUpdate.add(it) }
            }
        }

        return repository.updatePeriodWithPeriodicCategoriesFlow(
            period,
            pcCategoryIdsToDelete,
            periodicCategoriesToUpdate,
            periodicCategoriesToInsert
        )
    }

    private fun displayValidationResults(validationErrors: ValidationErrors) {
        _performUiEvent.call(PeriodCreateEditUiEvent.DisplayValidationResults(validationErrors))
    }

}