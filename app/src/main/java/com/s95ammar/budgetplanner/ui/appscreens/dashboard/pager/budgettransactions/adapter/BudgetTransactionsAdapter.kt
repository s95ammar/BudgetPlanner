package com.s95ammar.budgetplanner.ui.appscreens.dashboard.pager.budgettransactions.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import com.s95ammar.budgetplanner.R
import com.s95ammar.budgetplanner.databinding.ItemBudgetTransactionBinding
import com.s95ammar.budgetplanner.databinding.ItemViewOnMapBinding
import com.s95ammar.budgetplanner.databinding.LayoutEmptyBinding
import com.s95ammar.budgetplanner.models.IntBudgetTransactionType
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.common.data.BudgetTransaction
import com.s95ammar.budgetplanner.ui.base.BaseListAdapter
import com.s95ammar.budgetplanner.util.currentLocale
import com.s95ammar.budgetplanner.util.getAmountFormatResId
import java.text.SimpleDateFormat
import java.util.Date

class BudgetTransactionsAdapter(
    private val onMapClick: () -> Unit,
    private val onItemClick: (BudgetTransaction) -> Unit,
    private val onItemLongClick: (BudgetTransaction) -> Unit,
) : BaseListAdapter<BudgetTransactionsItemType, BudgetTransactionsAdapter.ViewHolder>(DiffUtilCallback()) {

    companion object {
        class DiffUtilCallback : DiffUtil.ItemCallback<BudgetTransactionsItemType>() {
            override fun areItemsTheSame(oldItem: BudgetTransactionsItemType, newItem: BudgetTransactionsItemType): Boolean {
                return when (oldItem) {
                    is BudgetTransactionsItemType.ViewOnMap -> {
                        oldItem.periodId == (newItem as? BudgetTransactionsItemType.ViewOnMap)?.periodId
                    }
                    is BudgetTransactionsItemType.ListItem -> {
                        oldItem.budgetTransaction.id == (newItem as? BudgetTransactionsItemType.ListItem)?.budgetTransaction?.id
                    }
                }
            }

            override fun areContentsTheSame(oldItem: BudgetTransactionsItemType, newItem: BudgetTransactionsItemType): Boolean {
                return (oldItem == newItem)
            }

            override fun getChangePayload(
                oldItem: BudgetTransactionsItemType,
                newItem: BudgetTransactionsItemType
            ): PayloadsHolder {
                return when (oldItem) {
                    is BudgetTransactionsItemType.ViewOnMap -> PayloadsHolder()
                    is BudgetTransactionsItemType.ListItem -> {
                        PayloadsHolder().apply {
                            if (newItem is BudgetTransactionsItemType.ListItem) {
                                val oldBtItem = oldItem.budgetTransaction
                                val newBtItem = newItem.budgetTransaction
                                addPayloadIfNotEqual(PayloadType.NAME, oldBtItem to newBtItem, BudgetTransaction::name)
                                addPayloadIfNotEqual(
                                    PayloadType.AMOUNT,
                                    oldBtItem to newBtItem,
                                    BudgetTransaction::amount,
                                    BudgetTransaction::currencyCode,
                                )
                                addPayloadIfNotEqual(
                                    PayloadType.CREATION_UNIX_MS,
                                    oldBtItem to newBtItem,
                                    BudgetTransaction::creationUnixMs
                                )
                                addPayloadIfNotEqual(PayloadType.CATEGORY_NAME, oldBtItem to newBtItem, BudgetTransaction::categoryName)
                            }
                        }
                    }
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is BudgetTransactionsItemType.ViewOnMap -> R.layout.item_view_on_map
            is BudgetTransactionsItemType.ListItem -> R.layout.item_budget_transaction
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            R.layout.item_view_on_map -> ViewOnMapViewHolder(
                onMapClick,
                ItemViewOnMapBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
            R.layout.item_budget_transaction -> BudgetTransactionListItemViewHolder(
                onItemClick,
                onItemLongClick,
                ItemBudgetTransactionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
            else -> EmptyViewHolder(
                LayoutEmptyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
        }
    }

    sealed class ViewHolder(view: View) : BaseViewHolder<BudgetTransactionsItemType>(view)

    class EmptyViewHolder(binding: LayoutEmptyBinding) : ViewHolder(binding.root)

    class ViewOnMapViewHolder(
        private val onItemClick: () -> Unit,
        private val binding: ItemViewOnMapBinding
    ) : ViewHolder(binding.root) {

        init {
            binding.viewOnMapButton.setOnClickListener { onItemClick() }
        }
    }

    class BudgetTransactionListItemViewHolder(
        private val onItemClick: (BudgetTransaction) -> Unit,
        private val onItemLongClick: (BudgetTransaction) -> Unit,
        private val binding: ItemBudgetTransactionBinding
    ) : ViewHolder(binding.root) {

        override fun bind(item: BudgetTransactionsItemType, payloads: PayloadsHolder) {
            val bt = (item as BudgetTransactionsItemType.ListItem).budgetTransaction
            itemView.setOnClickListener { onItemClick(bt) }
            itemView.setOnLongClickListener { onItemLongClick(bt); true }
            if (payloads.shouldUpdate(PayloadType.NAME)) setName(bt.name)
            if (payloads.shouldUpdate(PayloadType.AMOUNT)) setTypeAndAmount(bt.amount, bt.currencyCode)
            if (payloads.shouldUpdate(PayloadType.CREATION_UNIX_MS)) setCreationUnixMs(bt.creationUnixMs)
            if (payloads.shouldUpdate(PayloadType.CATEGORY_NAME)) setCategoryName(bt.categoryName)
        }

        private fun setName(name: String) {
            binding.textViewName.text = name
        }

        private fun setTypeAndAmount(amount: Double, currencyCode: String) {
            val type = IntBudgetTransactionType.getByAmount(amount)
            val color = ContextCompat.getColor(itemView.context, IntBudgetTransactionType.getColorRes(type))
            val amountFormatted = getString(getAmountFormatResId(amount), amount)
            binding.textViewAmount.text = getString(R.string.format_amount_string_with_currency, amountFormatted, currencyCode)
            binding.textViewAmount.setTextColor(color)
            binding.viewType.setBackgroundColor(color)
        }

        private fun setCreationUnixMs(creationUnixMs: Long) {
            // TODO: format date properly

            val locale = itemView.context.currentLocale

            binding.textViewDateTime.text = SimpleDateFormat("MMM dd yyyy HH:mm", locale).format(Date(creationUnixMs))
        }

        private fun setCategoryName(categoryName: String) {
            binding.textViewCategoryName.text = categoryName
        }
    }

    object PayloadType {
        const val NAME = 1
        const val AMOUNT = 2
        const val CREATION_UNIX_MS = 3
        const val CATEGORY_NAME = 4
    }

}