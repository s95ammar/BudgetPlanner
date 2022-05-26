package com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.budgetransactioncreateedit.subscreens.categoryselection.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.s95ammar.budgetplanner.databinding.ItemCategoryBinding
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.budgetransactioncreateedit.data.CategoryOfPeriodSimple

class CategoriesOfPeriodSingleSelectionAdapter(
    private val onItemClick: (Int) -> Unit
) : ListAdapter<CategoryOfPeriodSimple, CategoriesOfPeriodSingleSelectionAdapter.CategoriesViewHolder>(DiffUtilCallback()) {

    companion object {
        class DiffUtilCallback : DiffUtil.ItemCallback<CategoryOfPeriodSimple>() {
            override fun areItemsTheSame(oldItem: CategoryOfPeriodSimple, newItem: CategoryOfPeriodSimple): Boolean {
                return (oldItem.id == newItem.id)
            }

            override fun areContentsTheSame(oldItem: CategoryOfPeriodSimple, newItem: CategoryOfPeriodSimple): Boolean {
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

        fun bind(item: CategoryOfPeriodSimple) {
            binding.textViewTitle.text = item.categoryName
        }

    }
}