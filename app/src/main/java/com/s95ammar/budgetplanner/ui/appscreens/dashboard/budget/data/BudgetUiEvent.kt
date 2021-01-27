package com.s95ammar.budgetplanner.ui.appscreens.dashboard.budget.data

sealed class BudgetUiEvent {
    class OnEditPeriod(val periodId: Int): BudgetUiEvent()
}
