package com.domain.cats.app.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.domain.cats.app.data.FactsRepository
import com.domain.cats.app.domain.models.Cat
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable

class FactsViewModel(
    private val repository: FactsRepository,
    private val ioScheduler: Scheduler,  // Scheduler for IO
    private val mainScheduler: Scheduler // Scheduler for UI/Main
) : ViewModel() {

    private val disposable = CompositeDisposable()

    val loading = MutableLiveData<Boolean>()
    val error = MutableLiveData<Throwable>()
    val catsFacts = MutableLiveData<List<Cat>>()

    override fun onCleared() {
        disposable.clear()
        super.onCleared()
    }

    fun fetchFacts() {
        // TODO: implement fetchFacts method
    }
}
