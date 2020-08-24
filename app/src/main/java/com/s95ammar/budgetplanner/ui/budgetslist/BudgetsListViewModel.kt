package com.s95ammar.budgetplanner.ui.budgetslist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class BudgetsListViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is budgets list Fragment"
    }
    val text: LiveData<String> = _text
}