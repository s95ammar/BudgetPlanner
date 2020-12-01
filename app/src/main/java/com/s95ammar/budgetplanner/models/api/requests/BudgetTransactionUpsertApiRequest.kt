package com.s95ammar.budgetplanner.models.api.requests

import com.google.gson.annotations.SerializedName
import com.s95ammar.budgetplanner.models.common.IntBudgetTransactionType

sealed class BudgetTransactionUpsertApiRequest {

    data class Insertion(
        @SerializedName("name") val name: String?,
        @SerializedName("type") @IntBudgetTransactionType val type: Int?,
        @SerializedName("amount") val amount: Int?,
        @SerializedName("period_record_id") val periodRecordId: Int?
    ) : BudgetTransactionUpsertApiRequest()

    data class Update(
        @SerializedName("id") val id: Int?,
        @SerializedName("name") val name: String?,
        @SerializedName("type") @IntBudgetTransactionType val type: Int?,
        @SerializedName("amount") val amount: Int?,
        @SerializedName("period_record_id") val periodRecordId: Int?
    ) : BudgetTransactionUpsertApiRequest()

}