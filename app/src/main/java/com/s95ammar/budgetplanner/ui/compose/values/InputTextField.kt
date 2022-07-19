package com.s95ammar.budgetplanner.ui.compose.values

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun InputTextField(
    modifier: Modifier,
    hint: String,
    error: String? = null,
    value: String,
    onValueChange: (String) -> Unit
) {
    Column(modifier = modifier) {
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White),
            label = { Text(text = hint) },
            value = value,
            onValueChange = { onValueChange(it) },
            colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.White),
            isError = error != null
        )
        AnimatedVisibility(visible = error != null) {
            Text(
                modifier = Modifier.padding(start = 16.dp),
                text = error.orEmpty(),
                color = MaterialTheme.colors.error
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun Preview() {
    AppTheme {
        InputTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            hint = "Hint",
            value = "This is input text field",
            error = "Invalid value",
            onValueChange = {}
        )
    }
}