package com.s95ammar.budgetplanner.ui.appscreens.dashboard.budget.periods

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.s95ammar.budgetplanner.models.mappers.PeriodApiViewMapper
import com.s95ammar.budgetplanner.models.repository.LocalRepository
import com.s95ammar.budgetplanner.models.repository.RemoteRepository
import com.s95ammar.budgetplanner.models.view.PeriodViewEntity
import com.s95ammar.budgetplanner.ui.appscreens.auth.common.LoadingState
import com.s95ammar.budgetplanner.util.lifecycleutil.*
import kotlinx.coroutines.launch

class PeriodsViewModel @ViewModelInject constructor(
    private val localRepository: LocalRepository,
    private val remoteRepository: RemoteRepository
) : ViewModel() {

    private val _allPeriods = LoaderMutableLiveData<List<PeriodViewEntity>> { loadAllPeriods() }
    private val _displayLoadingState = EventMutableLiveData<LoadingState>(LoadingState.Cold)
    private val _navigateToEditPeriod = EventMutableLiveData<Int>()
    private val _showBottomSheet = EventMutableLiveData<PeriodViewEntity>()

    val allPeriods = _allPeriods.asLiveData()
    val displayLoadingState = _displayLoadingState.asEventLiveData()
    val navigateToEditPeriod = _navigateToEditPeriod.asEventLiveData()
    val showBottomSheet = _showBottomSheet.asEventLiveData()

    fun refresh() {
        loadAllPeriods()
    }

    fun onPeriodItemClick(position: Int) {
        _allPeriods.value?.getOrNull(position)?.let { period ->
            _navigateToEditPeriod.call(period.id)
        }
    }

    fun onPeriodItemLongClick(position: Int) {
        _allPeriods.value?.getOrNull(position)?.let { period ->
            _showBottomSheet.call(period)
        }
    }

    fun onDeletePeriod(id: Int) = viewModelScope.launch {
        _displayLoadingState.call(LoadingState.Loading)
        remoteRepository.deletePeriod(id)
            .onSuccess { refresh() }
            .onError { _displayLoadingState.call(LoadingState.Error(it)) }
    }

    private fun loadAllPeriods() {
        viewModelScope.launch {
            _displayLoadingState.call(LoadingState.Loading)
            remoteRepository.getAllUserPeriods()
                .onSuccess { categoriesApiEntities ->
                    val categories = categoriesApiEntities.orEmpty().map { apiEntity -> PeriodApiViewMapper.toViewEntity(apiEntity) }
                    _allPeriods.value = categories
                    _displayLoadingState.call(LoadingState.Success)
                }
                .onError { _displayLoadingState.call(LoadingState.Error(it)) }
        }
    }

}