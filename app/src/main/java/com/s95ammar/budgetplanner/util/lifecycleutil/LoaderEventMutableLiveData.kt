package com.s95ammar.budgetplanner.util.lifecycleutil

class LoaderEventMutableLiveData<T>(val load: () -> Unit) : EventMutableLiveData<T>() {

    private var isFired = false

    override fun onActive() {
        super.onActive()

        if (!isFired) {
            load()
            isFired = true
        }
    }
}