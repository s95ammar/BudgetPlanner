package com.s95ammar.budgetplanner.models.datasource.local.db

object BudgetPlannerDbConfig {
    const val DATABASE_FILE_PATH = "database/budgetPlanner.db"

    const val DB_NAME = "budgetPlanner"
    const val DB_VERSION = 1

    const val TABLE_NAME_PERIOD = "period"
    const val TABLE_NAME_CATEGORY = "category"
    const val TABLE_NAME_CATEGORY_OF_PERIOD = "categoryOfPeriod"
    const val TABLE_NAME_BUDGET_TRANSACTION = "budgetTransaction"
    const val TABLE_NAME_CURRENCY = "currency"

    const val COLUMN_NAME_ID = "id"
    const val COLUMN_NAME_PERIOD_ID = "periodId"
    const val COLUMN_NAME_CATEGORY_ID = "categoryId"
    const val COLUMN_NAME_CATEGORY_OF_PERIOD_ID = "categoryOfPeriodId"
    const val COLUMN_NAME_NAME = "name"
    const val COLUMN_NAME_CODE = "code"
    const val COLUMN_NAME_CURRENCY_CODE = "currencyCode"
}