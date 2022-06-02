package com.s95ammar.budgetplanner.ui.main.data

import androidx.annotation.Nullable
import com.s95ammar.budgetplanner.ui.common.LoadingState

sealed class MainUiEvent {
    class DisplayLoadingState(val loadingState: LoadingState) : MainUiEvent()
    class NavigateToMainCurrencySelection(
        @Nullable val currentCurrencyCode: String?,
        val isBackAllowed: Boolean
    ) : MainUiEvent()
    object FinishActivity : MainUiEvent()
}
