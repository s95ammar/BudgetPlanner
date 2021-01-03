package com.s95ammar.budgetplanner.ui.appscreens.dashboard.periodrecords

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.s95ammar.budgetplanner.models.api.requests.PeriodRecordUpsertApiRequest
import com.s95ammar.budgetplanner.models.mappers.CategoryApiViewMapper
import com.s95ammar.budgetplanner.models.repository.LocalRepository
import com.s95ammar.budgetplanner.models.repository.RemoteRepository
import com.s95ammar.budgetplanner.models.view.CategoryViewEntity
import com.s95ammar.budgetplanner.ui.appscreens.auth.common.LoadingState
import com.s95ammar.budgetplanner.util.lifecycleutil.EventMutableLiveData
import com.s95ammar.budgetplanner.util.lifecycleutil.EventMutableLiveDataVoid
import com.s95ammar.budgetplanner.util.lifecycleutil.LoaderMutableLiveData
import kotlinx.coroutines.launch

class PeriodRecordsViewModel @ViewModelInject constructor(
    private val localRepository: LocalRepository,
    private val remoteRepository: RemoteRepository,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val excludedIds = savedStateHandle.get<IntArray>(PeriodRecordsFragmentArgs::excludedCategoryIds.name)?.toList().orEmpty()

    private val _allCategories = LoaderMutableLiveData<List<CategoryViewEntity>> { loadAllCategories() }
    private val _displayLoadingState = EventMutableLiveData<LoadingState>(LoadingState.Cold)
    private val _onPeriodRecordAdded = EventMutableLiveDataVoid()

    val resultCategories = _allCategories.map { list -> list.filterNot { category -> category.id in excludedIds } }
    val displayLoadingState = _displayLoadingState.asEventLiveData()
    val onPeriodRecordAdded = _onPeriodRecordAdded.asEventLiveData()

    fun onCategoryItemClick(position: Int) {
        resultCategories.value?.getOrNull(position)?.let { category ->
            insertPeriodRecord(category.id)
        }
    }

    private fun insertPeriodRecord(categoryId: Int) {
        viewModelScope.launch {
            _displayLoadingState.call(LoadingState.Loading)
            savedStateHandle.get<Int>(PeriodRecordsFragmentArgs::periodId.name)?.let { periodId ->
                remoteRepository.insertPeriodRecord(PeriodRecordUpsertApiRequest.Insertion(categoryId, periodId, max = null /*TODO*/))
                    .onSuccess {
                        _onPeriodRecordAdded.call()
                        _displayLoadingState.call(LoadingState.Success)
                    }
                    .onError { _displayLoadingState.call(LoadingState.Error(it)) }
            }
        }
    }

    private fun loadAllCategories() {
        viewModelScope.launch {
            _displayLoadingState.call(LoadingState.Loading)
            remoteRepository.getAllUserCategories()
                .onSuccess { categoryApiEntities ->
                    val categories = categoryApiEntities.orEmpty().mapNotNull { apiEntity -> CategoryApiViewMapper.toViewEntity(apiEntity) }
                    _allCategories.value = categories
                    _displayLoadingState.call(LoadingState.Success)
                }
                .onError { _displayLoadingState.call(LoadingState.Error(it)) }
        }
    }

}