package com.s95ammar.budgetplanner.ui.compose.values

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun AppTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        typography = AppTypography,
        content = content
    )
}
