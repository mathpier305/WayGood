package com.example.wayfairassessment.retrofit

import com.example.wayfairassessment.model.WFProduct
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET("json-to-list/products.v1.json")
    suspend fun getProductList() : Response<List<WFProduct>>
}