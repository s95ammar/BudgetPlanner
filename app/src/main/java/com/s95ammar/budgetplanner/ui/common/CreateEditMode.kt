package com.s95ammar.budgetplanner.ui.common

import com.s95ammar.budgetplanner.util.NO_ITEM


enum class CreateEditMode {
    CREATE,
    EDIT;

    companion object {
        fun getById(id: Int): CreateEditMode {
            return if (id == Int.NO_ITEM) CREATE else EDIT
        }
    }

}