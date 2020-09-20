package com.s95ammar.budgetplanner.ui.budgetslist.createedit

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.s95ammar.budgetplanner.models.repository.Repository
import com.s95ammar.budgetplanner.ui.common.BundleKey
import com.s95ammar.budgetplanner.ui.common.Constants
import com.s95ammar.budgetplanner.ui.common.CreateEditMode
import com.s95ammar.budgetplanner.util.asLiveData

class BudgetCreateEditViewModel @ViewModelInject constructor(
    private val repository: Repository,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val budgetId = savedStateHandle.get<Int>(BundleKey.KEY_BUDGET_ID) ?: Constants.NO_ITEM


    private val _mode = MutableLiveData(CreateEditMode.getById(budgetId))

    val mode = _mode.asLiveData()

}