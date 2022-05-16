package com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.budgettransactionsmap.map

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.core.view.isVisible
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import com.google.maps.android.clustering.Cluster
import com.s95ammar.budgetplanner.databinding.LayoutBudgetTransactionsClusterInfoWindowBinding
import com.s95ammar.budgetplanner.models.isExpense
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.budgettransactionsmap.data.BudgetTransactionClusterItem
import com.s95ammar.budgetplanner.util.getAmountFormatResId


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
        val (expenses, income) = cluster.items.partition { it.amount.isExpense() }

        val expensesSum = expenses.sumOf { it.amount }
        binding.totalExpensesTextView.text = context.getString(getAmountFormatResId(expensesSum), expensesSum)

        val incomeSum = income.sumOf { it.amount }
        binding.totalIncomeTextView.text = context.getString(getAmountFormatResId(incomeSum), incomeSum)

        val areExpenseViewsVisible = expenses.isNotEmpty()
        binding.totalExpensesKeyTextView.isVisible = areExpenseViewsVisible
        binding.totalExpensesTextView.isVisible = areExpenseViewsVisible

        val areIncomeViewsVisible = income.isNotEmpty()
        binding.totalIncomeKeyTextView.isVisible = areIncomeViewsVisible
        binding.totalIncomeTextView.isVisible = areIncomeViewsVisible
    }

}