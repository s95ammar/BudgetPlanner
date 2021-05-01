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
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.s95ammar.budgetplanner.R
import com.s95ammar.budgetplanner.ui.common.KeyboardManager
import com.s95ammar.budgetplanner.ui.common.loading.LoadingDialog
import com.s95ammar.budgetplanner.ui.common.loading.LoadingManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), KeyboardManager, LoadingManager {

    private val bottomNavItems = listOf(
        R.id.navigation_dashboard,
        R.id.navigation_categories,
        R.id.navigation_saving_jars
    )

    private val navController: NavController by lazy {
        (supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment).navController
    }

    private val bottomNavView by lazy { findViewById<BottomNavigationView>(R.id.bottom_nav_view) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNavView.setupWithNavController(navController)
        bottomNavView.setOnNavigationItemSelectedListener { item -> onNavigationItemSelected(item) }
        navController.addOnDestinationChangedListener { _, destination, _ -> onDestinationChanged(destination) }
    }

    private fun onNavigationItemSelected(item: MenuItem): Boolean {
        if (item.itemId in bottomNavItems) navController.popBackStack()
        return NavigationUI.onNavDestinationSelected(item, navController)
    }

    private fun onDestinationChanged(destination: NavDestination) {
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
}