package com.s95ammar.budgetplanner.ui.appscreens.budgetslist.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.s95ammar.budgetplanner.R
import com.s95ammar.budgetplanner.ui.common.Keys
import kotlinx.android.synthetic.main.dialog_bottom_sheet_budget_list.*

class BudgetListItemBottomSheetDialogFragment : BottomSheetDialogFragment() {

    companion object {
        const val TAG = "TAG_BUDGET_LIST_ITEM_BOTTOM_SHEET"

        fun newInstance(budgetTitle: String, isActive: Boolean) = BudgetListItemBottomSheetDialogFragment().apply {
            arguments = bundleOf(
                Keys.KEY_BUDGET_TITLE to budgetTitle,
                Keys.KEY_BUDGET_IS_ACTIVE to isActive
            )
        }
    }

    interface Listener {
        fun onMakeActive()
        fun onEdit()
        fun onDelete()
    }

    var listener: Listener? = null

    private val argBudgetTitle
        get() = requireArguments().getString(Keys.KEY_BUDGET_TITLE)
    private val argBudgetIsActive
        get() = requireArguments().getBoolean(Keys.KEY_BUDGET_IS_ACTIVE)

   override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_bottom_sheet_budget_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViews()
    }

    private fun setUpViews() {
        text_view_title_budget_bottom_sheet.text = argBudgetTitle
        text_view_make_active_budget_bottom_sheet.isVisible = !argBudgetIsActive
        text_view_make_active_budget_bottom_sheet.setOnClickListener { listener?.onMakeActive(); dismiss() }
        text_view_edit_budget_bottom_sheet.setOnClickListener { listener?.onEdit(); dismiss() }
        text_view_delete_budget_bottom_sheet.setOnClickListener { listener?.onDelete(); dismiss() }
    }

}