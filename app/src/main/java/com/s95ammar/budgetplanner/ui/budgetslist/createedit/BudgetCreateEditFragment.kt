package com.s95ammar.budgetplanner.ui.budgetslist.createedit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.s95ammar.budgetplanner.R
import com.s95ammar.budgetplanner.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.budget_create_edit_fragment.*

@AndroidEntryPoint
class BudgetCreateEditFragment : BaseFragment() {

    companion object {
        fun newInstance() = BudgetCreateEditFragment()
    }

    private val viewModel: BudgetCreateEditViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.budget_create_edit_fragment, container, false)
    }

    override fun setUpViews() {
        super.setUpViews()
        toolbar_budgets_create_edit.setNavigationOnClickListener { onBackPressed() }
    }
}