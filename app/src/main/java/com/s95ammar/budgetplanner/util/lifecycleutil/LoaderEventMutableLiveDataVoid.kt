package com.s95ammar.budgetplanner.util.lifecycleutil

class LoaderEventMutableLiveDataVoid(val load: () -> Unit) : EventMutableLiveDataVoid() {

    private var isFired = false

    override fun onActive() {
        super.onActive()

        if (!isFired) {
            load()
            isFired = true
        }
    }
}