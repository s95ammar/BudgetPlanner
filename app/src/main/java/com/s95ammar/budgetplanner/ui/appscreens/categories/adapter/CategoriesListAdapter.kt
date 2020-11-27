package com.s95ammar.budgetplanner.ui.appscreens.categories.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.s95ammar.budgetplanner.R
import kotlinx.android.synthetic.main.item_category.view.*

class CategoriesListAdapter(
    private val onItemClick: (Int) -> Unit,
    private val onItemLongClick: (Int) -> Unit
)/* : ListAdapter<Category, CategoriesListAdapter.CategoriesViewHolder>(CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriesViewHolder {
        return CategoriesViewHolder(
            onItemClick,
            onItemLongClick,
            LayoutInflater.from(parent.context).inflate(R.layout.item_category, parent, false)
        )
    }

    override fun onBindViewHolder(holder: CategoriesViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    companion object {
        val CALLBACK
            get() = object : DiffUtil.ItemCallback<Category>() {
                override fun areItemsTheSame(oldItem: Category, newItem: Category) = (oldItem.id == newItem.id)
                override fun areContentsTheSame(oldItem: Category, newItem: Category) = (oldItem == newItem)
            }
    }

    class CategoriesViewHolder(
        private val onItemClick: (Int) -> Unit,
        private val onItemLongClick: (Int) -> Unit,
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        init {
            itemView.setOnClickListener { onItemClick(adapterPosition) }
            itemView.setOnLongClickListener { onItemLongClick(adapterPosition); true }
        }

        fun bind(item: Category) {
            itemView.text_view_category_item_title.text = item.name
        }

    }
}*/