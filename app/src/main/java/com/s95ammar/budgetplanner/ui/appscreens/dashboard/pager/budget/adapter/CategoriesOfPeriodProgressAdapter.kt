package com.s95ammar.budgetplanner.ui.appscreens.dashboard.pager.budget.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.recyclerview.widget.DiffUtil
import com.s95ammar.budgetplanner.R
import com.s95ammar.budgetplanner.databinding.ItemCategoryOfPeriodBinding
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.common.data.CategoryOfPeriod
import com.s95ammar.budgetplanner.ui.base.BaseListAdapter
import com.s95ammar.budgetplanner.util.getAmountStringFormatted
import kotlin.math.absoluteValue
import kotlin.math.roundToInt

class CategoriesOfPeriodProgressAdapter : BaseListAdapter<CategoryOfPeriod, CategoriesOfPeriodProgressAdapter.CategoriesOfPeriodViewHolder>(DiffUtilCallback()) {

    companion object {
        class DiffUtilCallback : DiffUtil.ItemCallback<CategoryOfPeriod>() {
            override fun areItemsTheSame(oldItem: CategoryOfPeriod, newItem: CategoryOfPeriod): Boolean {
                return (oldItem.categoryId == newItem.categoryId)
            }
            override fun areContentsTheSame(oldItem: CategoryOfPeriod, newItem: CategoryOfPeriod): Boolean {
                return (oldItem == newItem)
            }

            override fun getChangePayload(oldItem: CategoryOfPeriod, newItem: CategoryOfPeriod) = PayloadsHolder().apply {
                addPayloadIfNotEqual(PayloadType.CATEGORY_NAME, oldItem to newItem, CategoryOfPeriod::categoryName)
                addPayloadIfNotEqual(
                    PayloadType.AMOUNT_AND_ESTIMATE,
                    oldItem to newItem,
                    CategoryOfPeriod::estimate,
                    CategoryOfPeriod::budgetTransactionsAmountSum
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriesOfPeriodViewHolder {
        return CategoriesOfPeriodViewHolder(
            ItemCategoryOfPeriodBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    class CategoriesOfPeriodViewHolder(
        private val binding: ItemCategoryOfPeriodBinding
    ) : BaseListAdapter.BaseViewHolder<CategoryOfPeriod>(binding.root) {

        override fun bind(item: CategoryOfPeriod, payloads: PayloadsHolder) {
            if (payloads.shouldUpdate(PayloadType.CATEGORY_NAME)) setCategoryName(item.categoryName)
            if (payloads.shouldUpdate(PayloadType.AMOUNT_AND_ESTIMATE)) setAmountAndEstimate(item)
        }

        private fun setCategoryName(categoryName: String) {
            binding.textViewCategoryName.text = categoryName
        }

        private fun setAmountAndEstimate(categoryOfPeriod: CategoryOfPeriod) {
            val amountSum = categoryOfPeriod.budgetTransactionsAmountSum
            val estimate = categoryOfPeriod.estimate
            binding.progressBar.isGone = estimate == null

            if (estimate != null) {
                binding.progressBar.progress = (amountSum * 100.0 / estimate).roundToInt()
            }

            binding.textViewBudgetPeriodEntryValues.text = getAmountAndEstimateString(categoryOfPeriod)
        }

        private fun getAmountAndEstimateString(categoryOfPeriod: CategoryOfPeriod): String {
            val amountSum = categoryOfPeriod.budgetTransactionsAmountSum
            val estimate = categoryOfPeriod.estimate

            return if (estimate != null) {
                val areSumAndEstimateOfDifferentSigns = amountSum * estimate < 0
                val sumFormatted = getAmountStringFormatted(
                    if (areSumAndEstimateOfDifferentSigns) amountSum else amountSum.absoluteValue,
                    includePlusSign = areSumAndEstimateOfDifferentSigns
                )
                val estimateFormattedWithCurrency = getAmountStringFormatted(
                    if (areSumAndEstimateOfDifferentSigns) estimate else estimate.absoluteValue,
                    includePlusSign = areSumAndEstimateOfDifferentSigns,
                    currencyCode = categoryOfPeriod.currencyCode
                )
                getString(
                    R.string.format_value_out_of,
                    sumFormatted,
                    estimateFormattedWithCurrency
                )
            } else {
                getAmountStringFormatted(
                    amountSum.absoluteValue,
                    includePlusSign = false,
                    currencyCode = categoryOfPeriod.currencyCode
                )
            }
        }
    }

    object PayloadType {
        const val CATEGORY_NAME = 1
        const val AMOUNT_AND_ESTIMATE = 2
    }
}