package com.s95ammar.budgetplanner.ui.common

import com.s95ammar.budgetplanner.util.INVALID


enum class CreateEditMode {
    CREATE,
    EDIT;

    companion object {
        fun getById(id: Int): CreateEditMode {
            return if (id == Int.INVALID) CREATE else EDIT
        }
    }

}