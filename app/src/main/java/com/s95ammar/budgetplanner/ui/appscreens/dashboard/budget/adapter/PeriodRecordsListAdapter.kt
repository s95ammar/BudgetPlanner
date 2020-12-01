package com.s95ammar.budgetplanner.ui.appscreens.dashboard.budget.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.s95ammar.budgetplanner.R
import com.s95ammar.budgetplanner.databinding.ItemPeriodRecordBinding
import com.s95ammar.budgetplanner.models.view.PeriodRecordViewEntity

class PeriodRecordsListAdapter(
    private val onItemClick: ((Int) -> Unit)? = null /*TODO*/,
    private val onItemLongClick: ((Int) -> Unit)? = null /*TODO*/
) : ListAdapter<PeriodRecordViewEntity, PeriodRecordsListAdapter.PeriodRecordsViewHolder>(CALLBACK) {

    companion object {
        val CALLBACK
            get() = object : DiffUtil.ItemCallback<PeriodRecordViewEntity>() {
                override fun areItemsTheSame(oldItem: PeriodRecordViewEntity, newItem: PeriodRecordViewEntity) = (oldItem.id == newItem.id)
                override fun areContentsTheSame(oldItem: PeriodRecordViewEntity, newItem: PeriodRecordViewEntity) = (oldItem == newItem)
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PeriodRecordsViewHolder {
        return PeriodRecordsViewHolder(
            onItemClick,
            onItemLongClick,
            ItemPeriodRecordBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: PeriodRecordsViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    class PeriodRecordsViewHolder(
        private val onItemClick: ((Int) -> Unit)?,
        private val onItemLongClick: ((Int) -> Unit)?,
        private val binding: ItemPeriodRecordBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener { onItemClick?.invoke(adapterPosition) }
            itemView.setOnLongClickListener { onItemLongClick?.invoke(adapterPosition); true }
        }

        fun bind(item: PeriodRecordViewEntity) {
            binding.textViewCategoryName.text = item.categoryName
            val isMaxAvailable = (item.max != null)
            binding.progressBar.isVisible = isMaxAvailable

            if (isMaxAvailable) {
                binding.progressBar.progress = item.amount
                binding.textViewBudgetPeriodEntryValues.text = itemView.resources.getString(
                    R.string.format_value_out_of, item.amount, item.max
                )
            } else {
                binding.textViewBudgetPeriodEntryValues.text = item.amount.toString()
            }
        }

    }
}