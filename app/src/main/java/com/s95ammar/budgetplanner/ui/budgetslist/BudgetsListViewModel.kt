package com.s95ammar.budgetplanner.ui.budgetslist

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.s95ammar.budgetplanner.models.repository.Repository

class BudgetsListViewModel @ViewModelInject constructor(
    private val repository: Repository
) : ViewModel()