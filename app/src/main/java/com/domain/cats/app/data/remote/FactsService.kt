package com.domain.cats.app.data.remote

import com.domain.cats.app.data.remote.models.CatResponse
import retrofit2.http.GET

interface FactsService {
    @GET("facts?animal_type=cat")
    suspend fun fetchFacts(): List<CatResponse>
}