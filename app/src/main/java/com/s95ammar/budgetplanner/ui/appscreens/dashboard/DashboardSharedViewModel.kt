package com.s95ammar.budgetplanner.ui.appscreens.dashboard

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.s95ammar.budgetplanner.models.repository.PeriodRepository
import com.s95ammar.budgetplanner.util.INVALID
import com.s95ammar.budgetplanner.util.lifecycleutil.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DashboardSharedViewModel @Inject constructor(
    private val repository: PeriodRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _selectedPeriodId = MutableLiveData(Int.INVALID)

    val selectedPeriodId = _selectedPeriodId.asLiveData()

    fun refresh() {
        _selectedPeriodId.value?.let { id ->
            // TODO
        }
    }

    fun onPeriodChanged(periodId: Int?) {
        _selectedPeriodId.value = periodId ?: Int.INVALID
    }

}
