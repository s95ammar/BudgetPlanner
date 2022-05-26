package com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.periodcreateedit.subscreens.categoriesselection.subscreens

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.s95ammar.budgetplanner.models.IntBudgetTransactionType
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.common.data.CategoryOfPeriod
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.periodcreateedit.subscreens.categoriesselection.subscreens.data.PeriodCategoryEstimateCreateEditUiEvent
import com.s95ammar.budgetplanner.util.Optional
import com.s95ammar.budgetplanner.util.asOptional
import com.s95ammar.budgetplanner.util.lifecycleutil.EventMutableLiveData
import com.s95ammar.budgetplanner.util.lifecycleutil.MediatorLiveData
import com.s95ammar.budgetplanner.util.lifecycleutil.asLiveData
import com.s95ammar.budgetplanner.util.optionalValue
import com.s95ammar.budgetplanner.util.orZero
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.math.absoluteValue
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.periodcreateedit.subscreens.categoriesselection.subscreens.PeriodCategoryEstimateCreateEditFragmentArgs as FragmentArgs

@HiltViewModel
class PeriodCategoryEstimateCreateEditViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _categoryOfPeriod = savedStateHandle.getLiveData<CategoryOfPeriod>(FragmentArgs::categoryOfPeriod.name)

    private val _estimateInput = MediatorLiveData<Optional<Double>>().apply {
        addSource(_categoryOfPeriod) { categoryOfPeriod ->
            categoryOfPeriod.estimate?.let { value = getDisplayedEstimate(it).asOptional() }
        }
    }
    private val _type = MediatorLiveData(IntBudgetTransactionType.EXPENSE).apply {
        addSource(_categoryOfPeriod) { categoryOfPeriod ->
            categoryOfPeriod.estimate?.let { value = IntBudgetTransactionType.getByAmount(it) }
        }
    }
    private val _performUiEvent = EventMutableLiveData<PeriodCategoryEstimateCreateEditUiEvent>()

    val categoryName = _categoryOfPeriod.map { it.categoryName }
    val estimateInput = _estimateInput.asLiveData()
    val currencyCode = _categoryOfPeriod.map { it.currencyCode }
    val type = _type.asLiveData()
    val performUiEvent = _performUiEvent.asEventLiveData()

    fun setType(@IntBudgetTransactionType type: Int) {
        _type.value = type
    }

    fun setEstimate(estimate: String) {
        _estimateInput.value = estimate.toDoubleOrNull().asOptional()
    }

    fun onApply() {
        val estimate = _estimateInput.optionalValue.orZero()
        val actualEstimate = if (type.value == IntBudgetTransactionType.EXPENSE) -estimate else estimate
        _performUiEvent.call(PeriodCategoryEstimateCreateEditUiEvent.SetResult(actualEstimate.takeIf { it != 0.0 }))
        _performUiEvent.call(PeriodCategoryEstimateCreateEditUiEvent.Exit)
    }

    fun onBack() {
        _performUiEvent.call(PeriodCategoryEstimateCreateEditUiEvent.Exit)
    }

    private fun getDisplayedEstimate(actualEstimate: Double): Double {
        return actualEstimate.absoluteValue
    }

}