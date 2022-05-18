package com.s95ammar.budgetplanner.ui.appscreens.currencyprompt

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import com.s95ammar.budgetplanner.R
import com.s95ammar.budgetplanner.databinding.FragmentCurrencySelectionBinding
import com.s95ammar.budgetplanner.ui.common.viewbinding.BaseViewBinderFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CurrencySelectionFragment
    : BaseViewBinderFragment<FragmentCurrencySelectionBinding>(
    R.layout.fragment_currency_selection
) {

    private val viewModel: CurrencySelectionViewModel by viewModels()
    private val onBackPressedCallback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            activityViewModel.onCurrencySelectionBack()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
    }

    override fun initViewBinding(view: View): FragmentCurrencySelectionBinding {
        return FragmentCurrencySelectionBinding.bind(view)
    }

}