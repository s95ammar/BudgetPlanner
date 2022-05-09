package com.s95ammar.budgetplanner.flavor

import androidx.room.RoomDatabase

interface FlavorConfig {

    fun <T : RoomDatabase> RoomDatabase.Builder<T>.configureDbPrepopulation(): RoomDatabase.Builder<T>
}
