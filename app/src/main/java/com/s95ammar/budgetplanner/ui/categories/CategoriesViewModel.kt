package com.s95ammar.budgetplanner.ui.categories

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.s95ammar.budgetplanner.models.repository.Repository

class CategoriesViewModel @ViewModelInject constructor(
    private val repository: Repository
) : ViewModel()