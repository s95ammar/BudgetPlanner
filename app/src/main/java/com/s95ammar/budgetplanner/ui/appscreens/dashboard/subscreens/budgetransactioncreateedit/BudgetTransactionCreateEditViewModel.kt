package com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.budgetransactioncreateedit

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.viewModelScope
import com.s95ammar.budgetplanner.models.IntBudgetTransactionType
import com.s95ammar.budgetplanner.models.datasource.local.db.entity.BudgetTransactionEntity
import com.s95ammar.budgetplanner.models.repository.BudgetTransactionRepository
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.common.data.BudgetTransaction
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.budgetransactioncreateedit.data.BudgetTransactionInputBundle
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.budgetransactioncreateedit.data.PeriodicCategoryIdAndName
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.budgetransactioncreateedit.validation.BudgetTransactionCreateEditValidator
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.budgetransactioncreateedit.validation.BudgetTransactionValidationBundle
import com.s95ammar.budgetplanner.ui.common.CreateEditMode
import com.s95ammar.budgetplanner.ui.common.LoadingState
import com.s95ammar.budgetplanner.ui.common.validation.ValidationErrors
import com.s95ammar.budgetplanner.util.INVALID
import com.s95ammar.budgetplanner.util.lifecycleutil.EventMutableLiveData
import com.s95ammar.budgetplanner.util.lifecycleutil.LoaderMutableLiveData
import com.s95ammar.budgetplanner.util.lifecycleutil.MediatorLiveData
import com.s95ammar.budgetplanner.util.lifecycleutil.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.budgetransactioncreateedit.BudgetTransactionCreateEditFragmentArgs as FragmentArgs
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.budgetransactioncreateedit.data.BudgetTransactionCreateEditUiEvent as UiEvent

@HiltViewModel
class BudgetTransactionCreateEditViewModel @Inject constructor(
    private val repository: BudgetTransactionRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val editedBudgetTransactionId = savedStateHandle.get<Int>(FragmentArgs::budgetTransactionId.name) ?: Int.INVALID
    private val periodId = savedStateHandle.get<Int>(FragmentArgs::periodId.name) ?: Int.INVALID

    private val _mode = MutableLiveData(CreateEditMode.getById(editedBudgetTransactionId))
    private val _editedBudgetTransaction = LoaderMutableLiveData<BudgetTransaction> {
        if (_mode.value == CreateEditMode.EDIT) loadEditedBudgetTransaction()
    }

    private val _type = MediatorLiveData(IntBudgetTransactionType.EXPENSE).apply {
        addSource(_editedBudgetTransaction) { value = it.type }
    }
    private val _name = MediatorLiveData<String>().apply {
        addSource(_editedBudgetTransaction) { value = it.name }
    }
    private val _amount = MediatorLiveData<Int>().apply {
        addSource(_editedBudgetTransaction) { value = it.amount }
    }
    private val _periodicCategory = MediatorLiveData<PeriodicCategoryIdAndName>().apply {
        addSource(_editedBudgetTransaction) { value = PeriodicCategoryIdAndName(it.periodicCategoryId, it.categoryName) }
    }
    private val _performUiEvent = EventMutableLiveData<UiEvent>()

    val type = _type.distinctUntilChanged()
    val name = _name.distinctUntilChanged()
    val amount = _amount.distinctUntilChanged()
    val mode = _mode.asLiveData()

    val periodicCategory = _periodicCategory.asLiveData()
    val performUiEvent = _performUiEvent.asEventLiveData()

    fun setType(@IntBudgetTransactionType type: Int) {
        _type.value = type
    }

    fun setName(name: String) {
        _name.value = name
    }

    fun setAmount(amount: String) {
        amount.toIntOrNull()?.let { _amount.value = it }
    }

    fun setPeriodicCategory(periodicCategory: PeriodicCategoryIdAndName) {
        _periodicCategory.value = periodicCategory
    }

    fun onChoosePeriodicCategory() {
        _performUiEvent.call(UiEvent.ChoosePeriodicCategory(periodId))
    }

    fun onApply(budgetTransactionInputBundle: BudgetTransactionInputBundle) {
        val validator = createValidator(budgetTransactionInputBundle)
        _performUiEvent.call(UiEvent.DisplayValidationResults(validator.getValidationErrors(allBlank = true)))

        validator.getValidationResult()
            .onSuccess { insertOrUpdateBudgetTransaction(it) }
            .onError { displayValidationResults(it) }
    }

    fun onBack() {
        _performUiEvent.call(UiEvent.Exit)
    }

    private fun createValidator(budgetTransactionInputBundle: BudgetTransactionInputBundle): BudgetTransactionCreateEditValidator {
        val validationBundle = BudgetTransactionValidationBundle(
            type = _type.value ?: IntBudgetTransactionType.EXPENSE,
            name = budgetTransactionInputBundle.name,
            amount = budgetTransactionInputBundle.amount,
            periodicCategoryId = _periodicCategory.value?.periodicCategoryId ?: Int.INVALID
        )

        return BudgetTransactionCreateEditValidator(_editedBudgetTransaction.value, validationBundle)
    }

    private fun displayValidationResults(validationErrors: ValidationErrors) {
        _performUiEvent.call(UiEvent.DisplayValidationResults(validationErrors))
    }

    private fun loadEditedBudgetTransaction() {
        viewModelScope.launch {
            repository.getBudgetTransactionFlow(editedBudgetTransactionId)
                .onStart {
                    _performUiEvent.call(UiEvent.DisplayLoadingState(LoadingState.Loading))
                }
                .catch { throwable ->
                    _performUiEvent.call(UiEvent.DisplayLoadingState(LoadingState.Error(throwable)))
                }
                .collect { budgetTransaction ->
                    _editedBudgetTransaction.value = BudgetTransaction.Mapper.fromEntity(budgetTransaction)
                    _performUiEvent.call(UiEvent.DisplayLoadingState(LoadingState.Success))
                }
        }
    }

    private fun insertOrUpdateBudgetTransaction(budgetTransaction: BudgetTransactionEntity) = viewModelScope.launch {
        val mode = _mode.value ?: return@launch

        val flowRequest = when (mode) {
            CreateEditMode.CREATE -> repository.insertBudgetTransactionFlow(budgetTransaction)
            CreateEditMode.EDIT -> repository.updateBudgetTransactionFlow(budgetTransaction)
        }

        flowRequest
            .onStart {
                _performUiEvent.call(UiEvent.DisplayLoadingState(LoadingState.Loading))
            }
            .catch { throwable ->
                _performUiEvent.call(UiEvent.DisplayLoadingState(LoadingState.Error(throwable)))
            }
            .collect {
                _performUiEvent.call(UiEvent.DisplayLoadingState(LoadingState.Success))
                _performUiEvent.call(UiEvent.Exit)
            }

    }

}