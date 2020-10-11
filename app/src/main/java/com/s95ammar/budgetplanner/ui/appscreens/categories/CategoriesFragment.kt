package com.s95ammar.budgetplanner.ui.appscreens.categories

import androidx.fragment.app.viewModels
import com.s95ammar.budgetplanner.R
import com.s95ammar.budgetplanner.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CategoriesFragment : BaseFragment(R.layout.fragment_categories) {

    private val viewModel: CategoriesViewModel by viewModels()

}