package com.s95ammar.budgetplanner.models.api.requests

import com.google.gson.annotations.SerializedName

data class UserCredentials(
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String
)