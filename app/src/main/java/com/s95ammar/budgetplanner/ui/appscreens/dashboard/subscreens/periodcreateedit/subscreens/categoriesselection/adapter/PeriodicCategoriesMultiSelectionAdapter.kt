package com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.periodcreateedit.subscreens.categoriesselection.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.DiffUtil
import com.google.android.material.snackbar.Snackbar
import com.s95ammar.budgetplanner.R
import com.s95ammar.budgetplanner.databinding.ItemPeriodicCategorySelectionBinding
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.common.data.PeriodicCategory
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.periodcreateedit.subscreens.categoriesselection.adapter.PeriodicCategoriesMultiSelectionAdapter.PeriodicCategoriesSelectionViewHolder
import com.s95ammar.budgetplanner.ui.base.BaseListAdapter
import com.s95ammar.budgetplanner.util.text

class PeriodicCategoriesMultiSelectionAdapter(
    private val onClick: (Int, Boolean) -> Unit,
    private val onMaxInputChanged: (Int, String?) -> Unit
) : BaseListAdapter<PeriodicCategory, PeriodicCategoriesSelectionViewHolder>(DiffUtilCallback()) {

    var allowCategorySelectionForAll = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PeriodicCategoriesSelectionViewHolder {
        return PeriodicCategoriesSelectionViewHolder(
            onSelectionStateChanged = onClick,
            onMaxInputChanged = onMaxInputChanged,
            binding = ItemPeriodicCategorySelectionBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            alwaysAllowCategorySelection = allowCategorySelectionForAll
        )
    }

    class PeriodicCategoriesSelectionViewHolder(
        private val onSelectionStateChanged: (Int, Boolean) -> Unit,
        private val onMaxInputChanged: (Int, String?) -> Unit,
        private val binding: ItemPeriodicCategorySelectionBinding,
        private val alwaysAllowCategorySelection: Boolean
    ) : BaseListAdapter.BaseViewHolder<PeriodicCategory>(binding.root) {


        override fun bind(item: PeriodicCategory, payloads: PayloadsHolder) {
            binding.checkBoxCategoryName.setOnCheckedChangeListener { _, isChecked -> onSelectionStateChanged(adapterPosition, isChecked) }
            binding.inputLayoutMax.editText?.doAfterTextChanged { onMaxInputChanged(adapterPosition, it?.toString()) }

            if (payloads.shouldUpdate(PayloadType.NAME)) setName(item.categoryName)
            if (payloads.shouldUpdate(PayloadType.MAX)) setMax(item.max)
            if (payloads.shouldUpdate(PayloadType.SELECTION)) setSelection(item.isSelected, alwaysAllowCategorySelection || item.budgetTransactionsAmountSum == 0)
        }

        private fun setName(categoryName: String) {
            binding.checkBoxCategoryName.text = categoryName
        }

        private fun setMax(max: Int?) {
            binding.inputLayoutMax.text = max?.toString()
        }

        private fun setSelection(isChecked: Boolean, isEnabled: Boolean) {
            binding.checkBoxCategoryName.isChecked = isChecked
            binding.checkBoxCategoryName.isEnabled = isEnabled
            binding.viewDisabled.isGone = isEnabled

            binding.viewDisabled.setOnClickListener {
                Snackbar.make(itemView, R.string.category_unselectable, Snackbar.LENGTH_LONG).show()
            }

            binding.inputLayoutMax.isVisible = isChecked
        }
    }

    class DiffUtilCallback : DiffUtil.ItemCallback<PeriodicCategory>() {
        override fun areItemsTheSame(oldItem: PeriodicCategory, newItem: PeriodicCategory): Boolean {
            return oldItem.categoryId == newItem.categoryId
        }

        override fun areContentsTheSame(oldItem: PeriodicCategory, newItem: PeriodicCategory): Boolean {
            return oldItem == newItem
        }

        override fun getChangePayload(oldItem: PeriodicCategory, newItem: PeriodicCategory): PayloadsHolder {
            return PayloadsHolder().apply {
                addPayloadIfNotEqual(PayloadType.NAME, oldItem to newItem, PeriodicCategory::categoryName)
                addPayloadIfNotEqual(PayloadType.MAX, oldItem to newItem, PeriodicCategory::max)
                addPayloadIfNotEqual(
                    PayloadType.SELECTION,
                    oldItem to newItem,
                    PeriodicCategory::isSelected,
                    PeriodicCategory::budgetTransactionsAmountSum
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