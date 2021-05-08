package com.s95ammar.budgetplanner.ui.appscreens.dashboard.pager.budgettransactions.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.s95ammar.budgetplanner.R
import com.s95ammar.budgetplanner.databinding.ItemBudgetTransactionBinding
import com.s95ammar.budgetplanner.models.IntBudgetTransactionType
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.common.data.BudgetTransaction
import com.s95ammar.budgetplanner.ui.base.BaseListAdapter
import java.text.SimpleDateFormat
import java.util.Date

class BudgetTransactionsListAdapter : BaseListAdapter<BudgetTransaction, BudgetTransactionsListAdapter.BudgetTransactionsViewHolder>(
    DiffUtilCallback()
) {

    companion object {
        class DiffUtilCallback : DiffUtil.ItemCallback<BudgetTransaction>() {
            override fun areItemsTheSame(oldItem: BudgetTransaction, newItem: BudgetTransaction): Boolean {
                return (oldItem.id == newItem.id)
            }

            override fun areContentsTheSame(oldItem: BudgetTransaction, newItem: BudgetTransaction): Boolean {
                return (oldItem == newItem)
            }

            override fun getChangePayload(oldItem: BudgetTransaction, newItem: BudgetTransaction) = PayloadsHolder().apply {
                addPayloadIfNotEqual(PayloadType.NAME, oldItem to newItem, BudgetTransaction::name)
                addPayloadIfNotEqual(PayloadType.TYPE, oldItem to newItem, BudgetTransaction::type)
                addPayloadIfNotEqual(PayloadType.AMOUNT, oldItem to newItem, BudgetTransaction::amount)
                addPayloadIfNotEqual(PayloadType.CREATION_UNIX_MS, oldItem to newItem, BudgetTransaction::creationUnixMs)
                addPayloadIfNotEqual(PayloadType.CATEGORY_NAME, oldItem to newItem, BudgetTransaction::categoryName)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BudgetTransactionsViewHolder {
        return BudgetTransactionsViewHolder(
            ItemBudgetTransactionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    class BudgetTransactionsViewHolder(
        private val binding: ItemBudgetTransactionBinding
    ) : BaseListAdapter.BaseViewHolder<BudgetTransaction>(binding.root) {

        override fun bind(item: BudgetTransaction, payloads: PayloadsHolder) {
            if (payloads.shouldUpdate(PayloadType.NAME)) setName(item.name)
            if (payloads.shouldUpdate(PayloadType.TYPE)) setType(item.type)
            if (payloads.shouldUpdate(PayloadType.AMOUNT)) setAmount(item.amount, item.type)
            if (payloads.shouldUpdate(PayloadType.CREATION_UNIX_MS)) setCreationUnixMs(item.creationUnixMs)
            if (payloads.shouldUpdate(PayloadType.CATEGORY_NAME)) setCategoryName(item.categoryName)
        }

        private fun setName(name: String) {
            binding.textViewName.text = name
        }

        private fun setType(@IntBudgetTransactionType type: Int) {
            when (type) {
                IntBudgetTransactionType.EXPENSE -> {
                    binding.viewType.setBackgroundColor(getColor(R.color.colorRed))
                }
                IntBudgetTransactionType.INCOME -> {
                    binding.viewType.setBackgroundColor(getColor(R.color.colorGreen))
                }
            }
        }

        private fun setAmount(amount: Int, @IntBudgetTransactionType type: Int) {
            // TODO: thousands separator
            val amountFormattedString = amount.toString()
            when (type) {
                IntBudgetTransactionType.EXPENSE -> {
                    binding.textViewAmount.setTextColor(getColor(R.color.colorRed))
                    binding.textViewAmount.text = getString(R.string.format_minus_sign_prefix, amountFormattedString)
                }
                IntBudgetTransactionType.INCOME -> {
                    binding.textViewAmount.setTextColor(getColor(R.color.colorGreen))
                    binding.textViewAmount.text = getString(R.string.format_plus_sign_prefix, amountFormattedString)
                }
            }
        }

        @Suppress("Deprecation")
        private fun setCreationUnixMs(creationUnixMs: Long) {
            // TODO: format date properly


            val locale = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) itemView.resources.configuration.locales[0] else itemView.resources.configuration.locale
            binding.textViewDateTime.text = SimpleDateFormat("MMM dd yyyy HH:mm", locale).format(Date(creationUnixMs))
        }

        private fun setCategoryName(categoryName: String) {
            binding.textViewCategoryName.text = categoryName
        }
    }

    object PayloadType {
        const val NAME = 1
        const val TYPE = 2
        const val AMOUNT = 3
        const val CREATION_UNIX_MS = 4
        const val CATEGORY_NAME = 5
    }

}