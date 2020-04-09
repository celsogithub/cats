package com.domain.cats.app.data

import com.domain.cats.app.data.remote.FactsService
import com.domain.cats.app.domain.models.Cat
import io.reactivex.Observable

interface FactsRepository {
    fun fetchFacts(): Observable<List<Cat>>

    class Impl private constructor(private val service: FactsService) : FactsRepository {
        override fun fetchFacts(): Observable<List<Cat>> {
            return service.fetchFacts().map { factsResponse ->
                factsResponse.all.map { it.toCat() }
            }
        }
        // Implement all methods here

        companion object {
            // For Singleton instantiation
            @Volatile private var instance: FactsRepository? = null

            fun getInstance(service: FactsService): FactsRepository =
                instance ?: synchronized(this) {
                    instance ?: Impl(service).also { instance = it }
                }
        }
    }
}