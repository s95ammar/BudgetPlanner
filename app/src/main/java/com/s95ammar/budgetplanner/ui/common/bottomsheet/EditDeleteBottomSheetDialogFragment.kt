package com.s95ammar.budgetplanner.ui.common.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.core.os.bundleOf
import com.s95ammar.budgetplanner.R
import com.s95ammar.budgetplanner.databinding.DialogEditDeleteBottomSheetBinding
import com.s95ammar.budgetplanner.ui.base.BaseBottomSheetDialogFragment
import com.s95ammar.budgetplanner.ui.common.Keys
import com.s95ammar.budgetplanner.ui.common.viewbinding.ViewBinder

class EditDeleteBottomSheetDialogFragment : BaseBottomSheetDialogFragment(), ViewBinder<DialogEditDeleteBottomSheetBinding> {

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

    override val binding: DialogEditDeleteBottomSheetBinding
        get() = getBinding()

    override fun initViewBinding(view: View): DialogEditDeleteBottomSheetBinding {
        return DialogEditDeleteBottomSheetBinding.bind(view)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return binding.root
    }

    override fun setUpViews() {
        binding.textViewTitleEditDeleteBottomSheet.text = argTitle
        binding.textViewTitleEditDeleteBottomSheet.setCompoundDrawablesWithIntrinsicBounds(argIconRes, 0, 0, 0)
        binding.textViewEditEditDeleteBottomSheet.setOnClickListener { listener?.onEdit(); dismiss() }
        binding.textViewDeleteEditDeleteBottomSheet.setOnClickListener { listener?.onDelete(); dismiss() }
    }

}