package com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.periodcreateedit.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.DiffUtil
import com.s95ammar.budgetplanner.databinding.ItemPeriodicCategorySelectionBinding
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.common.data.PeriodicCategoryViewEntity
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.periodcreateedit.adapter.PeriodicCategoriesSelectionAdapter.PeriodicCategoriesSelectionViewHolder
import com.s95ammar.budgetplanner.ui.base.BaseListAdapter
import com.s95ammar.budgetplanner.util.inputText

class PeriodicCategoriesSelectionAdapter(
    private val onClick: (Int, Boolean) -> Unit,
    private val onMaxInputChanged: (Int, String?) -> Unit
) : BaseListAdapter<PeriodicCategoryViewEntity, PeriodicCategoriesSelectionViewHolder>(DiffUtilCallback()) {

    var alwaysAllowCategorySelection = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PeriodicCategoriesSelectionViewHolder {
        return PeriodicCategoriesSelectionViewHolder(
            onSelectionStateChanged = onClick,
            onMaxInputChanged = onMaxInputChanged,
            binding = ItemPeriodicCategorySelectionBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            alwaysAllowCategorySelection = alwaysAllowCategorySelection
        )
    }

    class PeriodicCategoriesSelectionViewHolder(
        private val onSelectionStateChanged: (Int, Boolean) -> Unit,
        private val onMaxInputChanged: (Int, String?) -> Unit,
        private val binding: ItemPeriodicCategorySelectionBinding,
        private val alwaysAllowCategorySelection: Boolean
    ) : BaseListAdapter.BaseViewHolder<PeriodicCategoryViewEntity>(binding.root) {


        override fun bind(item: PeriodicCategoryViewEntity, payloads: PayloadsHolder) {
            binding.checkBoxCategoryName.setOnCheckedChangeListener { _, isChecked -> onSelectionStateChanged(adapterPosition, isChecked) }
            binding.inputLayoutMax.editText?.doAfterTextChanged { onMaxInputChanged(adapterPosition, it?.toString()) }

            if (payloads.shouldUpdate(PayloadType.NAME)) setName(item.categoryName)
            if (payloads.shouldUpdate(PayloadType.MAX)) setMax(item.max)
            if (payloads.shouldUpdate(PayloadType.SELECTION)) setSelection(item.isSelected, alwaysAllowCategorySelection || item.amount == 0)
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

    class DiffUtilCallback : DiffUtil.ItemCallback<PeriodicCategoryViewEntity>() {
        override fun areItemsTheSame(oldItem: PeriodicCategoryViewEntity, newItem: PeriodicCategoryViewEntity): Boolean {
            return oldItem.categoryId == newItem.categoryId
        }

        override fun areContentsTheSame(oldItem: PeriodicCategoryViewEntity, newItem: PeriodicCategoryViewEntity): Boolean {
            return oldItem == newItem
        }

        override fun getChangePayload(oldItem: PeriodicCategoryViewEntity, newItem: PeriodicCategoryViewEntity): PayloadsHolder {
            return PayloadsHolder().apply {
                addPayloadIfNotEqual(PayloadType.NAME, oldItem to newItem, PeriodicCategoryViewEntity::categoryName)
                addPayloadIfNotEqual(PayloadType.MAX, oldItem to newItem, PeriodicCategoryViewEntity::max)
                addPayloadIfNotEqual(
                    PayloadType.SELECTION,
                    oldItem to newItem,
                    PeriodicCategoryViewEntity::isSelected,
                    PeriodicCategoryViewEntity::amount
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