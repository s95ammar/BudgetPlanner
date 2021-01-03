package com.s95ammar.budgetplanner.ui.appscreens.dashboard.dashboard

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.s95ammar.budgetplanner.models.repository.LocalRepository
import com.s95ammar.budgetplanner.models.repository.RemoteRepository
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.budget.data.PeriodRecordsNavigationBundle
import com.s95ammar.budgetplanner.util.lifecycleutil.EventMutableLiveData
import com.s95ammar.budgetplanner.util.lifecycleutil.EventMutableLiveDataVoid
import com.s95ammar.budgetplanner.util.lifecycleutil.asLiveData

class DashboardSharedViewModel @ViewModelInject constructor(
    private val localRepository: LocalRepository,
    private val remoteRepository: RemoteRepository,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _selectedPeriodId = MutableLiveData<Int>()
    private val _onNavigateToPeriodRecords = EventMutableLiveData<PeriodRecordsNavigationBundle>()
    private val _onPeriodRecordAdded = EventMutableLiveDataVoid()

    val selectedPeriodId = _selectedPeriodId.asLiveData()
    val onNavigateToPeriodRecords = _onNavigateToPeriodRecords.asEventLiveData()
    val onPeriodRecordAdded = _onPeriodRecordAdded.asEventLiveData()

    fun onPeriodChanged(periodId: Int) {
        _selectedPeriodId.value = periodId
    }

    fun onNavigateToPeriodRecords(navigationBundle: PeriodRecordsNavigationBundle) {
        _onNavigateToPeriodRecords.call(navigationBundle)
    }

    fun onPeriodRecordAdded() {
        _onPeriodRecordAdded.call()
    }
}