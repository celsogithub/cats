package com.domain.cats.app.data

import com.domain.cats.app.data.remote.FactsService
import com.domain.cats.app.domain.models.Cat
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface FactsRepository {
    fun fetchFacts(): Flow<List<Cat>>

    class Impl private constructor(private val service: FactsService) : FactsRepository {

        override fun fetchFacts(): Flow<List<Cat>> = flow {
            val factsResponse = service.fetchFacts()
            val facts = factsResponse.map { it.toCat() }
            emit(facts)
        }

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
