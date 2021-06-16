package com.domain.cats.app.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.domain.cats.app.domain.models.Cat
import com.domain.cats.app.domain.usecases.FetchFactsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FactsViewModel @Inject constructor(
    private val useCase: FetchFactsUseCase
) : ViewModel() {

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean>
        get() = _loading

    private val _error = MutableLiveData<Throwable>()
    val error: LiveData<Throwable>
        get() = _error

    private val _catsFacts = MutableLiveData<List<Cat>>()
    val catsFacts: LiveData<List<Cat>>
        get() = _catsFacts

    fun fetchFacts() {
        viewModelScope.launch {
            _loading.value = true

            useCase.execute()
                .catch { _error.value = it }
                .onCompletion { _loading.value = false }
                .collect { facts ->
                    _catsFacts.value = facts
                }
        }
    }
}
