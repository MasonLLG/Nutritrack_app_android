package com.fit2081assignment3.nutritrack.data

import com.fit2081assignment3.nutritrack.data.model.Fruit
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface FruitApiService {
    @GET("api/fruit/all")
    suspend fun getAllFruits(): Response<List<Fruit>>

    @GET("api/fruit/{id}")
    suspend fun getFruitById(@Path("id") id: Int): Response<Fruit>

    @GET("api/fruit/{name}")
    suspend fun getFruitByName(@Path("name") name: String): Response<Fruit>
}