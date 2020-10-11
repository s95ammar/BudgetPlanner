package com.s95ammar.budgetplanner.ui.appscreens.savingjars

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.s95ammar.budgetplanner.models.repository.LocalRepository

class SavingJarsViewModel @ViewModelInject constructor(
    private val localRepository: LocalRepository
) : ViewModel()