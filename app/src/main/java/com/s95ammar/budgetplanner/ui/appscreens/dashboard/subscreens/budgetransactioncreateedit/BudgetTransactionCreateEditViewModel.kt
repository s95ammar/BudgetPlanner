package com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.budgetransactioncreateedit

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.s95ammar.budgetplanner.models.IntBudgetTransactionType
import com.s95ammar.budgetplanner.models.datasource.local.db.entity.BudgetTransactionEntity
import com.s95ammar.budgetplanner.models.repository.BudgetTransactionRepository
import com.s95ammar.budgetplanner.models.repository.CurrencyRepository
import com.s95ammar.budgetplanner.models.repository.LocationRepository
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.common.data.BudgetTransaction
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.budgetransactioncreateedit.data.BudgetTransactionInputBundle
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.budgetransactioncreateedit.data.PeriodicCategorySimple
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.budgetransactioncreateedit.subscreens.locationselection.data.LocationWithAddress
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.budgetransactioncreateedit.validation.BudgetTransactionCreateEditValidator
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.budgetransactioncreateedit.validation.BudgetTransactionValidationBundle
import com.s95ammar.budgetplanner.ui.common.CreateEditMode
import com.s95ammar.budgetplanner.ui.common.LoadingState
import com.s95ammar.budgetplanner.ui.common.validation.ValidationErrors
import com.s95ammar.budgetplanner.util.INVALID
import com.s95ammar.budgetplanner.util.Optional
import com.s95ammar.budgetplanner.util.asOptional
import com.s95ammar.budgetplanner.util.lifecycleutil.EventMutableLiveData
import com.s95ammar.budgetplanner.util.lifecycleutil.LoaderMutableLiveData
import com.s95ammar.budgetplanner.util.lifecycleutil.MediatorLiveData
import com.s95ammar.budgetplanner.util.lifecycleutil.asLiveData
import com.s95ammar.budgetplanner.util.optionalValue
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.absoluteValue
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.budgetransactioncreateedit.BudgetTransactionCreateEditFragmentArgs as FragmentArgs
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.budgetransactioncreateedit.data.BudgetTransactionCreateEditUiEvent as UiEvent

@HiltViewModel
class BudgetTransactionCreateEditViewModel @Inject constructor(
    private val budgetTransactionRepository: BudgetTransactionRepository,
    private val currencyRepository: CurrencyRepository,
    private val locationRepository: LocationRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val editedBudgetTransactionId = savedStateHandle.get<Int>(FragmentArgs::budgetTransactionId.name) ?: Int.INVALID
    private val periodId = savedStateHandle.get<Int>(FragmentArgs::periodId.name) ?: Int.INVALID

    private val _mode = MutableLiveData(CreateEditMode.getById(editedBudgetTransactionId))
    private val _editedBudgetTransaction = LoaderMutableLiveData<BudgetTransaction> {
        if (_mode.value == CreateEditMode.EDIT) loadEditedBudgetTransaction()
    }

    private val _type = MediatorLiveData(IntBudgetTransactionType.EXPENSE).apply {
        addSource(_editedBudgetTransaction) { value = IntBudgetTransactionType.getByAmount(it.amount) }
    }
    private val _name = MediatorLiveData<String>().apply {
        addSource(_editedBudgetTransaction) { value = it.name }
    }
    private val _amountInput = MediatorLiveData<Double>().apply {
        addSource(_editedBudgetTransaction) { value = getDisplayedAmount(it.amount) }
    }
    private val _periodicCategory = MediatorLiveData<PeriodicCategorySimple>().apply {
        addSource(_editedBudgetTransaction) { value = PeriodicCategorySimple(it.periodicCategoryId, it.currencyCode, it.categoryName) }
    }
    private val _isCurrencyAvailable = MediatorLiveData(false).apply {
        addSource(_periodicCategory) { value = true }
    }
    private val _currencyCode = MediatorLiveData(getMainCurrency()).apply {
        addSource(_periodicCategory) { value = it.currencyCode }
    }
    private val _locationOptional = MediatorLiveData<Optional<LocationWithAddress>>(Optional.empty()).apply {
        addSource(_editedBudgetTransaction) { setLocationOptionalValue(it.latLng) }
    }
    private val _performUiEvent = EventMutableLiveData<UiEvent>()

    val mode = _mode.asLiveData()
    val type = _type.distinctUntilChanged()
    val name = _name.distinctUntilChanged()
    val amountInput = _amountInput.distinctUntilChanged()
    val isCurrencyAvailable = _isCurrencyAvailable.distinctUntilChanged()
    val currencyCode = _currencyCode.distinctUntilChanged()
    val periodicCategory = _periodicCategory.distinctUntilChanged()
    val locationOptional = _locationOptional.distinctUntilChanged()
    val performUiEvent = _performUiEvent.asEventLiveData()

    fun setType(@IntBudgetTransactionType type: Int) {
        _type.value = type
    }

    fun setName(name: String) {
        _name.value = name
    }

    fun setAmount(amount: String) {
        amount.toDoubleOrNull()?.let { _amountInput.value = it }
    }

    fun setPeriodicCategory(periodicCategory: PeriodicCategorySimple) {
        _periodicCategory.value = periodicCategory
    }

    fun setLocation(location: LocationWithAddress?) {
        _locationOptional.value = location.asOptional()
    }

    fun onChoosePeriodicCategory() {
        _performUiEvent.call(UiEvent.ChoosePeriodicCategory(periodId))
    }

    fun onChooseLocation() {
        _performUiEvent.call(UiEvent.ChooseLocation(_locationOptional.optionalValue))
    }

    fun onApply(budgetTransactionInputBundle: BudgetTransactionInputBundle) {
        val validator = createValidator(budgetTransactionInputBundle)
        _performUiEvent.call(UiEvent.DisplayValidationResults(validator.getValidationErrors(allBlank = true)))

        validator.getValidationResult()
            .onSuccess { insertOrUpdateBudgetTransaction(it) }
            .onError { displayValidationResults(it) }
    }

    fun onCalculateByAnotherCurrency() {
        _currencyCode.value?.let { toCurrency ->
            _performUiEvent.call(UiEvent.NavigateToCurrencyConversion(toCurrency))
        }
    }

    fun onAmountCalculated(amount: Double) {
        _amountInput.value = amount
    }

    fun onBack() {
        _performUiEvent.call(UiEvent.Exit)
    }

    private fun getMainCurrency(): String {
        return currencyRepository.getMainCurrencyCode()
    }

    private fun setLocationOptionalValue(latLng: LatLng?) {
        if (latLng != null) {
            viewModelScope.launch {
                _locationOptional.value = LocationWithAddress(
                    latLng,
                    locationRepository.getAddressByCoordinates(latLng.latitude, latLng.longitude)
                ).asOptional()
            }
        } else {
            _locationOptional.value = Optional.empty()
        }
    }

    private fun getDisplayedAmount(actualAmount: Double): Double {
        return actualAmount.absoluteValue
    }

    private fun createValidator(budgetTransactionInputBundle: BudgetTransactionInputBundle): BudgetTransactionCreateEditValidator {
        val validationBundle = BudgetTransactionValidationBundle(
            type = _type.value ?: IntBudgetTransactionType.EXPENSE,
            name = budgetTransactionInputBundle.name,
            amount = budgetTransactionInputBundle.amount,
            periodicCategoryId = _periodicCategory.value?.id ?: Int.INVALID,
            latLng = _locationOptional.optionalValue?.latLng
        )

        return BudgetTransactionCreateEditValidator(_editedBudgetTransaction.value, validationBundle)
    }

    private fun displayValidationResults(validationErrors: ValidationErrors) {
        _performUiEvent.call(UiEvent.DisplayValidationResults(validationErrors))
    }

    private fun loadEditedBudgetTransaction() {
        viewModelScope.launch {
            budgetTransactionRepository.getBudgetTransactionFlow(editedBudgetTransactionId)
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
            CreateEditMode.CREATE -> budgetTransactionRepository.insertBudgetTransactionFlow(budgetTransaction)
            CreateEditMode.EDIT -> budgetTransactionRepository.updateBudgetTransactionFlow(budgetTransaction)
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