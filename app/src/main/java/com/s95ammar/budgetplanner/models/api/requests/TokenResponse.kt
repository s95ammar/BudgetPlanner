package com.s95ammar.budgetplanner.models.api.requests

import com.google.gson.annotations.SerializedName

class TokenResponse(@SerializedName("token") val token: String)