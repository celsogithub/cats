package com.domain.cats.app.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.domain.cats.app.data.FactsRepository
import com.domain.cats.app.domain.usecases.FetchFactsUseCase

class FactsViewModelFactory(private val useCase: FetchFactsUseCase) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FactsViewModel::class.java)) {
            return FactsViewModel(useCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}