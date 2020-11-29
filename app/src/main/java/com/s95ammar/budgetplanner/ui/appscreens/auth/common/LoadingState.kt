package com.s95ammar.budgetplanner.ui.appscreens.auth.common

sealed class LoadingState {
    object Cold: LoadingState()
    object Loading: LoadingState()
    object Success: LoadingState()
    class Error(val throwable: Throwable): LoadingState()
}