package com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.budget.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.s95ammar.budgetplanner.R
import com.s95ammar.budgetplanner.databinding.ItemPeriodRecordBinding
import com.s95ammar.budgetplanner.models.view.PeriodRecordViewEntity
import kotlin.math.roundToInt

class PeriodRecordsListAdapter : ListAdapter<PeriodRecordViewEntity, PeriodRecordsListAdapter.PeriodRecordsViewHolder>(CALLBACK) {

    companion object {
        val CALLBACK
            get() = object : DiffUtil.ItemCallback<PeriodRecordViewEntity>() {
                override fun areItemsTheSame(oldItem: PeriodRecordViewEntity, newItem: PeriodRecordViewEntity): Boolean {
                    return (oldItem.categoryId == newItem.categoryId)
                }
                override fun areContentsTheSame(oldItem: PeriodRecordViewEntity, newItem: PeriodRecordViewEntity): Boolean {
                    return (oldItem == newItem)
                }
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PeriodRecordsViewHolder {
        return PeriodRecordsViewHolder(
            ItemPeriodRecordBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: PeriodRecordsViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    class PeriodRecordsViewHolder(
        private val binding: ItemPeriodRecordBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: PeriodRecordViewEntity) {
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