package com.domain.cats.app.domain.usecases

import com.domain.cats.app.data.FactsRepository
import com.domain.cats.app.domain.models.Cat
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

interface FetchFactsUseCase {
    fun execute(): Flow<List<Cat>>

    class Impl @Inject constructor(
        private val repository: FactsRepository,
        private val ioDispatcher: CoroutineDispatcher
    ) : FetchFactsUseCase {

        override fun execute(): Flow<List<Cat>> {
            return repository
                .fetchFacts()
                .flowOn(ioDispatcher)
        }
    }
}