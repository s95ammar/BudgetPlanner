package com.s95ammar.budgetplanner.ui.common


enum class CreateEditMode {
    CREATE,
    EDIT;

    companion object {
        fun getById(id: Int): CreateEditMode {
            return if (id == Constants.NO_ITEM) CREATE else EDIT
        }
    }

    fun isEdit() = this == EDIT
}