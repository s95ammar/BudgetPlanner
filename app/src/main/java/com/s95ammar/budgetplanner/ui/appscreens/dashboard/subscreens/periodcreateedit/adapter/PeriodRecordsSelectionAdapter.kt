package com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.periodcreateedit.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.DiffUtil
import com.s95ammar.budgetplanner.databinding.ItemPeriodRecordSelectionBinding
import com.s95ammar.budgetplanner.models.view.PeriodRecordViewEntity
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.periodcreateedit.adapter.PeriodRecordsSelectionAdapter.PeriodRecordsSelectionViewHolder
import com.s95ammar.budgetplanner.ui.base.BaseListAdapter
import com.s95ammar.budgetplanner.util.inputText

class PeriodRecordsSelectionAdapter(
    private val onClick: (Int, Boolean) -> Unit,
    private val onMaxInputChanged: (Int, String?) -> Unit
) : BaseListAdapter<PeriodRecordViewEntity, PeriodRecordsSelectionViewHolder>(DiffUtilCallback()) {

    var isInsertionTemplate = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PeriodRecordsSelectionViewHolder {
        return PeriodRecordsSelectionViewHolder(
            onSelectionStateChanged = onClick,
            onMaxInputChanged = onMaxInputChanged,
            binding = ItemPeriodRecordSelectionBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            isInsertionTemplate = isInsertionTemplate
        )
    }

    class PeriodRecordsSelectionViewHolder(
        private val onSelectionStateChanged: (Int, Boolean) -> Unit,
        private val onMaxInputChanged: (Int, String?) -> Unit,
        private val binding: ItemPeriodRecordSelectionBinding,
        private val isInsertionTemplate: Boolean
    ) : BaseListAdapter.BaseViewHolder<PeriodRecordViewEntity>(binding.root) {


        override fun bind(item: PeriodRecordViewEntity, payloads: PayloadsHolder) {
            binding.checkBoxCategoryName.setOnCheckedChangeListener { _, isChecked -> onSelectionStateChanged(adapterPosition, isChecked) }
            binding.inputLayoutMax.editText?.doAfterTextChanged { onMaxInputChanged(adapterPosition, it?.toString()) }

            if (payloads.shouldUpdate(PayloadType.NAME)) setName(item.categoryName)
            if (payloads.shouldUpdate(PayloadType.MAX)) setMax(item.max)
            if (payloads.shouldUpdate(PayloadType.SELECTION)) setSelection(item.isSelected, item.amount == 0 || isInsertionTemplate)
        }

        private fun setName(categoryName: String) {
            binding.checkBoxCategoryName.text = categoryName
        }

        private fun setMax(max: Int?) {
            binding.inputLayoutMax.inputText = max?.toString()
        }

        private fun setSelection(isChecked: Boolean, isEnabled: Boolean) {
            binding.checkBoxCategoryName.isChecked = isChecked
            binding.checkBoxCategoryName.isEnabled = isEnabled
            binding.viewDisabled.isGone = isEnabled
            binding.viewDisabled.setOnClickListener {

                Toast.makeText(
                    itemView.context,
                    "This category already has budget transactions created during this period",
                    Toast.LENGTH_SHORT
                ).show()
            }
            binding.inputLayoutMax.isVisible = isChecked
        }
    }

    class DiffUtilCallback : DiffUtil.ItemCallback<PeriodRecordViewEntity>() {
        override fun areItemsTheSame(oldItem: PeriodRecordViewEntity, newItem: PeriodRecordViewEntity): Boolean {
            return oldItem.categoryId == newItem.categoryId
        }

        override fun areContentsTheSame(oldItem: PeriodRecordViewEntity, newItem: PeriodRecordViewEntity): Boolean {
            return oldItem == newItem
        }

        override fun getChangePayload(oldItem: PeriodRecordViewEntity, newItem: PeriodRecordViewEntity): PayloadsHolder {
            return PayloadsHolder().apply {
                addPayloadIfNotEqual(PayloadType.NAME, oldItem to newItem, PeriodRecordViewEntity::categoryName)
                addPayloadIfNotEqual(PayloadType.MAX, oldItem to newItem, PeriodRecordViewEntity::max)
                addPayloadIfNotEqual(
                    PayloadType.SELECTION,
                    oldItem to newItem,
                    PeriodRecordViewEntity::isSelected,
                    PeriodRecordViewEntity::amount
                )
            }
        }
    }

    object PayloadType {
        const val NAME = 1
        const val MAX = 2
        const val SELECTION = 3
    }
}