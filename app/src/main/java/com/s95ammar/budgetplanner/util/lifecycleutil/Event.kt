package com.s95ammar.budgetplanner.util.lifecycleutil

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

open class Event<out T>(private val content: T) {
    var hasBeenHandled = false
        private set

    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }

    fun peekContent(): T? = content
}

open class EventLiveData<T> : LiveData<Event<T>> {
    constructor(initialValue: T): super(Event(initialValue))
    constructor(): super()

    protected open fun call(arg: T) {
        value = Event(arg)
    }
}

open class EventLiveDataVoid : LiveData<Event<Unit>>() {
    protected open fun call() {
        value = Event(Unit)
    }
}

open class EventMutableLiveData<T> : EventLiveData<T> {
    constructor(initialValue: T): super(initialValue)
    constructor(): super()

    public override fun call(arg: T) {
        super.call(arg)
    }

    fun asEventLiveData() = this as EventLiveData<T>
}

open class EventMutableLiveDataVoid : EventLiveDataVoid() {
    public override fun call() {
        super.call()
    }

    fun asEventLiveData() = this as EventLiveDataVoid
}

class EventObserver<T>(val action: (T) -> Unit) : Observer<Event<T>> {
    override fun onChanged(event: Event<T>) {
        event.getContentIfNotHandled()?.let { action(it) }
    }
}

fun <T> LiveData<Event<T>>.observeEvent(lifecycleOwner: LifecycleOwner, action: (T) -> Unit) {
    observe(lifecycleOwner, EventObserver(action))
}