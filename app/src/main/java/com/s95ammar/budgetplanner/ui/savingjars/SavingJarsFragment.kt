package com.s95ammar.budgetplanner.ui.savingjars

import androidx.fragment.app.viewModels
import com.s95ammar.budgetplanner.R
import com.s95ammar.budgetplanner.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SavingJarsFragment : BaseFragment(R.layout.fragment_saving_jars) {

    private val viewModel: SavingJarsViewModel by viewModels()

}