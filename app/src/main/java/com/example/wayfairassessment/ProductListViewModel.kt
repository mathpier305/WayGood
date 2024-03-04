package com.example.wayfairassessment


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wayfairassessment.model.WFProduct
import com.example.wayfairassessment.repository.WFRepository
import com.example.wayfairassessment.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductListViewModel @Inject constructor(private val repository: WFRepository): ViewModel() {

    private val viewModelState = MutableStateFlow(ProductListUiState())
    val uiState = viewModelState.asStateFlow()

    fun fetchProductList() {
        viewModelScope.launch {
            viewModelState.update { it.copy(isLoading = true, screenState = ProductListScreenState.LOADING) }
            when(val result = repository.fetchProductList()) {
                is Result.Error -> {
                    viewModelState.update { it.copy(screenState = ProductListScreenState.ERROR, isLoading = false) }
                }
                is Result.Success -> {
                    viewModelState.update { it.copy(isLoading = false, listOfProduct = result.data, screenState = ProductListScreenState.PRODUCT_LIST_WITH_DATA)  }
                }
            }
        }
    }
}

enum class ProductListScreenState {
    LOADING, IDLE, ERROR, PRODUCT_LIST_WITH_DATA
}

data class ProductListUiState(
    val screenState: ProductListScreenState = ProductListScreenState.IDLE,
    val isLoading: Boolean = false,
    val listOfProduct: List<WFProduct> = emptyList(),
    val errorMessage: String = "",
)