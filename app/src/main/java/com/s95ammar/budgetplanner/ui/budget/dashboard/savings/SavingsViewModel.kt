package com.s95ammar.budgetplanner.ui.budget.dashboard.savings

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.s95ammar.budgetplanner.models.repository.Repository

class SavingsViewModel @ViewModelInject constructor(
    private val repository: Repository
) : ViewModel()