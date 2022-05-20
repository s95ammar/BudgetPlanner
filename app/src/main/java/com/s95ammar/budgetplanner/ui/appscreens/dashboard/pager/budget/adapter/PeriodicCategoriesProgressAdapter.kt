package com.s95ammar.budgetplanner.ui.appscreens.dashboard.pager.budget.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.recyclerview.widget.DiffUtil
import com.s95ammar.budgetplanner.R
import com.s95ammar.budgetplanner.databinding.ItemPeriodicCategoryBinding
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.common.data.PeriodicCategory
import com.s95ammar.budgetplanner.ui.base.BaseListAdapter
import com.s95ammar.budgetplanner.util.getAmountFormatResId
import kotlin.math.absoluteValue
import kotlin.math.roundToInt

class PeriodicCategoriesProgressAdapter : BaseListAdapter<PeriodicCategory, PeriodicCategoriesProgressAdapter.PeriodicCategoriesViewHolder>(DiffUtilCallback()) {

    companion object {
        class DiffUtilCallback : DiffUtil.ItemCallback<PeriodicCategory>() {
            override fun areItemsTheSame(oldItem: PeriodicCategory, newItem: PeriodicCategory): Boolean {
                return (oldItem.categoryId == newItem.categoryId)
            }
            override fun areContentsTheSame(oldItem: PeriodicCategory, newItem: PeriodicCategory): Boolean {
                return (oldItem == newItem)
            }

            override fun getChangePayload(oldItem: PeriodicCategory, newItem: PeriodicCategory) = PayloadsHolder().apply {
                addPayloadIfNotEqual(PayloadType.CATEGORY_NAME, oldItem to newItem, PeriodicCategory::categoryName)
                addPayloadIfNotEqual(
                    PayloadType.AMOUNT_AND_ESTIMATE,
                    oldItem to newItem,
                    PeriodicCategory::estimate,
                    PeriodicCategory::budgetTransactionsAmountSum
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PeriodicCategoriesViewHolder {
        return PeriodicCategoriesViewHolder(
            ItemPeriodicCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    class PeriodicCategoriesViewHolder(
        private val binding: ItemPeriodicCategoryBinding
    ) : BaseListAdapter.BaseViewHolder<PeriodicCategory>(binding.root) {

        override fun bind(item: PeriodicCategory, payloads: PayloadsHolder) {
            if (payloads.shouldUpdate(PayloadType.CATEGORY_NAME)) setCategoryName(item.categoryName)
            if (payloads.shouldUpdate(PayloadType.AMOUNT_AND_ESTIMATE)) setAmountAndEstimate(item)
        }

        private fun setCategoryName(categoryName: String) {
            binding.textViewCategoryName.text = categoryName
        }

        private fun setAmountAndEstimate(periodicCategory: PeriodicCategory) {
            val amountSum = periodicCategory.budgetTransactionsAmountSum
            val estimate = periodicCategory.estimate
            binding.progressBar.isGone = estimate == null

            if (estimate != null) {
                binding.progressBar.progress = (amountSum * 100.0 / estimate).roundToInt()
            }

            binding.textViewBudgetPeriodEntryValues.text = getAmountAndEstimateString(periodicCategory)
        }

        private fun getAmountAndEstimateString(periodicCategory: PeriodicCategory): String {
            val amountSum = periodicCategory.budgetTransactionsAmountSum
            val estimate = periodicCategory.estimate

            return if (estimate != null) {
                val areSumAndEstimateOfDifferentSigns = amountSum * estimate < 0
                val sumFormatted = getString(
                    getAmountFormatResId(amountSum, includePlusSign = areSumAndEstimateOfDifferentSigns),
                    if (areSumAndEstimateOfDifferentSigns) amountSum else amountSum.absoluteValue
                )
                val estimateFormatted = getString(
                    getAmountFormatResId(estimate, includePlusSign = areSumAndEstimateOfDifferentSigns),
                    if (areSumAndEstimateOfDifferentSigns) estimate else estimate.absoluteValue
                )
                val progressFormatted = getString(
                    R.string.format_value_out_of,
                    sumFormatted,
                    estimateFormatted
                )
                getString(
                    R.string.format_amount_string_with_currency,
                    progressFormatted,
                    periodicCategory.currencyCode
                )
            } else {
                val sumFormatted = getString(
                    getAmountFormatResId(amountSum, includePlusSign = false),
                    amountSum.absoluteValue
                )
                getString(
                    R.string.format_amount_string_with_currency,
                    sumFormatted,
                    periodicCategory.currencyCode
                )
            }
        }
    }

    object PayloadType {
        const val CATEGORY_NAME = 1
        const val AMOUNT_AND_ESTIMATE = 2
    }
}