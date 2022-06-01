package com.s95ammar.budgetplanner.ui.common.simplemenu

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.s95ammar.budgetplanner.databinding.ItemSimpleMenuBinding

class SimpleMenuAdapter(
    private val onItemClick: (Int) -> Unit,
    private val onItemLongClick: ((Int) -> Unit)?
) : ListAdapter<SimpleMenuItem, SimpleMenuAdapter.MenuItemViewHolder>(DiffUtilCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuItemViewHolder {
        return MenuItemViewHolder(
            onItemClick,
            onItemLongClick,
            ItemSimpleMenuBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: MenuItemViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    companion object {

        class DiffUtilCallback : DiffUtil.ItemCallback<SimpleMenuItem>() {

            override fun areItemsTheSame(oldItem: SimpleMenuItem, newItem: SimpleMenuItem): Boolean {
                return (oldItem.getMenuItemId() == newItem.getMenuItemId())
            }
            override fun areContentsTheSame(oldItem: SimpleMenuItem, newItem: SimpleMenuItem): Boolean {
                return (oldItem.getMenuItemTitle() == newItem.getMenuItemTitle())
            }
        }
    }

    class MenuItemViewHolder(
        private val onItemClick: (Int) -> Unit,
        private val onItemLongClick: ((Int) -> Unit)?,
        private val binding: ItemSimpleMenuBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener { onItemClick(adapterPosition) }
            itemView.setOnLongClickListener { onItemLongClick?.invoke(adapterPosition); true }
        }

        fun bind(item: SimpleMenuItem) {
            binding.textViewTitle.text = item.getMenuItemTitle()
        }

    }
}
