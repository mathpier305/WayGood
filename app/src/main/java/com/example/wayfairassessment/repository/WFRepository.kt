package com.example.wayfairassessment.repository

import com.example.wayfairassessment.model.WFProduct
import com.example.wayfairassessment.util.Result

interface WFRepository {
    suspend fun fetchProductList() : Result<List<WFProduct>>
}