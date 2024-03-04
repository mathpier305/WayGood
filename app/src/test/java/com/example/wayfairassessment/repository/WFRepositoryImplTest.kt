package com.example.wayfairassessment.repository

import com.example.wayfairassessment.model.WFProduct
import com.example.wayfairassessment.retrofit.ApiService
import com.example.wayfairassessment.util.Result
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType
import okhttp3.ResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import retrofit2.Response


class WFRepositoryImplTest {

    private lateinit var mockClient: ApiService
    private lateinit var repository: WFRepository

    @Before
    fun setup() {
        mockClient = mock(ApiService::class.java)
        repository = WFRepositoryImpl(mockClient)
    }

    @Test
    fun `fetchProductList when error found`() = runTest {
        `when`(mockClient.getProductList()).thenAnswer { Response.error<String>(400, ResponseBody.create(
            MediaType.parse(""),"")) }
        val result = repository.fetchProductList()
        val a : Boolean = result is Result.Error
        assertTrue(a)
    }

    @Test
    fun `fetchProductList successfully when No product found`() = runTest {

        `when`(mockClient.getProductList()).thenAnswer { Response.success(emptyList<WFProduct>()) }
        val result = repository.fetchProductList()

        val a : Boolean = result is Result.Success
        assertTrue(a)
        if(result is Result.Success) {
            val productList : List<WFProduct> = result.data
            assertEquals(productList.size, 0)
        } else {
            fail()
        }
    }

    @Test
    fun `fetchProductList successfully when One product found`() = runTest {
        val list = mutableListOf<WFProduct>().apply {
            add(WFProduct("product 1", "tag 1", 2.5, "2-10-2022"))
        }
        `when`(mockClient.getProductList()).thenReturn( Response.success(200, list) )

        val result = repository.fetchProductList()
        val a : Boolean = result is Result.Success
        assertTrue(a)
        if(result is Result.Success) {
            val productList : List<WFProduct> = result.data
            assertEquals(1, productList.size, )
        } else {
            fail()
        }
    }

    @Test
    fun `fetchProductList successfully when multiple products found`() = runTest {
        val list = mutableListOf<WFProduct>().apply {
            add(WFProduct("product 1", "tag 1", 2.5, "2-10-2022"))
            add(WFProduct("product 2", "tag 2", 1.395, "2-10-2022"))
            add(WFProduct("product 3", "tag 3", 4.559, "2-10-2022"))
            add(WFProduct("product 4", "tag 4", 3.957, "2-10-2022"))
        }
        `when`(mockClient.getProductList()).thenAnswer { Response.success(list) }
        val result = repository.fetchProductList()
        val a : Boolean = result is Result.Success
        assertTrue(a)
        if(result is Result.Success) {
            val productList : List<WFProduct> = result.data
            assertEquals(productList.size, 4)
        } else {
            fail()
        }
    }
}