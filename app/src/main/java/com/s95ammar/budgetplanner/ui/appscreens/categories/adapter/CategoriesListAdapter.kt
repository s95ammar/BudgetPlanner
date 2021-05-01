package com.s95ammar.budgetplanner.ui.appscreens.categories.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.s95ammar.budgetplanner.databinding.ItemCategoryBinding
import com.s95ammar.budgetplanner.ui.appscreens.categories.common.data.Category

class CategoriesListAdapter(
    private val onItemClick: (Int) -> Unit,
    private val onItemLongClick: ((Int) -> Unit)?
) : ListAdapter<Category, CategoriesListAdapter.CategoriesViewHolder>(DiffUtilCallback()) {

    companion object {
        class DiffUtilCallback : DiffUtil.ItemCallback<Category>() {
            override fun areItemsTheSame(oldItem: Category, newItem: Category) = (oldItem.id == newItem.id)
            override fun areContentsTheSame(oldItem: Category, newItem: Category) = (oldItem == newItem)
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

        fun bind(item: Category) {
            binding.textViewTitle.text = item.name
        }

    }
}