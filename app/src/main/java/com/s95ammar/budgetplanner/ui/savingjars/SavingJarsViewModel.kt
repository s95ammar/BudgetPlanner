package com.s95ammar.budgetplanner.ui.savingjars

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.s95ammar.budgetplanner.models.repository.Repository

class SavingJarsViewModel @ViewModelInject constructor(
    private val repository: Repository
) : ViewModel()