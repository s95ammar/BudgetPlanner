package com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.periods.data

import com.s95ammar.budgetplanner.ui.appscreens.dashboard.common.data.PeriodSimple
import com.s95ammar.budgetplanner.ui.common.LoadingState

sealed class PeriodsUiEvent {
    class DisplayLoadingState(val loadingState: LoadingState): PeriodsUiEvent()
    class OnNavigateToEditPeriod(val periodId: Int): PeriodsUiEvent()
    class ShowBottomSheet(val period: PeriodSimple): PeriodsUiEvent()
    object OnPeriodDeleted : PeriodsUiEvent()
    object Exit: PeriodsUiEvent()
}
