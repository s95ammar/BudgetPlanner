package com.s95ammar.budgetplanner.ui.budget.expenses

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.s95ammar.budgetplanner.models.repository.LocalRepository

class ExpensesViewModel @ViewModelInject constructor(
    private val localRepository: LocalRepository
) : ViewModel()