package com.domain.cats.app.utils

import com.domain.cats.app.BuildConfig
import com.domain.cats.app.data.FactsRepository
import com.domain.cats.app.data.remote.FactsService
import com.domain.cats.app.domain.usecases.FetchFactsUseCase
import com.domain.cats.app.presentation.FactsViewModelFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

object InjectorUtils {

    private const val DEFAULT_TIMEOUT_SECONDS = 15L

    @JvmStatic
    private fun providesLoggerInterceptor(debuggable: Boolean = false): Interceptor {
        val loggingLevel = if (debuggable) Level.BODY else Level.NONE
        return HttpLoggingInterceptor().apply { level = loggingLevel }
    }

    @JvmStatic
    private fun provideHttpClient(logger: Interceptor): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(logger)
            .connectTimeout(DEFAULT_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .readTimeout(DEFAULT_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .writeTimeout(DEFAULT_TIMEOUT_SECONDS, TimeUnit.MILLISECONDS)
            .build()

    @JvmStatic
    private fun provideMoshi(): Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    @JvmStatic
    fun provideFactsService(
        httpClient: OkHttpClient,
        moshi: Moshi,
        apiURL: String = BuildConfig.BASE_URL
    ): FactsService {
        val converterFactory = MoshiConverterFactory.create(moshi)
        val retrofit = Retrofit.Builder()
            .baseUrl(apiURL)
            .client(httpClient)
            .addConverterFactory(converterFactory)
            .build()

        return retrofit.create(FactsService::class.java)
    }

    @JvmStatic
    private fun provideFactsRepository(service: FactsService): FactsRepository {
        return FactsRepository.Impl.getInstance(service)
    }

    @JvmStatic
    private fun provideFetchFactsUseCase(
        repository: FactsRepository,
        ioDispatcher: CoroutineDispatcher
    ): FetchFactsUseCase {
        return FetchFactsUseCase.Impl(repository, ioDispatcher)
    }

    @JvmStatic
    fun provideFactsViewModelFactory(
        debuggable: Boolean = BuildConfig.DEBUG,
    ): FactsViewModelFactory {
        val logger = providesLoggerInterceptor(debuggable)
        val okHttpClient = provideHttpClient(logger)
        val moshi = provideMoshi()
        val service = provideFactsService(okHttpClient, moshi)
        val repository = provideFactsRepository(service)
        val useCase = provideFetchFactsUseCase(repository, Dispatchers.IO)
        return FactsViewModelFactory(useCase)
    }
}