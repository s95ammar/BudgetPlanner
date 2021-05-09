package com.s95ammar.budgetplanner.ui.common.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.core.os.bundleOf
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.s95ammar.budgetplanner.R
import com.s95ammar.budgetplanner.ui.common.Keys

class EditDeleteBottomSheetDialogFragment : BottomSheetDialogFragment() {

    companion object {
        const val TAG = "EditDeleteBottomSheetDialogFragment"

        fun newInstance(title: String, @DrawableRes iconRes: Int = 0) = EditDeleteBottomSheetDialogFragment().apply {
            arguments = bundleOf(
                Keys.KEY_NAME to title,
                Keys.KEY_DRAWABLE_RES to iconRes
            )
        }
    }

    interface Listener {
        fun onEdit()
        fun onDelete()
    }

    var listener: Listener? = null

    private val argName
        get() = requireArguments().getString(Keys.KEY_NAME)
    private val argDrawableRes
        get() = requireArguments().getInt(Keys.KEY_DRAWABLE_RES)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_edit_delete_bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<TextView>(R.id.name_text_view).apply {
            text = argName
            setCompoundDrawablesWithIntrinsicBounds(argDrawableRes, 0, 0, 0)
        }
        view.findViewById<TextView>(R.id.edit_text_view).setOnClickListener { listener?.onEdit(); dismiss() }
        view.findViewById<TextView>(R.id.delete_text_view).setOnClickListener { listener?.onDelete(); dismiss() }
    }

}