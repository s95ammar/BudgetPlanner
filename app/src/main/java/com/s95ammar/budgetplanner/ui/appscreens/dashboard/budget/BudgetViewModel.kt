package com.s95ammar.budgetplanner.ui.appscreens.dashboard.budget

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.s95ammar.budgetplanner.models.mappers.PeriodRecordApiViewMapper
import com.s95ammar.budgetplanner.models.repository.LocalRepository
import com.s95ammar.budgetplanner.models.repository.RemoteRepository
import com.s95ammar.budgetplanner.models.view.PeriodRecordViewEntity
import com.s95ammar.budgetplanner.ui.appscreens.auth.common.LoadingState
import com.s95ammar.budgetplanner.util.lifecycleutil.EventMutableLiveData
import com.s95ammar.budgetplanner.util.lifecycleutil.asLiveData
import kotlinx.coroutines.launch

class BudgetViewModel @ViewModelInject constructor(
    private val localRepository: LocalRepository,
    private val remoteRepository: RemoteRepository,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _periodRecords = MutableLiveData<List<PeriodRecordViewEntity>>()
    private val _displayLoadingState = EventMutableLiveData<LoadingState>(LoadingState.Cold)

    val periodRecords = _periodRecords.asLiveData()
    val displayLoadingState = _displayLoadingState.asEventLiveData()

    fun onPeriodChanged(periodId: Int) {
        loadPeriodRecords(periodId)
    }

    private fun loadPeriodRecords(periodId: Int) {
        _displayLoadingState.call(LoadingState.Loading)
        viewModelScope.launch {
            remoteRepository.getPeriodRecordsForPeriod(periodId)
                .onSuccess { periodRecordApiEntities ->
                    _periodRecords.value = periodRecordApiEntities.orEmpty().mapNotNull { PeriodRecordApiViewMapper.toViewEntity(it) }
                    _displayLoadingState.call(LoadingState.Success)
                }
                .onError { _displayLoadingState.call(LoadingState.Error(it)) }
        }
    }
}