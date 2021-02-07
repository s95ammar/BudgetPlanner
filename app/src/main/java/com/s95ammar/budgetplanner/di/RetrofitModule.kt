package com.s95ammar.budgetplanner.di

import com.google.gson.GsonBuilder
import com.s95ammar.budgetplanner.BuildConfig
import com.s95ammar.budgetplanner.models.api.ApiService
import com.s95ammar.budgetplanner.models.api.BudgetPlannerApiConfig
import com.s95ammar.budgetplanner.models.sharedprefs.SharedPrefsManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {
    @Singleton
    @Provides
    fun provideOkHttpClient(sharedPreferences: SharedPrefsManager): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request()
                if (request.url.encodedPath in BudgetPlannerApiConfig.NO_TOKEN_PATHS)
                    chain.proceed(request)
                else
                    chain.proceed(request.addTokenHeader(sharedPreferences.loadAuthToken()))
            }
            .apply {
                if (BuildConfig.DEBUG) addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })
            }
            .connectTimeout(BudgetPlannerApiConfig.TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .writeTimeout(BudgetPlannerApiConfig.TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .readTimeout(BudgetPlannerApiConfig.TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .build()
    }

    private fun Request.addTokenHeader(token: String?): Request {
        val requestBuilder: Request.Builder = newBuilder()

        if (!token.isNullOrEmpty())
            requestBuilder.addHeader(BudgetPlannerApiConfig.TOKEN_HEADER_KEY, "${BudgetPlannerApiConfig.TOKEN_HEADER_VALUE_PREFIX}$token")

        return requestBuilder.build()
    }

    @Singleton
    @Provides
    fun provideApiService(okHttpClient: OkHttpClient): ApiService {
        return Retrofit.Builder().baseUrl(BudgetPlannerApiConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setPrettyPrinting().create()))
            .client(okHttpClient)
            .build()
            .create(ApiService::class.java)
    }
}

