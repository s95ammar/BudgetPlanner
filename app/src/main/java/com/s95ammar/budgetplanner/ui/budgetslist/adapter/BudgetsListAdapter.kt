package com.s95ammar.budgetplanner.ui.budgetslist.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.s95ammar.budgetplanner.R
import com.s95ammar.budgetplanner.ui.base.BaseListAdapter
import com.s95ammar.budgetplanner.ui.budgetslist.entity.BudgetViewEntity

class BudgetsListAdapter: BaseListAdapter<BudgetViewEntity, BudgetsListAdapter.BudgetsListViewHolder>(BudgetItemCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BudgetsListViewHolder {
        return BudgetsListViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_budget, parent, false))
    }

    class BudgetsListViewHolder(itemView: View): BaseViewHolder<BudgetViewEntity>(itemView) {

        override fun bind(item: BudgetViewEntity, payloads: PayloadsHolder<BudgetViewEntity>) {

        }
    }

    class BudgetItemCallback : DiffUtil.ItemCallback<BudgetViewEntity>() {
        override fun areItemsTheSame(oldItem: BudgetViewEntity, newItem: BudgetViewEntity): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: BudgetViewEntity, newItem: BudgetViewEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun getChangePayload(oldItem: BudgetViewEntity, newItem: BudgetViewEntity): Any? {
            return PayloadsHolder<BudgetViewEntity>().apply {

                val itemPair = oldItem to newItem

                addPayloadIfNotEqual(itemPair, BudgetViewEntity::name, PayloadsType.NAME)
                addPayloadIfNotEqual(itemPair, BudgetViewEntity::totalBalance, PayloadsType.TOTAL_BALANCE)
                addPayloadIfNotEqual(itemPair, BudgetViewEntity::totalSpendingEstimate, PayloadsType.TOTAL_SPENDING_ESTIMATE)
                addPayloadIfNotEqual(itemPair, BudgetViewEntity::totalSavings, PayloadsType.TOTAL_SAVINGS)


/*
                if (oldItem.name != newItem.name)
                    add(PayloadsType.NAME)

                if (oldItem.totalBalance != newItem.totalBalance)
                    add(PayloadsType.TOTAL_BALANCE)

                if (oldItem.totalSpendingEstimate != newItem.totalSpendingEstimate)
                    add(PayloadsType.TOTAL_SPENDING_ESTIMATE)

                if (oldItem.totalSavings != newItem.totalSavings)
                    add(PayloadsType.TOTAL_SAVINGS)
*/

            }
        }
    }

    object PayloadsType {
        const val NAME = 0
        const val TOTAL_BALANCE = 1
        const val TOTAL_SPENDING_ESTIMATE = 2
        const val TOTAL_SAVINGS = 3
    }

}