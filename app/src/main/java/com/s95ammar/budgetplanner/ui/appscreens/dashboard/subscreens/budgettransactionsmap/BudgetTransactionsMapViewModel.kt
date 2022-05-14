package com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.budgettransactionsmap

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.s95ammar.budgetplanner.models.IntBudgetTransactionType
import com.s95ammar.budgetplanner.models.repository.BudgetTransactionRepository
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.budgettransactionsmap.data.BudgetTransactionClusterItem
import com.s95ammar.budgetplanner.ui.common.LoadingState
import com.s95ammar.budgetplanner.util.INVALID
import com.s95ammar.budgetplanner.util.lifecycleutil.EventMutableLiveData
import com.s95ammar.budgetplanner.util.lifecycleutil.LoaderMutableLiveData
import com.s95ammar.budgetplanner.util.lifecycleutil.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.budgettransactionsmap.BudgetTransactionsMapFragmentArgs as FragmentArgs
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.budgettransactionsmap.data.BudgetTransactionsMapUiEvent as UiEvent

@HiltViewModel
class BudgetTransactionsMapViewModel @Inject constructor(
    val savedStateHandle: SavedStateHandle,
    val repository: BudgetTransactionRepository
) : ViewModel() {

    private val periodId = savedStateHandle.get<Int>(FragmentArgs::periodId.name) ?: Int.INVALID
    private val _budgetTransactionClusterItems = LoaderMutableLiveData<List<BudgetTransactionClusterItem>> {
        loadBudgetTransactions()
    }
    private val _performUiEvent = EventMutableLiveData<UiEvent>()

    val budgetTransactionClusterItems = _budgetTransactionClusterItems.asLiveData()
    val performUiEvent = _performUiEvent.asEventLiveData()

    private fun loadBudgetTransactions() {
        viewModelScope.launch {
            repository.getPeriodBudgetTransactionsFlow(periodId)
                .onStart {
                    _performUiEvent.call(UiEvent.DisplayLoadingState(LoadingState.Loading))
                }
                .catch { throwable ->
                    _performUiEvent.call(UiEvent.DisplayLoadingState(LoadingState.Error(throwable)))
                }
                .collect { budgetTransactionJoinEntityList ->
                    _performUiEvent.call(UiEvent.DisplayLoadingState(LoadingState.Success))
                    _budgetTransactionClusterItems.value = budgetTransactionJoinEntityList
                        .filter { it.type == IntBudgetTransactionType.EXPENSE }
                        .mapNotNull {
                            BudgetTransactionClusterItem(
                                it.name,
                                it.type,
                                it.latLng?.let { (lat, lng) -> LatLng(lat, lng) } ?: return@mapNotNull null,
                                it.amount
                            )
                        }
                }
        }
    }
}