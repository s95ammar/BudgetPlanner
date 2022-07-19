package com.s95ammar.budgetplanner.ui.appscreens.categories.subscreens.createedit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import com.s95ammar.budgetplanner.ui.appscreens.categories.subscreens.createedit.compose.CategoryCreateEditScreen
import com.s95ammar.budgetplanner.ui.base.BaseFragment
import com.s95ammar.budgetplanner.ui.compose.values.AppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CategoryCreateEditFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return ComposeView(requireContext()).apply {
            setContent {
                AppTheme {
                    CategoryCreateEditScreen(
                        navController,
                        activityViewModel
                    )
                }
            }
        }
    }
}
