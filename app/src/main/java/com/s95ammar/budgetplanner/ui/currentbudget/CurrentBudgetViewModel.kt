package com.s95ammar.budgetplanner.ui.currentbudget

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CurrentBudgetViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is current budget fragment"
    }
    val text: LiveData<String> = _text
}