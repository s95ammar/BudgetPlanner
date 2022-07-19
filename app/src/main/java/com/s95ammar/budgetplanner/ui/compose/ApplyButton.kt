package com.s95ammar.budgetplanner.ui.compose

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

val DefaultApplyButtonContentPadding = 4.dp

@Composable
fun ApplyButton(
    modifier: Modifier,
    text: String,
    onClick: () -> Unit
) {
    Button(
        modifier = modifier,
        onClick = onClick,
        contentPadding = PaddingValues(DefaultApplyButtonContentPadding)
    ) {
        Text(
            text = text.uppercase(),
            style = MaterialTheme.typography.h4
        )
    }
}
