package com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.budgettransactionsmap.map

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import com.s95ammar.budgetplanner.databinding.LayoutBudgetTransactionInfoWindowBinding
import com.s95ammar.budgetplanner.models.IntBudgetTransactionType
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.budgettransactionsmap.data.BudgetTransactionClusterItem
import com.s95ammar.budgetplanner.util.currentLocale
import com.s95ammar.budgetplanner.util.getAmountFormat
import java.text.SimpleDateFormat
import java.util.Date

class BudgetTransactionInfoWindowAdapter(
    val context: Context
) : GoogleMap.InfoWindowAdapter {

    private val binding = LayoutBudgetTransactionInfoWindowBinding.inflate(LayoutInflater.from(context))

    override fun getInfoWindow(marker: Marker?): View {
        return binding.root
    }

    override fun getInfoContents(marker: Marker?): View? {
        return null
    }

    fun updateWindowForItem(item: BudgetTransactionClusterItem) {
        setName(item.name)
        setTypeAndAmount(item.amount)
        setCreationUnixMs(item.creationUnixMs)
        setCategoryName(item.categoryName)
    }

    private fun setName(name: String) {
        binding.layoutItem.textViewName.text = name
    }

    private fun setTypeAndAmount(amount: Double) {
        val type = IntBudgetTransactionType.getByAmount(amount)
        val color = ContextCompat.getColor(context, IntBudgetTransactionType.getColorRes(type))
        binding.layoutItem.textViewAmount.text = context.getString(getAmountFormat(amount), amount)
        binding.layoutItem.textViewAmount.setTextColor(color)
        binding.layoutItem.viewType.setBackgroundColor(color)
        binding.layoutItem.viewType.isVisible = false // bugging out :/
    }

    private fun setCreationUnixMs(creationUnixMs: Long) {
        // TODO: format date properly

        val locale = context.currentLocale
        binding.layoutItem.textViewDateTime.text = SimpleDateFormat("MMM dd yyyy HH:mm", locale).format(Date(creationUnixMs))
    }

    private fun setCategoryName(categoryName: String) {
        binding.layoutItem.textViewCategoryName.text = categoryName
    }

}