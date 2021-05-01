package com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.budgetransactioncreateedit

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.s95ammar.budgetplanner.models.IntBudgetTransactionType
import com.s95ammar.budgetplanner.models.repository.BudgetTransactionRepository
import com.s95ammar.budgetplanner.ui.appscreens.categories.common.data.Category
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.common.data.BudgetTransactionViewEntity
import com.s95ammar.budgetplanner.ui.common.CreateEditMode
import com.s95ammar.budgetplanner.util.NO_ITEM
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

    private val budgetTransactionId = savedStateHandle.get<Int>(BudgetTransactionCreateEditFragmentArgs::budgetTransactionId.name) ?: Int.NO_ITEM

    private val _mode = MutableLiveData(CreateEditMode.getById(budgetTransactionId))
    private val _budgetTransaction = LoaderMutableLiveData<BudgetTransactionViewEntity> {
        if (_mode.value == CreateEditMode.EDIT) loadEditedBudgetTransaction()
    }

    private val _selectedType = MutableLiveData<@IntBudgetTransactionType Int>()
    private val _selectedCategory = MutableLiveData<Category>()


    private val _name = MediatorLiveData<String>().apply {
        addSource(_budgetTransaction) { value = it.name }
    }
    private val _amount = MediatorLiveData<Int>().apply {
        addSource(_budgetTransaction) { value = it.amount }
    }
    private val _category = MediatorLiveData<Category>().apply {
        addSource(_budgetTransaction) { value = Category(it.periodicCategoryId, it.categoryName) }
    }
    private val _performUiEvent = EventMutableLiveData<UiEvent>()

    val mode = _mode.asLiveData()
    val selectedCategory = _selectedCategory.asLiveData()
    val performUiEvent = _performUiEvent.asEventLiveData()
/*
    val name = _name.asLiveData()
    val max = _max.asLiveData()
    val periodicCategories = _periodicCategories.asLiveData()

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
*/
    fun setSelectedBudgetTransactionType(position: Int) {
        IntBudgetTransactionType.values().getOrNull(position)?.let { type ->
            _selectedType.value = type
        }
    }

    fun onChooseCategory() {
        _performUiEvent.call(UiEvent.ChooseCategory)
    }

    fun setCategory(category: Category) {
        _selectedCategory.value = category
    }

    fun onBack() {
        _performUiEvent.call(UiEvent.Exit)
    }

    private fun loadEditedBudgetTransaction() {
//        repository.getBudgetTransactionsForPeriod()
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