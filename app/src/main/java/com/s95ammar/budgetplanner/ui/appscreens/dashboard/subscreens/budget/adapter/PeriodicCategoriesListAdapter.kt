package com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.budget.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.s95ammar.budgetplanner.R
import com.s95ammar.budgetplanner.databinding.ItemPeriodicCategoryBinding
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.common.data.PeriodicCategoryViewEntity
import kotlin.math.roundToInt

class PeriodicCategoriesListAdapter : ListAdapter<PeriodicCategoryViewEntity, PeriodicCategoriesListAdapter.PeriodicCategoriesViewHolder>(DiffUtilCallback()) {

    companion object {
        class DiffUtilCallback : DiffUtil.ItemCallback<PeriodicCategoryViewEntity>() {
            override fun areItemsTheSame(oldItem: PeriodicCategoryViewEntity, newItem: PeriodicCategoryViewEntity): Boolean {
                return (oldItem.categoryId == newItem.categoryId)
            }
            override fun areContentsTheSame(oldItem: PeriodicCategoryViewEntity, newItem: PeriodicCategoryViewEntity): Boolean {
                return (oldItem == newItem)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PeriodicCategoriesViewHolder {
        return PeriodicCategoriesViewHolder(
            ItemPeriodicCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: PeriodicCategoriesViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    class PeriodicCategoriesViewHolder(
        private val binding: ItemPeriodicCategoryBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: PeriodicCategoryViewEntity) {
            binding.textViewCategoryName.text = item.categoryName
            val max = item.max

            binding.progressBar.isGone = max == null

            if (max != null) {
                binding.progressBar.progress = (item.amount * 100.0 / max).roundToInt()
                binding.textViewBudgetPeriodEntryValues.text = itemView.resources.getString(
                    R.string.format_value_out_of, item.amount, max
                )
            } else {
                binding.textViewBudgetPeriodEntryValues.text = item.amount.toString()
            }
        }

    }
}