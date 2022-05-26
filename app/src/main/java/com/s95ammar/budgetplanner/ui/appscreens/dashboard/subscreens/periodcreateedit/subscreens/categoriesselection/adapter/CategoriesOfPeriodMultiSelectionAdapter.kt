package com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.periodcreateedit.subscreens.categoriesselection.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import com.google.android.material.snackbar.Snackbar
import com.s95ammar.budgetplanner.R
import com.s95ammar.budgetplanner.databinding.ItemCategoryOfPeriodSelectionBinding
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.common.data.CategoryOfPeriod
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.periodcreateedit.subscreens.categoriesselection.adapter.CategoriesOfPeriodMultiSelectionAdapter.CategoriesOfPeriodSelectionViewHolder
import com.s95ammar.budgetplanner.ui.base.BaseListAdapter
import com.s95ammar.budgetplanner.util.getAmountStringFormatted
import com.s95ammar.budgetplanner.util.setDrawableTint
import com.s95ammar.budgetplanner.util.setSelectableItemBackground
import kotlin.math.absoluteValue

class CategoriesOfPeriodMultiSelectionAdapter(
    private val onSelectionStateChanged: (CategoryOfPeriod, Boolean) -> Unit,
    private val onCreateEditEstimate: (CategoryOfPeriod) -> Unit,
    private val onChangeCurrency: (CategoryOfPeriod) -> Unit
) : BaseListAdapter<CategoryOfPeriod, CategoriesOfPeriodSelectionViewHolder>(DiffUtilCallback()) {

    var allowCategorySelectionForAll = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriesOfPeriodSelectionViewHolder {
        return CategoriesOfPeriodSelectionViewHolder(
            onSelectionStateChanged = onSelectionStateChanged,
            onCreateEditEstimate = onCreateEditEstimate,
            onChangeCurrency = onChangeCurrency,
            binding = ItemCategoryOfPeriodSelectionBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            alwaysAllowCategorySelection = allowCategorySelectionForAll
        )
    }

    class CategoriesOfPeriodSelectionViewHolder(
        private val onSelectionStateChanged: (CategoryOfPeriod, Boolean) -> Unit,
        private val onCreateEditEstimate: (CategoryOfPeriod) -> Unit,
        private val onChangeCurrency: (CategoryOfPeriod) -> Unit,
        private val binding: ItemCategoryOfPeriodSelectionBinding,
        private val alwaysAllowCategorySelection: Boolean
    ) : BaseListAdapter.BaseViewHolder<CategoryOfPeriod>(binding.root) {


        override fun bind(item: CategoryOfPeriod, payloads: PayloadsHolder) {
            binding.checkBoxCategoryName.setOnCheckedChangeListener { _, isChecked -> onSelectionStateChanged(item, isChecked) }
            binding.addEstimatesTextView.setOnClickListener { onCreateEditEstimate(item) }
            binding.currencyTextView.setOnClickListener { onChangeCurrency(item) }

            if (payloads.shouldUpdate(PayloadType.NAME)) setName(item.categoryName)
            if (payloads.shouldUpdate(PayloadType.CURRENCY)) setCurrencyView(item)
            if (payloads.shouldUpdate(PayloadType.ESTIMATE)) setEstimate(item.estimate)
            if (payloads.shouldUpdate(PayloadType.SELECTION))
                setSelection(item.isSelected, alwaysAllowCategorySelection || item.budgetTransactionsAmountSum == 0.0)
        }

        private fun setName(categoryName: String) {
            binding.checkBoxCategoryName.text = categoryName
        }

        private fun setCurrencyView(item: CategoryOfPeriod) {
            val isEnabled = item.budgetTransactionsAmountSum == 0.0

            binding.currencyTextView.text = getString(R.string.format_currency, item.currencyCode)
            binding.currencyTextView.setSelectableItemBackground(isEnabled)

            if (isEnabled) {
                binding.currencyTextView.setTextColor(getColor(R.color.colorBlack))
                binding.currencyTextView.setDrawableTint(getColor(R.color.colorDarkGray))
                binding.currencyTextView.setOnClickListener { onChangeCurrency(item) }
            } else {
                binding.currencyTextView.setTextColor(getColor(R.color.colorLightGray))
                binding.currencyTextView.setDrawableTint(getColor(R.color.colorLightGray))
                binding.currencyTextView.setOnClickListener {
                    Snackbar.make(itemView, R.string.currency_not_changeable, Snackbar.LENGTH_LONG).show()
                }
            }
        }

        private fun setEstimate(estimate: Double?) {
            if (estimate != null && estimate != 0.0) {
                binding.addEstimatesTextView.text = getString(
                    if (estimate < 0) R.string.format_estimate_expenses else R.string.format_estimate_income,
                    getAmountStringFormatted(estimate.absoluteValue, includePlusSign = false)
                )
                binding.addEstimatesTextView.setTextColor(getColor(R.color.colorBlack))
            } else {
                binding.addEstimatesTextView.text = getString(R.string.add_estimate_optional)
                binding.addEstimatesTextView.setTextColor(getColor(R.color.colorDarkGray))
            }
        }

        private fun setSelection(isChecked: Boolean, isEnabled: Boolean) {
            binding.checkBoxCategoryName.isChecked = isChecked
            binding.checkBoxCategoryName.isEnabled = isEnabled
            binding.selectionDisabledView.isGone = isEnabled

            binding.selectionDisabledView.setOnClickListener {
                Snackbar.make(itemView, R.string.category_not_selectable, Snackbar.LENGTH_LONG).show()
            }

            binding.addEstimatesTextView.isVisible = isChecked
            binding.currencyTextView.isVisible = isChecked
        }
    }

    class DiffUtilCallback : DiffUtil.ItemCallback<CategoryOfPeriod>() {
        override fun areItemsTheSame(oldItem: CategoryOfPeriod, newItem: CategoryOfPeriod): Boolean {
            return oldItem.categoryId == newItem.categoryId
        }

        override fun areContentsTheSame(oldItem: CategoryOfPeriod, newItem: CategoryOfPeriod): Boolean {
            return oldItem == newItem
        }

        override fun getChangePayload(oldItem: CategoryOfPeriod, newItem: CategoryOfPeriod): PayloadsHolder {
            return PayloadsHolder().apply {
                addPayloadIfNotEqual(PayloadType.NAME, oldItem to newItem, CategoryOfPeriod::categoryName)
                addPayloadIfNotEqual(
                    PayloadType.CURRENCY,
                    oldItem to newItem,
                    CategoryOfPeriod::currencyCode,
                    CategoryOfPeriod::budgetTransactionsAmountSum
                )
                addPayloadIfNotEqual(PayloadType.ESTIMATE, oldItem to newItem, CategoryOfPeriod::estimate)
                addPayloadIfNotEqual(
                    PayloadType.SELECTION,
                    oldItem to newItem,
                    CategoryOfPeriod::isSelected,
                    CategoryOfPeriod::budgetTransactionsAmountSum
                )
            }
        }
    }

    object PayloadType {
        const val NAME = 1
        const val CURRENCY = 2
        const val ESTIMATE = 3
        const val SELECTION = 4
    }
}