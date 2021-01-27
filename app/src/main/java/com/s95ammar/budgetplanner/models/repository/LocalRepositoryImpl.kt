package com.s95ammar.budgetplanner.models.repository

import com.s95ammar.budgetplanner.models.sharedprefs.SharedPrefsManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalRepositoryImpl @Inject constructor(
    private val sharedPrefs: SharedPrefsManager
) : LocalRepository {

    // SharedPreferences

    override fun saveAuthToken(token: String) = sharedPrefs.saveAuthToken(token)
    override fun loadAuthToken(): String? = sharedPrefs.loadAuthToken()
    override fun clearAuthToken() = sharedPrefs.clearAuthToken()

}