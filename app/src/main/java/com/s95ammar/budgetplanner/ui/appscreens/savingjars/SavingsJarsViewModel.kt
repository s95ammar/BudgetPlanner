package com.s95ammar.budgetplanner.ui.appscreens.savingjars

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.s95ammar.budgetplanner.models.datasource.LocalDataSource

class SavingsJarsViewModel @ViewModelInject constructor(
    private val localDataSource: LocalDataSource
) : ViewModel()