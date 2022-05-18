package com.s95ammar.budgetplanner.ui.appscreens.savingjars

import androidx.fragment.app.viewModels
import com.s95ammar.budgetplanner.R
import com.s95ammar.budgetplanner.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SavingsJarsFragment : BaseFragment(R.layout.fragment_savings_jars) {

    private val viewModel: SavingsJarsViewModel by viewModels()

}