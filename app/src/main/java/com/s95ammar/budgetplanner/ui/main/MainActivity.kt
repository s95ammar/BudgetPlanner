package com.s95ammar.budgetplanner.ui.main

import android.content.Context
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.s95ammar.budgetplanner.R
import com.s95ammar.budgetplanner.ui.common.KeyboardManager
import com.s95ammar.budgetplanner.ui.common.LoadingDialog
import com.s95ammar.budgetplanner.ui.common.LoadingManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), KeyboardManager, LoadingManager {

    private val bottomNavItems = listOf(
        R.id.navigation_current_budget,
        R.id.navigation_budgets_list,
        R.id.navigation_categories,
        R.id.navigation_saving_jars
    )

    private val navController: NavController by lazy {
        (supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment).navController
    }

    private val loadingDialog by lazy { LoadingDialog.newInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottom_nav_view.setupWithNavController(navController)
        bottom_nav_view.setOnNavigationItemSelectedListener { item -> onNavigationItemSelected(item) }
        navController.addOnDestinationChangedListener { _, destination, _ -> onDestinationChanged(destination) }
    }

    private fun onNavigationItemSelected(item: MenuItem): Boolean {
        if (item.itemId in bottomNavItems) navController.popBackStack()
        return NavigationUI.onNavDestinationSelected(item, navController)
    }

    private fun onDestinationChanged(destination: NavDestination) {
        bottom_nav_view.isVisible = (destination.id in bottomNavItems)
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
        loadingDialog.show(supportFragmentManager, LoadingDialog.TAG)
    }

    override fun hideLoading() {
        loadingDialog.dismiss()
    }
}