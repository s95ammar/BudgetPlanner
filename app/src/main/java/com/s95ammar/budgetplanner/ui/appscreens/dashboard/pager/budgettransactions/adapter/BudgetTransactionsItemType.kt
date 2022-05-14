package com.s95ammar.budgetplanner.ui.appscreens.dashboard.pager.budgettransactions.adapter

import com.s95ammar.budgetplanner.ui.appscreens.dashboard.common.data.BudgetTransaction

sealed class BudgetTransactionsItemType {
    data class ViewOnMap(val periodId: Int) : BudgetTransactionsItemType()
    data class ListItem(val budgetTransaction: BudgetTransaction) : BudgetTransactionsItemType()
}
