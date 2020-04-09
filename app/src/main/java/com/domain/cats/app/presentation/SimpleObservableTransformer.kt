package com.domain.cats.app.presentation

import androidx.lifecycle.MutableLiveData
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.ObservableTransformer

class SimpleObservableTransformer<T>(
    private val loading: MutableLiveData<Boolean>,
    private val error: MutableLiveData<Throwable>
) : ObservableTransformer<T, T> {

    override fun apply(upstream: Observable<T>): ObservableSource<T> {
        return upstream
            .doOnSubscribe { loading.value = true }
            .doFinally { loading.value = false }
            .doOnError { errorException -> error.value = errorException }
    }
}