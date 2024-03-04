package com.example.wayfairassessment

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.compose.runtime.collectAsState
import androidx.test.platform.device.DeviceController.ScreenOrientation
import com.example.wayfairassessment.model.WFProduct
import com.example.wayfairassessment.repository.WFRepository
import com.example.wayfairassessment.retrofit.ApiService
import com.example.wayfairassessment.util.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.currentTime
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import java.lang.Exception

@OptIn(ExperimentalCoroutinesApi::class)
class ProductListViewModelTest {

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutinesApi = MainCoroutineRule()

    private lateinit var mockWFRepository: WFRepository
    private lateinit var productListViewModel: ProductListViewModel

    @Before
    fun setup(){
        mockWFRepository = mock(WFRepository::class.java)
        productListViewModel = ProductListViewModel(mockWFRepository)
    }


    @Test
    fun `test result failed when fetchProductList is called`() = runTest {

        `when`(mockWFRepository.fetchProductList()).thenReturn(Result.Error(Exception()))
        productListViewModel.fetchProductList()

        runCurrent()

        Assert.assertEquals(ProductListScreenState.ERROR, productListViewModel.uiState.value.screenState)
        Assert.assertEquals(false, productListViewModel.uiState.value.isLoading)
    }


    @Test
    fun `test products returned when fetchProductList is called`() = runTest {
        val list = mutableListOf<WFProduct>().apply {
            add(WFProduct("product 1", "tag 1", 2.5, "2-10-2022"))
            add(WFProduct("product 2", "tag 2", 1.395, "2-10-2022"))
            add(WFProduct("product 3", "tag 3", 4.559, "2-10-2022"))
            add(WFProduct("product 4", "tag 4", 3.957, "2-10-2022"))
        }
        `when`(mockWFRepository.fetchProductList()).thenReturn(Result.Success(list))
        productListViewModel.fetchProductList()

        runCurrent()

        Assert.assertEquals(4, productListViewModel.uiState.value.listOfProduct.size)
        Assert.assertEquals(ProductListScreenState.PRODUCT_LIST_WITH_DATA, productListViewModel.uiState.value.screenState)
    }


    @Test
    fun `test loading false, true, false when fetchProductList is called`() = runTest {
        val productListUiState = mutableListOf<ProductListUiState>()
        backgroundScope.launch(UnconfinedTestDispatcher()){
                productListViewModel.uiState.collect{productListUiState.add(it)}
            }
        Assert.assertEquals(false, productListViewModel.uiState.value.isLoading)

        `when`(mockWFRepository.fetchProductList()).thenReturn(Result.Success(emptyList()))
        productListViewModel.fetchProductList()

        runCurrent()
        Assert.assertEquals(false, (productListViewModel.uiState.value.isLoading))
        Assert.assertEquals(3, productListUiState.toList().size)
        Assert.assertEquals(listOf(false, true, false), productListUiState.map { it.isLoading })
    }


}