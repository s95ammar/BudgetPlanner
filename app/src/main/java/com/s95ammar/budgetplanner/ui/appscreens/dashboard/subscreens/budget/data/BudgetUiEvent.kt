package com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.budget.data

sealed class BudgetUiEvent {
    class OnEditPeriod(val periodId: Int): BudgetUiEvent()
}
