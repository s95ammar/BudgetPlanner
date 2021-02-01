package com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.periods.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.s95ammar.budgetplanner.databinding.ItemPeriodBinding
import com.s95ammar.budgetplanner.models.view.PeriodSimpleViewEntity

class PeriodsListAdapter(
    private val onItemClick: (Int) -> Unit,
    private val onItemLongClick: (Int) -> Unit
) : ListAdapter<PeriodSimpleViewEntity, PeriodsListAdapter.PeriodsViewHolder>(CALLBACK) {

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
        val CALLBACK
            get() = object : DiffUtil.ItemCallback<PeriodSimpleViewEntity>() {
                override fun areItemsTheSame(oldItem: PeriodSimpleViewEntity, newItem: PeriodSimpleViewEntity) = (oldItem.id == newItem.id)
                override fun areContentsTheSame(oldItem: PeriodSimpleViewEntity, newItem: PeriodSimpleViewEntity) = (oldItem == newItem)
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

        fun bind(item: PeriodSimpleViewEntity) {
            binding.textViewTitle.text = item.name
        }

    }
}