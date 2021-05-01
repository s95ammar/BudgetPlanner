package com.s95ammar.budgetplanner.ui.common.viewbinding

class ViewBindingException(message: String? = MESSAGE_INAPPROPRIATE_LIFECYCLE_STATE) : IllegalStateException(message) {
    companion object {
        const val MESSAGE_INAPPROPRIATE_LIFECYCLE_STATE = "binding is not initialized or" +
                " is accessed from outside of the fragment's view lifecycle"
    }
}