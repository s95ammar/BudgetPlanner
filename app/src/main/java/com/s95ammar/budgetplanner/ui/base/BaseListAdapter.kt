package com.s95ammar.budgetplanner.ui.base

import android.view.View
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlin.reflect.KProperty1

abstract class BaseListAdapter<T, VH : BaseListAdapter.BaseViewHolder<T>>(diffCallback: DiffUtil.ItemCallback<T>) :
    ListAdapter<T, VH>(diffCallback) {

    override fun onBindViewHolder(holder: VH, position: Int, payloads: MutableList<Any>) {
        try {
            @Suppress("UNCHECKED_CAST")
            bindHolder(holder, position, payloads.singleOrNull() as PayloadsHolder<T>)
        } catch (e: Exception) {
            super.onBindViewHolder(holder, position, payloads)
        }
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        bindHolder(holder, position)
    }

    private fun bindHolder(holder: VH, position: Int, payloads: PayloadsHolder<T> = PayloadsHolder()) {
        getItem(position)?.let { holder.bind(it, payloads) }
    }

    abstract class BaseViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {
        abstract fun bind(item: T, payloads: PayloadsHolder<T>)
    }

    class PayloadsHolder<T> {

        private val payloadTypes: MutableList<Int> = mutableListOf()

        fun addPayloadIfNotEqual(itemPair: Pair<T, T>, property: KProperty1<T, Any?>, payloadType: Int) {
            itemPair.let { (oldItem, newItem) ->
                if (property.get(oldItem) != property.get(newItem)) payloadTypes.add(payloadType)
            }
        }

        fun shouldUpdate(payloadType: Int): Boolean {
            return payloadTypes.isEmpty() || payloadTypes.contains(payloadType)
        }
    }
}
