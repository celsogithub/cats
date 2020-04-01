package com.domain.cats.app.data.remote

import com.domain.cats.app.data.remote.models.FactsResponse
import io.reactivex.Observable
import retrofit2.http.GET

interface FactsService {
    @GET("facts")
    fun fetchFacts(): Observable<FactsResponse>
}