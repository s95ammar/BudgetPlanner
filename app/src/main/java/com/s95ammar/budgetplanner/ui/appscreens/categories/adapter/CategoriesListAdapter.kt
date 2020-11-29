package com.s95ammar.budgetplanner.ui.appscreens.categories.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.s95ammar.budgetplanner.databinding.ItemCategoryBinding
import com.s95ammar.budgetplanner.models.view.CategoryViewEntity

class CategoriesListAdapter(
    private val onItemClick: (Int) -> Unit,
    private val onItemLongClick: (Int) -> Unit
) : ListAdapter<CategoryViewEntity, CategoriesListAdapter.CategoriesViewHolder>(CALLBACK) {

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

    companion object {
        val CALLBACK
            get() = object : DiffUtil.ItemCallback<CategoryViewEntity>() {
                override fun areItemsTheSame(oldItem: CategoryViewEntity, newItem: CategoryViewEntity) = (oldItem.id == newItem.id)
                override fun areContentsTheSame(oldItem: CategoryViewEntity, newItem: CategoryViewEntity) = (oldItem == newItem)
            }
    }

    class CategoriesViewHolder(
        private val onItemClick: (Int) -> Unit,
        private val onItemLongClick: (Int) -> Unit,
        private val binding: ItemCategoryBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener { onItemClick(adapterPosition) }
            itemView.setOnLongClickListener { onItemLongClick(adapterPosition); true }
        }

        fun bind(item: CategoryViewEntity) {
            binding.textViewCategoryItemTitle.text = item.name
        }

    }
}