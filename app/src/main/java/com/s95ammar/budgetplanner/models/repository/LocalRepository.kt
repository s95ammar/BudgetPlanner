package com.s95ammar.budgetplanner.models.repository

import com.s95ammar.budgetplanner.models.persistence.dao.*
import com.s95ammar.budgetplanner.models.sharedprefs.SharedPrefsManager

interface LocalRepository : SharedPrefsManager, BudgetDao, CategoryDao, BudgetTransactionDao, SavingJarDao, SavingDao