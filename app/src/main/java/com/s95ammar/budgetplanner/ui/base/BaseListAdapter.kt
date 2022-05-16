package com.s95ammar.budgetplanner.ui.base

import android.view.View
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlin.reflect.KProperty1

abstract class BaseListAdapter<T, VH : BaseListAdapter.BaseViewHolder<T>>(diffCallback: DiffUtil.ItemCallback<T>) :
    ListAdapter<T, VH>(diffCallback) {

    override fun onBindViewHolder(holder: VH, position: Int, payloads: MutableList<Any>) {
        try {
            bindHolder(holder, position, payloads.singleOrNull() as PayloadsHolder)
        } catch (e: Exception) {
            onBindViewHolder(holder, position)
        }
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        bindHolder(holder, position)
    }

    private fun bindHolder(holder: VH, position: Int, payloads: PayloadsHolder = PayloadsHolder()) {
        getItem(position)?.let { holder.bind(it, payloads) }
    }

    abstract class BaseViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {
        open fun bind(item: T, payloads: PayloadsHolder) {}
        protected fun getColor(@ColorRes colorResId: Int) = ContextCompat.getColor(itemView.context, colorResId)
        protected fun getString(@StringRes stringResId: Int) = itemView.resources.getString(stringResId)
        protected fun getString(@StringRes stringResId: Int, vararg formatArgs: Any) = itemView.resources.getString(stringResId, *formatArgs)
    }

    class PayloadsHolder {

        private val payloadTypes: MutableList<Int> = mutableListOf()

        fun <T> addPayloadIfNotEqual(payloadType: Int, itemPair: Pair<T, T>, vararg properties: KProperty1<T, Any?>) {
            itemPair.let { (oldItem, newItem) ->
                if (properties.any { property -> property.get(oldItem) != property.get(newItem) }) payloadTypes.add(payloadType)
            }
        }

        fun <T> addPayloadIfNot(payloadType: Int, itemPair: Pair<T, T>, predicate: () -> Boolean) {
            itemPair.let {
                if (!predicate()) payloadTypes.add(payloadType)
            }
        }

        fun shouldUpdate(payloadType: Int): Boolean {
            return payloadTypes.isEmpty() || payloadTypes.contains(payloadType)
        }
    }
}
