package com.domain.cats.app.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.domain.cats.app.data.FactsRepository
import io.reactivex.Scheduler

class FactsViewModelFactory(
    private val repository: FactsRepository,
    private val ioScheduler: Scheduler,  // Scheduler for IO
    private val mainScheduler: Scheduler // Scheduler for UI/Main
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FactsViewModel::class.java)) {
            return FactsViewModel(repository, ioScheduler, mainScheduler) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}