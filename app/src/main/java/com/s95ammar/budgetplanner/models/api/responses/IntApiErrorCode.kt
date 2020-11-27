package com.s95ammar.budgetplanner.models.api.responses

@Retention(AnnotationRetention.SOURCE)
annotation class IntApiErrorCode {
    companion object {

        const val UNKNOWN = 0

        const val REGISTER_EMAIL_TAKEN = 1

        const val LOGIN_USER_DOES_NOT_EXIST = 2
        const val LOGIN_INVALID_PASSWORD = 3

    }
}