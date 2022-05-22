package com.s95ammar.budgetplanner.di

import com.s95ammar.budgetplanner.BuildConfig
import com.s95ammar.budgetplanner.models.datasource.remote.api.ApiConfig
import com.s95ammar.budgetplanner.models.datasource.remote.api.ApiService
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
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(ApiConfig.TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .readTimeout(ApiConfig.TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .writeTimeout(ApiConfig.TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .addInterceptor { chain ->
                val authorisedRequest: Request = chain.request().newBuilder()
                    .addHeader(ApiConfig.HEADER_HOST.key, ApiConfig.HEADER_HOST.value)
                    .addHeader(ApiConfig.HEADER_KEY.key, ApiConfig.HEADER_KEY.value)
                    .build()
                chain.proceed(authorisedRequest);
            }
            .apply {
                if (BuildConfig.DEBUG) addInterceptor(
                    HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
                )
            }
            .build()
    }

    @Singleton
    @Provides
    fun provideApiService(okHttpClient: OkHttpClient): ApiService {
        return Retrofit.Builder()
            .baseUrl(ApiConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

}