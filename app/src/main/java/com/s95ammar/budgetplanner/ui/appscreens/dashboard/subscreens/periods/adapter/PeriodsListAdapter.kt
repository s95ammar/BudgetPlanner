package com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.periods.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.s95ammar.budgetplanner.databinding.ItemPeriodBinding
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.common.data.PeriodSimple

class PeriodsListAdapter(
    private val onItemClick: (Int) -> Unit,
    private val onItemLongClick: (Int) -> Unit
) : ListAdapter<PeriodSimple, PeriodsListAdapter.PeriodsViewHolder>(DiffUtilCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PeriodsViewHolder {
        return PeriodsViewHolder(
            onItemClick,
            onItemLongClick,
            ItemPeriodBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: PeriodsViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    companion object {
        class DiffUtilCallback : DiffUtil.ItemCallback<PeriodSimple>() {
            override fun areItemsTheSame(oldItem: PeriodSimple, newItem: PeriodSimple) = (oldItem.id == newItem.id)
            override fun areContentsTheSame(oldItem: PeriodSimple, newItem: PeriodSimple) = (oldItem == newItem)
        }
    }

    class PeriodsViewHolder(
        private val onItemClick: (Int) -> Unit,
        private val onItemLongClick: (Int) -> Unit,
        private val binding: ItemPeriodBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener { onItemClick(adapterPosition) }
            itemView.setOnLongClickListener { onItemLongClick(adapterPosition); true }
        }

        fun bind(item: PeriodSimple) {
            binding.textViewTitle.text = item.name
        }

    }
}