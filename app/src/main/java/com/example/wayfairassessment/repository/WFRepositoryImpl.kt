package com.example.wayfairassessment.repository


import com.example.wayfairassessment.model.WFProduct
import com.example.wayfairassessment.retrofit.ApiService
import java.lang.IllegalStateException
import com.example.wayfairassessment.util.Result
import javax.inject.Inject

class WFRepositoryImpl @Inject constructor(private val service: ApiService) : WFRepository {

    override suspend fun fetchProductList(): Result<List<WFProduct>> {
        return try{
            val apiResponse  = service.getProductList()
            if(apiResponse.isSuccessful) {
                val productListResult : List<WFProduct> ?= apiResponse.body()
                if(productListResult != null) {
                    println(productListResult)
                    Result.Success(productListResult)
                } else {
                    Result.Error(IllegalStateException("Response successful but no product found"))
                }
            } else {
                Result.Error(IllegalStateException("failed response failed: ${apiResponse.code()}"))
            }
        }catch (exception : Exception) {
            Result.Error(IllegalStateException(exception.message))
        }

    }
}