package com.domain.cats.app.data

import com.domain.cats.app.data.remote.FactsService
import com.domain.cats.app.domain.models.Cat
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

interface FactsRepository {
    fun fetchFacts(): Flow<List<Cat>>

    class Impl @Inject constructor(private val service: FactsService) : FactsRepository {

        override fun fetchFacts(): Flow<List<Cat>> = flow {
            val factsResponse = service.fetchFacts()
            val facts = factsResponse.map { it.toCat() }
            emit(facts)
        }
    }
}
