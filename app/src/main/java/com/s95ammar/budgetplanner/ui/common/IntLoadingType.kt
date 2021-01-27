package com.s95ammar.budgetplanner.ui.common

import androidx.annotation.IntDef

@IntDef(
    IntLoadingType.MAIN,
    IntLoadingType.SWIPE_TO_REFRESH,
)
@Retention(AnnotationRetention.SOURCE)
annotation class IntLoadingType {
    companion object {
        const val MAIN = 0
        const val SWIPE_TO_REFRESH = 1
    }
}
