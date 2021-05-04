package com.s95ammar.budgetplanner.ui.appscreens.dashboard.pager.budget.data

sealed class BudgetUiEvent {
    class OnEditPeriod(val periodId: Int): BudgetUiEvent()
}
