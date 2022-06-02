package com.s95ammar.budgetplanner.ui.main

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.s95ammar.budgetplanner.MobileNavigationDirections
import com.s95ammar.budgetplanner.R
import com.s95ammar.budgetplanner.ui.common.KeyboardManager
import com.s95ammar.budgetplanner.ui.common.LoadingState
import com.s95ammar.budgetplanner.ui.common.data.IntCurrencySelectionType
import com.s95ammar.budgetplanner.ui.common.loading.LoadingDialog
import com.s95ammar.budgetplanner.ui.common.loading.LoadingManager
import com.s95ammar.budgetplanner.ui.main.data.MainUiEvent
import com.s95ammar.budgetplanner.util.lifecycleutil.observeEvent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), KeyboardManager, LoadingManager {

    private val bottomNavItems = listOf(
        R.id.navigation_dashboard,
        R.id.navigation_categories,
        R.id.navigation_settings
    )

    private val navController: NavController by lazy {
        (supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment).navController
    }

    private val bottomNavView by lazy { findViewById<BottomNavigationView>(R.id.bottom_nav_view) }

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel.performUiEvent.observeEvent(this) { performUiEvent(it) }
        bottomNavView.setupWithNavController(navController)
        navController.addOnDestinationChangedListener { _, destination, _ -> onDestinationChanged(destination) }
    }

    private fun performUiEvent(uiEvent: MainUiEvent) {
        when (uiEvent) {
            is MainUiEvent.NavigateToMainCurrencySelection -> {
                navigateToMainCurrencySelection(uiEvent.currentCurrencyCode, uiEvent.isBackAllowed)
            }
            is MainUiEvent.FinishActivity -> finish()
            is MainUiEvent.DisplayLoadingState -> handleLoadingState(uiEvent.loadingState)
        }
    }

    // TODO: remove from fragments & call by activityViewModel.setLoadingState
    private fun handleLoadingState(loadingState: LoadingState) {
        when (loadingState) {
            is LoadingState.Cold,
            is LoadingState.Success -> hideLoading()
            is LoadingState.Loading -> showLoading()
            is LoadingState.Error -> {
                hideLoading()
            }
        }
    }

    private fun navigateToMainCurrencySelection(
        currentCurrencyCode: String?,
        isBackAllowed: Boolean
    ) {
        navController.navigate(
            MobileNavigationDirections.actionGlobalCurrencySelectionFragment(
                currentCurrencyCode = currentCurrencyCode,
                currencySelectionType = IntCurrencySelectionType.MAIN_CURRENCY,
                isBackAllowed
            )
        )
    }

    private fun onDestinationChanged(destination: NavDestination) {
        hideLoading()
        bottomNavView.isVisible = (destination.id in bottomNavItems)
        hideKeyboard()
    }

    override fun hideKeyboard() {
        val view: View = currentFocus ?: return
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        imm?.hideSoftInputFromWindow(view.windowToken, 0)
    }

    override fun showKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        imm?.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
    }

    override fun showLoading() {
        val loadingDialog = (supportFragmentManager.findFragmentByTag(LoadingDialog.TAG) as? LoadingDialog) ?: LoadingDialog.newInstance()
        if (!loadingDialog.isAdded)
            loadingDialog.show(supportFragmentManager, LoadingDialog.TAG)
    }

    override fun hideLoading() {
        supportFragmentManager.executePendingTransactions()
        val loadingDialog = supportFragmentManager.findFragmentByTag(LoadingDialog.TAG) as? LoadingDialog
        loadingDialog?.dismiss()
    }

    override fun onBackPressed() {
        if (navController.currentDestination?.id in bottomNavItems) {
            finish()
        } else {
            super.onBackPressed()
        }
    }
}