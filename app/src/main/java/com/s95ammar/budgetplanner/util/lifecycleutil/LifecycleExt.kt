package com.s95ammar.budgetplanner.util.lifecycleutil

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData

fun <T> MutableLiveData<T>.asLiveData(): LiveData<T> = this

fun <T> MediatorLiveData<T>.asLiveData(): LiveData<T> = this

fun <T> MediatorLiveData(initialValue: T) = MediatorLiveData<T>().apply { value = initialValue }