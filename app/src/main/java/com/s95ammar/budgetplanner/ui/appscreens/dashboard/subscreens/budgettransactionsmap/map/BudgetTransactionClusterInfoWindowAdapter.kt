package com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.budgettransactionsmap.map

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.core.view.isVisible
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import com.google.maps.android.clustering.Cluster
import com.s95ammar.budgetplanner.R
import com.s95ammar.budgetplanner.databinding.LayoutBudgetTransactionsClusterInfoWindowBinding
import com.s95ammar.budgetplanner.models.IntBudgetTransactionType
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.budgettransactionsmap.data.BudgetTransactionClusterItem


class BudgetTransactionClusterInfoWindowAdapter(
    val context: Context
) : GoogleMap.InfoWindowAdapter {

    private val binding = LayoutBudgetTransactionsClusterInfoWindowBinding.inflate(LayoutInflater.from(context))

    override fun getInfoWindow(marker: Marker?): View {
        return binding.root
    }

    override fun getInfoContents(marker: Marker?): View? {
        return null
    }

    fun updateWindowForItem(cluster: Cluster<BudgetTransactionClusterItem>) {
        val (expenses, income) = cluster.items.partition { it.type == IntBudgetTransactionType.EXPENSE }

        val expensesSum = expenses.sumOf { it.amount }
        binding.totalExpensesTextView.text = context.getString(R.string.format_budget_transaction_amount, -expensesSum)

        val incomeSum = income.sumOf { it.amount }
        binding.totalIncomeTextView.text = context.getString(R.string.format_budget_transaction_amount, incomeSum)

        val areExpenseViewsVisible = expensesSum > 0
        binding.totalExpensesKeyTextView.isVisible = areExpenseViewsVisible
        binding.totalExpensesTextView.isVisible = areExpenseViewsVisible

        val areIncomeViewsVisible = incomeSum > 0
        binding.totalIncomeKeyTextView.isVisible = areIncomeViewsVisible
        binding.totalIncomeTextView.isVisible = areIncomeViewsVisible
    }

}