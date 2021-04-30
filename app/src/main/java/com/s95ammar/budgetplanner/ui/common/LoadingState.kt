package com.s95ammar.budgetplanner.ui.common

sealed class LoadingState {
    object Cold: LoadingState()
    object Loading: LoadingState()
    object Success: LoadingState()
    class Error(val throwable: Throwable): LoadingState()
}