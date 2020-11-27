package com.s95ammar.budgetplanner.util.lifecycleutil

import androidx.lifecycle.MutableLiveData

class LoaderMutableLiveData<T>(val load: () -> Unit) : MutableLiveData<T>() {

    private var isFired = false

    override fun onActive() {
        super.onActive()

        if (!isFired) {
            load()
            isFired = true
        }
    }
}