package com.s95ammar.budgetplanner.models.api.responses

import com.google.gson.annotations.SerializedName
import com.s95ammar.budgetplanner.models.common.IntBudgetTransactionType

data class BudgetTransactionApiEntity(
    @SerializedName("id") val id: Int?,
    @SerializedName("name") val name: String?,
    @SerializedName("type") @IntBudgetTransactionType val type: Int?,
    @SerializedName("amount") val amount: Int?,
    @SerializedName("creationUnixMs") val creationUnixMs: Long?,
    @SerializedName("periodRecordId") val periodRecordId: Int?,
    @SerializedName("userId") val userId: Int?
)