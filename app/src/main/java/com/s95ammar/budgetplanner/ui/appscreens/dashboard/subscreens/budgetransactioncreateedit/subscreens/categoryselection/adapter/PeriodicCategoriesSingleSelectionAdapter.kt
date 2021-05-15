package com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.budgetransactioncreateedit.subscreens.categoryselection.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.s95ammar.budgetplanner.databinding.ItemCategoryBinding
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.budgetransactioncreateedit.data.PeriodicCategoryIdAndName

class PeriodicCategoriesSingleSelectionAdapter(
    private val onItemClick: (Int) -> Unit
) : ListAdapter<PeriodicCategoryIdAndName, PeriodicCategoriesSingleSelectionAdapter.CategoriesViewHolder>(DiffUtilCallback()) {

    companion object {
        class DiffUtilCallback : DiffUtil.ItemCallback<PeriodicCategoryIdAndName>() {
            override fun areItemsTheSame(oldItem: PeriodicCategoryIdAndName, newItem: PeriodicCategoryIdAndName): Boolean {
                return (oldItem.periodicCategoryId == newItem.periodicCategoryId)
            }

            override fun areContentsTheSame(oldItem: PeriodicCategoryIdAndName, newItem: PeriodicCategoryIdAndName): Boolean {
                return (oldItem.categoryName == newItem.categoryName)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriesViewHolder {
        return CategoriesViewHolder(
            onItemClick,
            ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: CategoriesViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    class CategoriesViewHolder(
        private val onItemClick: (Int) -> Unit,
        private val binding: ItemCategoryBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener { onItemClick(adapterPosition) }
        }

        fun bind(item: PeriodicCategoryIdAndName) {
            binding.textViewTitle.text = item.categoryName
        }

    }
}