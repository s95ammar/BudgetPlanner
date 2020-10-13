package com.s95ammar.budgetplanner.ui.appscreens.budgetslist.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import com.s95ammar.budgetplanner.R
import com.s95ammar.budgetplanner.ui.base.BaseListAdapter
import com.s95ammar.budgetplanner.ui.appscreens.budgetslist.data.BudgetViewEntity
import kotlinx.android.synthetic.main.item_budget.view.*

class BudgetsListAdapter(
    private val onItemClick: (Int) -> Unit,
    private val onItemLongClick: (Int) -> Unit
): BaseListAdapter<BudgetViewEntity, BudgetsListAdapter.BudgetsListViewHolder>(BudgetItemCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BudgetsListViewHolder {
        return BudgetsListViewHolder(
            onItemClick,
            onItemLongClick,
            LayoutInflater.from(parent.context).inflate(R.layout.item_budget, parent, false)
        )
    }

    class BudgetsListViewHolder(
        val onItemClick: (Int) -> Unit,
        val onItemLongClick: (Int) -> Unit,
        itemView: View
    ): BaseViewHolder<BudgetViewEntity>(itemView) {

        init {
            itemView.setOnClickListener { onItemClick(adapterPosition) }
            itemView.setOnLongClickListener { onItemLongClick(adapterPosition); true }
        }

        override fun bind(item: BudgetViewEntity, payloads: PayloadsHolder<BudgetViewEntity>) {

            if (payloads.shouldUpdate(PayloadsType.NAME))
                itemView.text_view_budget_item_title.text = item.name

            if (payloads.shouldUpdate(PayloadsType.IS_ACTIVE))
                itemView.image_view_budgetItem_active.isVisible = item.isActive

            if (payloads.shouldUpdate(PayloadsType.TOTAL_BALANCE))
                itemView.text_view_budget_item_total_balance_value.text = item.totalBalance.toString()

            if (payloads.shouldUpdate(PayloadsType.TOTAL_SPENDING_ESTIMATE)) {
                itemView.text_view_budget_item_total_spending_estimate_value.text = item.totalSpendingEstimate?.toString()
                itemView.progress_budget_item_total_spending_estimate?.isVisible = item.totalSpendingEstimate == null
            }

            if (payloads.shouldUpdate(PayloadsType.TOTAL_SAVINGS)) {
                itemView.text_view_budget_item_total_savings_value.text = item.totalSavings?.toString()
                itemView.progress_budget_item_total_savings?.isVisible = item.totalSavings == null
            }
        }
    }

    class BudgetItemCallback : DiffUtil.ItemCallback<BudgetViewEntity>() {
        override fun areItemsTheSame(oldItem: BudgetViewEntity, newItem: BudgetViewEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: BudgetViewEntity, newItem: BudgetViewEntity): Boolean {
            return oldItem == newItem
        }

        override fun getChangePayload(oldItem: BudgetViewEntity, newItem: BudgetViewEntity): Any? {
            return PayloadsHolder<BudgetViewEntity>().apply {
                val itemPair = oldItem to newItem

                addPayloadIfNotEqual(itemPair, BudgetViewEntity::name, PayloadsType.NAME)
                addPayloadIfNotEqual(itemPair, BudgetViewEntity::isActive, PayloadsType.IS_ACTIVE)
                addPayloadIfNotEqual(itemPair, BudgetViewEntity::totalBalance, PayloadsType.TOTAL_BALANCE)
                addPayloadIfNotEqual(itemPair, BudgetViewEntity::totalSpendingEstimate, PayloadsType.TOTAL_SPENDING_ESTIMATE)
                addPayloadIfNotEqual(itemPair, BudgetViewEntity::totalSavings, PayloadsType.TOTAL_SAVINGS)

            }
        }
    }

    object PayloadsType {
        const val NAME = 0
        const val IS_ACTIVE = 1
        const val TOTAL_BALANCE = 2
        const val TOTAL_SPENDING_ESTIMATE = 3
        const val TOTAL_SAVINGS = 4
    }

}