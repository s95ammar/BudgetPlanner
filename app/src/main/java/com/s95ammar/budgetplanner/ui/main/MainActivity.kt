package com.s95ammar.budgetplanner.ui.main

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.s95ammar.budgetplanner.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val bottomNavItems = listOf(
        R.id.navigation_current_budget,
        R.id.navigation_budgets_list,
        R.id.navigation_categories,
        R.id.navigation_saving_jars
    )

    private val navController: NavController by lazy {
        (supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment).navController
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottom_nav_view.setupWithNavController(navController)
        bottom_nav_view.setOnNavigationItemSelectedListener { item -> onNavigationItemSelected(item) }
        navController.addOnDestinationChangedListener { _, destination, _ -> onDestinationChanged(destination) }
    }

    private fun onDestinationChanged(destination: NavDestination) {
        bottom_nav_view.isVisible = bottomNavItems.contains(destination.id)
    }

    private fun onNavigationItemSelected(item: MenuItem): Boolean {
        if (item.itemId in bottomNavItems) navController.popBackStack()
        return NavigationUI.onNavDestinationSelected(item, navController)
    }
}