package com.s95ammar.budgetplanner.ui.budget.expenses

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.s95ammar.budgetplanner.models.repository.Repository

class ExpensesViewModel @ViewModelInject constructor(
    private val repository: Repository
) : ViewModel()