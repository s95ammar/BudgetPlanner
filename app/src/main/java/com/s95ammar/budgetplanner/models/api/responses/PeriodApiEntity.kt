package com.s95ammar.budgetplanner.models.api.responses

import com.google.gson.annotations.SerializedName

data class PeriodApiEntity(
    @SerializedName("id") val id: Int?,
    @SerializedName("name") val name: String?,
    @SerializedName("max") val max: Int?,
    @SerializedName("periodRecords") val periodRecords: List<PeriodRecordApiEntity?>?,
    @SerializedName("budgetTransactions") val budgetTransactions: List<BudgetTransactionApiEntity>?,
)