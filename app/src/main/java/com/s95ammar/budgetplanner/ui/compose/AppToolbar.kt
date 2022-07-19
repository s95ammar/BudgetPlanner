package com.s95ammar.budgetplanner.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.s95ammar.budgetplanner.ui.compose.values.AppTheme

@Composable
fun AppToolbar(
    title: String,
    modifier: Modifier = Modifier,
    onBackClick: (() -> Unit)? = null
) {
    TopAppBar(
        modifier = modifier,
        title = { Text(
            text = title,
            style = MaterialTheme.typography.h3
        ) },
        navigationIcon = onBackClick?.let { { BackNavigationIconButton(it) } },
    )
}

@Composable
fun BackNavigationIconButton(
    onClick: () -> Unit
) {
    IconButton(onClick = onClick) {
        Icon(Icons.Filled.ArrowBack, contentDescription = "back"/*TODO-COMPOSE*/)
    }
}

@Composable
@Preview
fun Preview() {
    AppTheme {
        Box(
            Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            AppToolbar(
                title = "App screen",
                onBackClick = {}
            )
        }
    }
}
