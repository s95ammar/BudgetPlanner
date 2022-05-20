package com.s95ammar.budgetplanner.ui.appscreens.currencyselection.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import com.s95ammar.budgetplanner.R
import com.s95ammar.budgetplanner.databinding.ItemCurrencySelectionBinding
import com.s95ammar.budgetplanner.databinding.ItemLoadMoreBinding
import com.s95ammar.budgetplanner.databinding.LayoutEmptyBinding
import com.s95ammar.budgetplanner.ui.base.BaseListAdapter
import com.s95ammar.budgetplanner.ui.common.LoadingState
import com.s95ammar.budgetplanner.ui.common.data.Selectable
import com.s95ammar.budgetplanner.ui.main.data.Currency

class CurrencySelectionAdapter(
    private val onCurrencyClick: (Currency) -> Unit,
    private val onLoadMoreCurrencies: () -> Unit
) : BaseListAdapter<CurrencySelectionItemType, CurrencySelectionAdapter.ViewHolder>(DiffUtilCallback()) {

    companion object {

        class DiffUtilCallback : DiffUtil.ItemCallback<CurrencySelectionItemType>() {

            override fun areItemsTheSame(oldItem: CurrencySelectionItemType, newItem: CurrencySelectionItemType): Boolean {
                return when (oldItem) {
                    is CurrencySelectionItemType.ListItem -> oldItem.selectableCurrency.value.code ==
                            (newItem as? CurrencySelectionItemType.ListItem)?.selectableCurrency?.value?.code

                    is CurrencySelectionItemType.LoadMore -> newItem is CurrencySelectionItemType.LoadMore
                }

            }

            override fun areContentsTheSame(oldItem: CurrencySelectionItemType, newItem: CurrencySelectionItemType): Boolean {
                return oldItem == newItem
            }

            override fun getChangePayload(oldItem: CurrencySelectionItemType, newItem: CurrencySelectionItemType): PayloadsHolder {
                return PayloadsHolder().apply {
                    when (oldItem) {
                        is CurrencySelectionItemType.ListItem -> {
                            if (newItem is CurrencySelectionItemType.ListItem) {
                                addPayloadIfNotEqual(
                                    PayloadType.FULL_NAME,
                                    oldItem.selectableCurrency.value to newItem.selectableCurrency.value,
                                    Currency::code,
                                    Currency::name
                                )
                                addPayloadIfNotEqual(
                                    PayloadType.SELECTION,
                                    oldItem.selectableCurrency to newItem.selectableCurrency,
                                    Selectable<Currency>::isSelected
                                )
                                addPayloadIfNotEqual(
                                    PayloadType.LOADING_STATE,
                                    oldItem.selectableCurrency to newItem.selectableCurrency,
                                    Selectable<Currency>::isSelected
                                )
                            }
                        }
                        is CurrencySelectionItemType.LoadMore -> {}
                    }
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is CurrencySelectionItemType.ListItem -> R.layout.item_currency_selection
            is CurrencySelectionItemType.LoadMore -> R.layout.item_load_more
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            R.layout.item_currency_selection -> CurrencySelectionViewHolder(
                binding = ItemCurrencySelectionBinding.inflate(LayoutInflater.from(parent.context), parent, false),
                onCurrencyClick = onCurrencyClick
            )
            R.layout.item_load_more -> LoadMoreViewHolder(
                binding = ItemLoadMoreBinding.inflate(LayoutInflater.from(parent.context), parent, false),
                onLoadMoreCurrencies = onLoadMoreCurrencies
            )
            else -> EmptyViewHolder(
                binding = LayoutEmptyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
        }
    }

    sealed class ViewHolder(view: View) : BaseViewHolder<CurrencySelectionItemType>(view)

    class EmptyViewHolder(binding: LayoutEmptyBinding) : ViewHolder(binding.root)

    class CurrencySelectionViewHolder(
        private val binding: ItemCurrencySelectionBinding,
        private val onCurrencyClick: (Currency) -> Unit
    ) : ViewHolder(binding.root) {

        override fun bind(item: CurrencySelectionItemType, payloads: PayloadsHolder) {
            val selectableCurrency = (item as CurrencySelectionItemType.ListItem).selectableCurrency
            binding.root.setOnClickListener { onCurrencyClick(selectableCurrency.value) }
            binding.nameTextView.text = itemView.context.getString(
                R.string.format_currency_code_and_name,
                selectableCurrency.value.code,
                selectableCurrency.value.name
            )
            binding.selectedImageView.isVisible = selectableCurrency.isSelected
        }
    }

    class LoadMoreViewHolder(
        private val binding: ItemLoadMoreBinding,
        private val onLoadMoreCurrencies: () -> Unit
        ) : ViewHolder(binding.root) {

        override fun bind(item: CurrencySelectionItemType, payloads: PayloadsHolder) {
            val loadingState = (item as CurrencySelectionItemType.LoadMore).loadingState
            binding.loadMoreButton.setOnClickListener { onLoadMoreCurrencies() }
            binding.loadMoreButton.isVisible = loadingState is LoadingState.Cold || loadingState is LoadingState.Error
            binding.progressBar.isVisible = loadingState is LoadingState.Loading
        }
    }

    object PayloadType {
        const val FULL_NAME = 1
        const val SELECTION = 2
        const val LOADING_STATE = 3
    }

}