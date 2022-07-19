package com.s95ammar.budgetplanner.ui.compose.values

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp
import com.s95ammar.budgetplanner.R

val Cairo = FontFamily(
    Font(R.font.cairo_regular)
)

val AppTypography = Typography(
    defaultFontFamily = Cairo,
    h1 = TextStyle(fontSize = 24.sp),
    h2 = TextStyle(fontSize = 22.sp),
    h3 = TextStyle(fontSize = 20.sp),
    h4 = TextStyle(fontSize = 18.sp),
    h5 = TextStyle(fontSize = 16.sp),
    h6 = TextStyle(fontSize = 14.sp),
)
