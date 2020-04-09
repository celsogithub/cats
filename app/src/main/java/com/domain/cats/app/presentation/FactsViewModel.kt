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
        disposable.add(repository.fetchFacts()
            .subscribeOn(ioScheduler)
            .observeOn(mainScheduler)
            .compose(SimpleObservableTransformer(loading, error))
            .subscribe {
                catsFacts.value = it
            }
        )

//        disposable.add(repository.fetchFacts()
//            .subscribeOn(ioScheduler)
//            .observeOn(mainScheduler)
//            .compose(SimpleObservableTransformer(loading, error))
//            .subscribe({
//                catsFacts.value = it
//            }, {
//                error.value = it
//                it.printStackTrace()
//            })
//        )

//        disposable.add(repository.fetchFacts()
//            .subscribeOn(ioScheduler)
//            .observeOn(mainScheduler)
//            .doOnSubscribe { loading.value = true }
//            .doOnError { loading.value = false }
//            .doOnComplete { loading.value = false }
//            .subscribe({
//                catsFacts.value = it
//            }, {
//                error.value = it
//                it.printStackTrace()
//            })
//        )
    }
}