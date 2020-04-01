package com.domain.cats.app.utils

import com.domain.cats.app.BuildConfig
import com.domain.cats.app.data.FactsRepository
import com.domain.cats.app.data.remote.FactsService
import com.domain.cats.app.presentation.FactsViewModelFactory
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
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
    fun provideFactsService(
        httpClient: OkHttpClient,
        apiURL: String = BuildConfig.BASE_URL
    ): FactsService {
        val converterFactory = GsonConverterFactory.create()
        val rxAdapter = RxJava2CallAdapterFactory.create()
        val retrofit = Retrofit.Builder()
            .baseUrl(apiURL)
            .client(httpClient)
            .addConverterFactory(converterFactory)
            .addCallAdapterFactory(rxAdapter)
            .build()

        return retrofit.create(FactsService::class.java)
    }

    @JvmStatic
    private fun provideFactsRepository(service: FactsService): FactsRepository {
        return FactsRepository.Impl.getInstance(service)
    }

    @JvmStatic
    fun provideFactsViewModelFactory(
        debuggable: Boolean = BuildConfig.DEBUG,
        ioScheduler: Scheduler = Schedulers.io(),
        mainScheduler: Scheduler = AndroidSchedulers.mainThread()
    ): FactsViewModelFactory {
        val logger = providesLoggerInterceptor(debuggable)
        val okHttpClient = provideHttpClient(logger)
        val service = provideFactsService(okHttpClient)
        val repository = provideFactsRepository(service)
        return FactsViewModelFactory(repository, ioScheduler, mainScheduler)
    }
}