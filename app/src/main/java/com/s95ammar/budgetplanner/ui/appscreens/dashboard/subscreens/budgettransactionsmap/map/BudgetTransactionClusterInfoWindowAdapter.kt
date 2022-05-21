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
import com.s95ammar.budgetplanner.models.isExpense
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.budgettransactionsmap.data.BudgetTransactionClusterItem
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.budgettransactionsmap.data.BudgetTransactionsSumDetails
import com.s95ammar.budgetplanner.ui.main.data.CurrencyDetails
import com.s95ammar.budgetplanner.util.getAmountStringFormatted
import com.s95ammar.budgetplanner.util.orFalse

class BudgetTransactionClusterInfoWindowAdapter(
    private val context: Context,
    private val mainCurrencyDetails: CurrencyDetails
) : GoogleMap.InfoWindowAdapter {

    private val binding = LayoutBudgetTransactionsClusterInfoWindowBinding.inflate(LayoutInflater.from(context))
    private val areRatesAvailable
        get() = mainCurrencyDetails.ratesOptional.isNotEmpty

    override fun getInfoWindow(marker: Marker?): View {
        return binding.root
    }

    override fun getInfoContents(marker: Marker?): View? {
        return null
    }

    fun updateWindowForItem(cluster: Cluster<BudgetTransactionClusterItem>) {
        val (expenses, income) = cluster.items.partition { it.amount.isExpense() }

        val areExpenseViewsVisible = expenses.isNotEmpty()
        val expensesSumDetails = getBudgetTransactionsSumFormatted(expenses)
        binding.totalExpensesTextView.text = expensesSumDetails?.sumFormatted ?: context.getString(R.string.unknown_total)
        binding.totalExpensesTextView.isVisible = areExpenseViewsVisible
        binding.totalExpensesKeyTextView.isVisible = areExpenseViewsVisible

        val areIncomeViewsVisible = income.isNotEmpty()
        val incomeSumDetails = getBudgetTransactionsSumFormatted(income)
        binding.totalIncomeTextView.text = incomeSumDetails?.sumFormatted
        binding.totalIncomeTextView.isVisible = areIncomeViewsVisible
        binding.totalIncomeKeyTextView.isVisible = areIncomeViewsVisible

        binding.multipleCurrenciesTipTextView.isVisible =
            expensesSumDetails?.isMultipleCurrencies.orFalse() || incomeSumDetails?.isMultipleCurrencies.orFalse()
    }

    private fun getBudgetTransactionsSumFormatted(
        list: List<BudgetTransactionClusterItem>
    ) : BudgetTransactionsSumDetails? {
        if (list.isEmpty()) return null

        val groupedByCurrencyCode = list.groupBy { it.currencyCode }
        val isMultipleCurrencies = groupedByCurrencyCode.size > 1
        val sum: Double
        val currencyCode: String
        if (isMultipleCurrencies) {
            sum = getBudgetTransactionsSumInMainCurrency(groupedByCurrencyCode) ?: return null
            currencyCode = mainCurrencyDetails.currencyCode
        } else {
            sum = list.sumOf { it.amount }
            currencyCode = list.first().currencyCode
        }

        val sumFormatted = context.getAmountStringFormatted(sum, currencyCode = currencyCode)
        val resultString = if (isMultipleCurrencies) {
            context.getString(R.string.format_approximate_sum, sumFormatted)
        } else {
            sumFormatted
        }

        return BudgetTransactionsSumDetails(resultString, isMultipleCurrencies)
    }

    private fun getBudgetTransactionsSumInMainCurrency(
        groupedByCurrencyCode: Map<String, List<BudgetTransactionClusterItem>>
    ) : Double? {
        val mainCurrencyRates = mainCurrencyDetails.ratesOptional.value ?: return null
        var sumInMainCurrency = 0.0
        for ((currencyCode, budgetTransactionsSameCurrency) in groupedByCurrencyCode) {
            val sumInCurrentCurrency = budgetTransactionsSameCurrency.sumOf { it.amount }
            sumInMainCurrency += sumInCurrentCurrency * mainCurrencyRates.getOrElse(currencyCode) { 1.0 }
        }
        return sumInMainCurrency
    }
}
