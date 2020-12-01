package com.s95ammar.budgetplanner.ui.appscreens.dashboard

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.s95ammar.budgetplanner.models.repository.LocalRepository
import com.s95ammar.budgetplanner.models.repository.RemoteRepository
import com.s95ammar.budgetplanner.util.lifecycleutil.asLiveData

class DashboardSharedViewModel @ViewModelInject constructor(
    private val localRepository: LocalRepository,
    private val remoteRepository: RemoteRepository,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _selectedPeriodId = MutableLiveData<Int>()

    val selectedPeriodId = _selectedPeriodId.asLiveData()

    fun onPeriodChanged(periodId: Int) {
        _selectedPeriodId.value = periodId
    }


}