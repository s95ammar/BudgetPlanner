package com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.periodcreateedit

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.s95ammar.budgetplanner.models.datasource.local.db.entity.CategoryOfPeriodEntity
import com.s95ammar.budgetplanner.models.datasource.local.db.entity.PeriodEntity
import com.s95ammar.budgetplanner.models.repository.PeriodRepository
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.common.data.CategoryOfPeriod
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.common.data.PeriodSimple
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.common.data.PeriodWithCategoriesOfPeriod
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.periodcreateedit.data.PeriodCreateEditUiEvent
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.periodcreateedit.data.PeriodInputBundle
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.periodcreateedit.validation.PeriodCreateEditValidator
import com.s95ammar.budgetplanner.ui.common.CreateEditMode
import com.s95ammar.budgetplanner.ui.common.LoadingState
import com.s95ammar.budgetplanner.ui.common.validation.ValidationErrors
import com.s95ammar.budgetplanner.ui.main.data.Currency
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
    private val _periodWithCategoriesOfPeriod = LoaderMutableLiveData<PeriodWithCategoriesOfPeriod> { loadEditedPeriodOrInsertTemplate() }

    private val _name = MediatorLiveData<String>().apply {
        addSource(_periodWithCategoriesOfPeriod) { value = it.periodName ?: CalendarUtil.getNextMonthPeriodName(locale) }
    }
    private val _categoriesOfPeriod = MediatorLiveData<List<CategoryOfPeriod>>().apply {
        addSource(_periodWithCategoriesOfPeriod) { value = it.categoriesOfPeriod }
    }
    private val _allowCategorySelectionForAll = MediatorLiveData<Boolean>().apply {
        addSource(_mode) { value = it == CreateEditMode.CREATE }
    }
    private val _performUiEvent = EventMutableLiveData<PeriodCreateEditUiEvent>()

    val mode = _mode.asLiveData()
    val name = _name.asLiveData()
    val categoriesOfPeriod = _categoriesOfPeriod.asLiveData()
    val selectedCategoriesOfPeriod = _categoriesOfPeriod.map { list -> list.filter { it.isSelected } }
    val allowCategorySelectionForAll = _allowCategorySelectionForAll.asLiveData()
    val performUiEvent = _performUiEvent.asEventLiveData()

    fun onCategoryOfPeriodCurrencyChanged(categoryOfPeriod: CategoryOfPeriod, currency: Currency) {
        _categoriesOfPeriod.value = _categoriesOfPeriod.value.orEmpty().map { listItem ->
            if (listItem.categoryId == categoryOfPeriod.categoryId) listItem.copy(currencyCode = currency.code) else listItem
        }
    }

    fun onCategoryOfPeriodSelectionStateChanged(categoryOfPeriod: CategoryOfPeriod, isSelected: Boolean) {
        _categoriesOfPeriod.value = _categoriesOfPeriod.value.orEmpty().map { listItem ->
            if (listItem.categoryId == categoryOfPeriod.categoryId) listItem.copy(isSelected = isSelected) else listItem
        }
    }

    fun onCategoryOfPeriodEstimateChanged(categoryOfPeriod: CategoryOfPeriod, estimate: Double?) {
        _categoriesOfPeriod.value = _categoriesOfPeriod.value.orEmpty().map { listItem ->
            if (listItem.categoryId == categoryOfPeriod.categoryId) listItem.copy(estimate = estimate) else listItem
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
                .collect { categoryOfPeriodJoinEntityList ->
                    _periodWithCategoriesOfPeriod.value = PeriodWithCategoriesOfPeriod(
                        periodId = editedPeriodId,
                        periodName = editedPeriod?.name,
                        categoriesOfPeriod = categoryOfPeriodJoinEntityList.mapNotNull(CategoryOfPeriod.JoinEntityMapper::fromEntity)
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

    private fun getInsertFlow(period: PeriodEntity) = repository.insertPeriodWithCategoriesOfPeriodFlow(
        period = period,
        categoriesOfPeriod = _categoriesOfPeriod.value.orEmpty()
            .filter { it.isSelected }
            .mapNotNull(CategoryOfPeriod.EntityMapper::toEntity)
    )

    private fun getUpdateFlow(period: PeriodEntity): Flow<Unit> {
        val initialCoPList = _periodWithCategoriesOfPeriod.value?.categoriesOfPeriod.orEmpty().filter { it.isSelected }
        val finalCoPList = categoriesOfPeriod.value.orEmpty().filter { it.isSelected }

        val initialCoPCategoryIdsList = initialCoPList.map { it.categoryId }
        val finalCoPCategoryIdsList = finalCoPList.map { it.categoryId }

        val pcCategoryIdsToDelete = mutableListOf<Int>()
        val categoriesOfPeriodToUpdate = mutableListOf<CategoryOfPeriodEntity>()
        val categoriesOfPeriodToInsert = mutableListOf<CategoryOfPeriodEntity>()
        val categoriesOfPeriodIdsUnion = initialCoPCategoryIdsList.union(finalCoPCategoryIdsList)

        for (categoryId in categoriesOfPeriodIdsUnion) {
            
            if (categoryId in initialCoPCategoryIdsList && categoryId !in finalCoPCategoryIdsList) {
                pcCategoryIdsToDelete.add(initialCoPList.first { it.categoryId == categoryId }.id)
                continue
            }

            if (categoryId !in initialCoPCategoryIdsList && categoryId in finalCoPCategoryIdsList) {
                val categoryOfPeriod = finalCoPList.first { it.categoryId == categoryId }
                CategoryOfPeriod.EntityMapper.toEntity(categoryOfPeriod)?.let { categoriesOfPeriodToInsert.add(it) }
                continue
            }
            
            if (categoryId in initialCoPCategoryIdsList && categoryId in finalCoPCategoryIdsList) {
                val pcInitial = initialCoPList.first { it.categoryId == categoryId }
                val pcFinal = finalCoPList.first { it.categoryId == categoryId }
                if (pcInitial != pcFinal)
                    CategoryOfPeriod.EntityMapper.toEntity(pcFinal)?.let { categoriesOfPeriodToUpdate.add(it) }
            }
        }

        return repository.updatePeriodWithCategoriesOfPeriodFlow(
            period,
            pcCategoryIdsToDelete,
            categoriesOfPeriodToUpdate,
            categoriesOfPeriodToInsert
        )
    }

    private fun displayValidationResults(validationErrors: ValidationErrors) {
        _performUiEvent.call(PeriodCreateEditUiEvent.DisplayValidationResults(validationErrors))
    }

}