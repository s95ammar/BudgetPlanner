package com.s95ammar.budgetplanner.ui.appscreens.categories.subscreens.createedit.compose

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.s95ammar.budgetplanner.R
import com.s95ammar.budgetplanner.ui.appscreens.categories.subscreens.createedit.CategoryCreateEditViewModel
import com.s95ammar.budgetplanner.ui.appscreens.categories.subscreens.createedit.data.CategoryCreateEditUiEvent
import com.s95ammar.budgetplanner.ui.appscreens.categories.subscreens.createedit.validation.CategoryCreateEditValidator
import com.s95ammar.budgetplanner.ui.common.CreateEditMode
import com.s95ammar.budgetplanner.ui.common.validation.ValidationErrors
import com.s95ammar.budgetplanner.ui.compose.AppToolbar
import com.s95ammar.budgetplanner.ui.compose.ApplyButton
import com.s95ammar.budgetplanner.ui.compose.values.AppTheme
import com.s95ammar.budgetplanner.ui.compose.values.InputTextField
import com.s95ammar.budgetplanner.ui.main.MainViewModel
import com.s95ammar.budgetplanner.util.collectAsStateSafe
import com.s95ammar.budgetplanner.util.stringResourceOrNull
import kotlinx.coroutines.flow.collect

@Composable
fun CategoryCreateEditScreen(
    navController: NavController,
    activityViewModel: MainViewModel,
    viewModel: CategoryCreateEditViewModel = viewModel()
) {
    val mode by viewModel.mode.collectAsStateSafe()
    val name by viewModel.name.collectAsStateSafe()
    val validationErrors by viewModel.validationErrors.collectAsStateSafe()

    LaunchedEffect(key1 = Unit) {
        viewModel.uiEvent.collect {
            handleUiEvent(it, activityViewModel, navController)
        }
    }

    CategoryCreateEditContent(
        toolbarTitleResId = getToolbarTitleStringResId(mode),
        applyButtonTextResId = getApplyButtonStringResId(mode),
        name = name,
        onNameChange = viewModel::onNameChanged,
        validationErrors = validationErrors,
        onBackClick = viewModel::onBack,
        onApplyClick = {
            viewModel.onApply()
        }
    )
}

@Composable
fun CategoryCreateEditContent(
    @StringRes toolbarTitleResId: Int,
    @StringRes applyButtonTextResId: Int,
    name: String,
    onNameChange: (String) -> Unit,
    validationErrors: ValidationErrors?,
    onBackClick: () -> Unit,
    onApplyClick: () -> Unit
) {
    Scaffold(
        topBar = {
            AppToolbar(
                title = stringResource(id = toolbarTitleResId),
                onBackClick = onBackClick
            )
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    horizontal = 22.dp,
                    vertical = 12.dp
                )
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                InputTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp),
                    error = stringResourceOrNull(getNameErrorResIdOrZero(validationErrors)),
                    hint = stringResource(id = R.string.name),
                    value = name,
                    onValueChange = onNameChange
                )
            }
            ApplyButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter),
                text = stringResource(id = applyButtonTextResId),
                onClick = onApplyClick,
            )
        }
    }
}

@StringRes
private fun getToolbarTitleStringResId(mode: CreateEditMode) = when (mode) {
    CreateEditMode.CREATE -> R.string.create_category
    CreateEditMode.EDIT -> R.string.edit_category
}

@StringRes
private fun getApplyButtonStringResId(mode: CreateEditMode) = when (mode) {
    CreateEditMode.CREATE -> R.string.create
    CreateEditMode.EDIT -> R.string.save
}

@StringRes
private fun getNameErrorResIdOrZero(validationErrors: ValidationErrors?): Int? {
    return validationErrors
        ?.viewsErrors
        ?.find { it.viewKey == CategoryCreateEditValidator.ViewKeys.VIEW_NAME }
        ?.highestPriorityOrNone
}

private fun handleUiEvent(
    uiEvent: CategoryCreateEditUiEvent,
    activityViewModel: MainViewModel,
    navController: NavController
) {
    when (uiEvent) {
        is CategoryCreateEditUiEvent.DisplayLoadingState -> activityViewModel.setMainLoadingState(uiEvent.loadingState)
        CategoryCreateEditUiEvent.Exit -> navController.navigateUp()
    }
}

@Composable
@Preview
fun Preview() {
    AppTheme {
        CategoryCreateEditContent(
            toolbarTitleResId = R.string.create_category,
            applyButtonTextResId = R.string.create,
            name = "Groceries",
            onNameChange = {},
            validationErrors = ValidationErrors(
                listOf(
                    ValidationErrors.ViewErrors(
                        CategoryCreateEditValidator.ViewKeys.VIEW_NAME,
                        listOf(CategoryCreateEditValidator.Errors.NAME_TAKEN)
                    )
                )
            ),
            onBackClick = {},
            onApplyClick = {}
        )
    }
}
