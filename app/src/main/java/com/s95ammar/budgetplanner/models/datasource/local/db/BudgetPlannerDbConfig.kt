package com.s95ammar.budgetplanner.models.datasource.local.db

object BudgetPlannerDbConfig {
    const val DB_NAME = "budgetPlanner"
    const val DB_VERSION = 1

    const val TABLE_NAME_PERIOD = "period"
    const val TABLE_NAME_CATEGORY = "category"
    const val TABLE_NAME_PERIODIC_CATEGORY = "periodicCategory"
    const val TABLE_NAME_BUDGET_TRANSACTION = "budgetTransaction"

    const val COLUMN_NAME_ID = "id"
    const val COLUMN_NAME_PERIOD_ID = "periodId"
    const val COLUMN_NAME_CATEGORY_ID = "categoryId"
    const val COLUMN_NAME_PERIODIC_CATEGORY_ID = "periodicCategoryId"
}