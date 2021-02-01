package com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.periods.data

import com.s95ammar.budgetplanner.models.view.PeriodSimpleViewEntity
import com.s95ammar.budgetplanner.ui.appscreens.auth.common.LoadingState

sealed class PeriodsUiEvent {
    class DisplayLoadingState(val loadingState: LoadingState): PeriodsUiEvent()
    class OnNavigateToEditPeriod(val periodId: Int): PeriodsUiEvent()
    class ShowBottomSheet(val period: PeriodSimpleViewEntity): PeriodsUiEvent()
}
