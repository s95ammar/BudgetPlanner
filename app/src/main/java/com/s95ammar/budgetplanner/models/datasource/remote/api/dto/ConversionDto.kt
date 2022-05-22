package com.s95ammar.budgetplanner.models.datasource.remote.api.dto

import com.google.gson.annotations.SerializedName

data class ConversionDto(

    @SerializedName("amount")
    private val amount: Double? = null,

    @SerializedName("base_currency_code")
    val baseCurrencyCode: String? = null,

    @SerializedName("base_currency_name")
    val baseCurrencyName: String? = null,

    @SerializedName("rates")
    val rates: Map<String, TargetCurrency>? = null,

    @SerializedName("updated_date")
    val updatedDate: String? = null
) {

    data class TargetCurrency(

        @SerializedName("currency_name")
        val currencyName: String? = null,

        @SerializedName("rate")
        val rate: Double? = null,

        @SerializedName("rate_for_amount")
        val rateForAmount: Double? = null
    )

}
