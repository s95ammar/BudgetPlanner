package com.s95ammar.budgetplanner.ui.common.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.s95ammar.budgetplanner.R
import com.s95ammar.budgetplanner.ui.common.Keys
import kotlinx.android.synthetic.main.dialog_edit_delete_bottom_sheet.*

class EditDeleteBottomSheetDialogFragment : BottomSheetDialogFragment() {

    companion object {
        const val TAG = "EditDeleteBottomSheetDialogFragment"

        fun newInstance(title: String, @DrawableRes iconRes: Int = 0) = EditDeleteBottomSheetDialogFragment().apply {
            arguments = bundleOf(
                Keys.KEY_TITLE to title,
                Keys.KEY_ICON_RES to iconRes
            )
        }
    }

    interface Listener {
        fun onEdit()
        fun onDelete()
    }

    var listener: Listener? = null

    private val argTitle
        get() = requireArguments().getString(Keys.KEY_TITLE)
    private val argIconRes
        get() = requireArguments().getInt(Keys.KEY_ICON_RES)

   override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_edit_delete_bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViews()
    }

    private fun setUpViews() {
        text_view_title_edit_delete_bottom_sheet.text = argTitle
        text_view_title_edit_delete_bottom_sheet.setCompoundDrawablesWithIntrinsicBounds(argIconRes, 0, 0, 0)
        text_view_edit_edit_delete_bottom_sheet.setOnClickListener { listener?.onEdit(); dismiss() }
        text_view_delete_edit_delete_bottom_sheet.setOnClickListener { listener?.onDelete(); dismiss() }
    }

}