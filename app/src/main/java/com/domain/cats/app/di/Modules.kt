package com.domain.cats.app.di

import com.domain.cats.app.BuildConfig
import com.domain.cats.app.data.FactsRepository
import com.domain.cats.app.data.remote.FactsService
import com.domain.cats.app.domain.usecases.FetchFactsUseCase
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(ViewModelComponent::class)
class DomainModule {
    @Provides
    @ViewModelScoped
    fun provideFetchFactsCountersUseCase(
        repository: FactsRepository,
        ioDispatcher: CoroutineDispatcher
    ): FetchFactsUseCase {
        return FetchFactsUseCase.Impl(repository, ioDispatcher)
    }
}

@Module
@InstallIn(SingletonComponent::class)
class DataModule {
    @Provides
    @Singleton
    fun provideCoroutinesDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    @Singleton
    fun providesLoggerInterceptor(): Interceptor {
        val loggingLevel = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        return HttpLoggingInterceptor().apply { level = loggingLevel }
    }

    @Provides
    @Singleton
    fun provideHttpClient(logger: Interceptor): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(logger)
            .connectTimeout(DEFAULT_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .readTimeout(DEFAULT_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .writeTimeout(DEFAULT_TIMEOUT_SECONDS, TimeUnit.MILLISECONDS)
            .build()

    @Provides
    @Singleton
    fun provideMoshi(): Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    @Provides
    @Singleton
    fun provideFactsService(
        httpClient: OkHttpClient,
        moshi: Moshi
    ): FactsService {
        val converterFactory = MoshiConverterFactory.create(moshi)
        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(httpClient)
            .addConverterFactory(converterFactory)
            .build()

        return retrofit.create(FactsService::class.java)
    }

    @Provides
    @Singleton
    fun provideFactsRepository(service: FactsService): FactsRepository {
        return FactsRepository.Impl(service)
    }

    companion object {
        private const val DEFAULT_TIMEOUT_SECONDS = 30L
    }
}