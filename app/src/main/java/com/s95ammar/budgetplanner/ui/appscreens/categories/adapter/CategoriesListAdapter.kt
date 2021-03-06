package com.s95ammar.budgetplanner.ui.appscreens.categories.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.s95ammar.budgetplanner.databinding.ItemCategoryBinding
import com.s95ammar.budgetplanner.ui.appscreens.categories.common.data.CategoryViewEntity

class CategoriesListAdapter(
    private val onItemClick: (Int) -> Unit,
    private val onItemLongClick: ((Int) -> Unit)?
) : ListAdapter<CategoryViewEntity, CategoriesListAdapter.CategoriesViewHolder>(DiffUtilCallback()) {

    companion object {
        class DiffUtilCallback : DiffUtil.ItemCallback<CategoryViewEntity>() {
            override fun areItemsTheSame(oldItem: CategoryViewEntity, newItem: CategoryViewEntity) = (oldItem.id == newItem.id)
            override fun areContentsTheSame(oldItem: CategoryViewEntity, newItem: CategoryViewEntity) = (oldItem == newItem)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriesViewHolder {
        return CategoriesViewHolder(
            onItemClick,
            onItemLongClick,
            ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: CategoriesViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    class CategoriesViewHolder(
        private val onItemClick: (Int) -> Unit,
        private val onItemLongClick: ((Int) -> Unit)?,
        private val binding: ItemCategoryBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener { onItemClick(adapterPosition) }
            itemView.setOnLongClickListener { onItemLongClick?.invoke(adapterPosition); true }
        }

        fun bind(item: CategoryViewEntity) {
            binding.textViewTitle.text = item.name
        }

    }
}