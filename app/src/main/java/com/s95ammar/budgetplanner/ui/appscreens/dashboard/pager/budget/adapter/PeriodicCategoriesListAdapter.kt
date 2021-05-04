package com.s95ammar.budgetplanner.ui.appscreens.dashboard.pager.budget.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.recyclerview.widget.DiffUtil
import com.s95ammar.budgetplanner.R
import com.s95ammar.budgetplanner.databinding.ItemPeriodicCategoryBinding
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.common.data.PeriodicCategory
import com.s95ammar.budgetplanner.ui.base.BaseListAdapter
import kotlin.math.roundToInt

class PeriodicCategoriesListAdapter : BaseListAdapter<PeriodicCategory, PeriodicCategoriesListAdapter.PeriodicCategoriesViewHolder>(DiffUtilCallback()) {

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
                addPayloadIfNotEqual(PayloadType.AMOUNT_MAX, oldItem to newItem, PeriodicCategory::max, PeriodicCategory::budgetTransactionsAmountSum)
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
            if (payloads.shouldUpdate(PayloadType.AMOUNT_MAX)) setAmountAndMax(item.budgetTransactionsAmountSum, item.max)
        }

        private fun setCategoryName(categoryName: String) {
            binding.textViewCategoryName.text = categoryName
        }

        private fun setAmountAndMax(amount: Int, max: Int?) {
            binding.progressBar.isGone = max == null

            if (max != null) {
                binding.progressBar.progress = (amount * 100.0 / max).roundToInt()
                binding.textViewBudgetPeriodEntryValues.text = getString(
                    R.string.format_value_out_of, amount, max
                )
            } else {
                binding.textViewBudgetPeriodEntryValues.text = amount.toString()
            }

        }
    }

    object PayloadType {
        const val CATEGORY_NAME = 1
        const val AMOUNT_MAX = 2
    }
}