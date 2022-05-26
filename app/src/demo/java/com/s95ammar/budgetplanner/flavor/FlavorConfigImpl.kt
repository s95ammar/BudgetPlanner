package com.s95ammar.budgetplanner.flavor

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FlavorConfigImpl @Inject constructor() : FlavorConfig {

    override fun shouldAutoCreatePeriodOnFirstLaunch(): Boolean {
        return false
    }
}
