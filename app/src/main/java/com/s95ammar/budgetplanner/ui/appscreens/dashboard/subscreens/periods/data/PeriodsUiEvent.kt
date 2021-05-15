package com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.periods.data

import com.s95ammar.budgetplanner.ui.appscreens.dashboard.common.data.PeriodSimple
import com.s95ammar.budgetplanner.ui.common.LoadingState

sealed class PeriodsUiEvent {
    class DisplayLoadingState(val loadingState: LoadingState): PeriodsUiEvent()
    class ListenAndNavigateToEditPeriod(val period: PeriodSimple): PeriodsUiEvent()
    class ShowBottomSheet(val period: PeriodSimple): PeriodsUiEvent()
    object Exit: PeriodsUiEvent()
}
