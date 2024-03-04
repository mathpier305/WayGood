package com.example.wayfairassessment

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.example.wayfairassessment.model.WFProduct
import com.example.wayfairassessment.ui.theme.WayFairAssessmentTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<ProductListViewModel>()
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.fetchProductList()
        setContent {
            WayFairAssessmentTheme {
                // A surface container using the 'background' color from the theme
                val uiState = viewModel.uiState.collectAsState()
                Scaffold(
                    topBar = {
                        TopAppBar(title = {
                            Text(text="WayGood")
                        }, colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = Color.Blue,
                            titleContentColor = Color.White)
                        )
                    }
                ) {
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(it),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        ProductListContainer(uiState = uiState.value)
                    }
                }

            }
        }
    }
}

@Composable
fun ProductListContainer(uiState: ProductListUiState) {
    when(uiState.screenState) {
        ProductListScreenState.LOADING -> {
            ProductListLoading()
        }
        ProductListScreenState.ERROR -> {
            ProductListError()
        }
        ProductListScreenState.PRODUCT_LIST_WITH_DATA -> {
            ProductListScreen(productList = uiState.listOfProduct)
        }
        else -> {}
    }
}

@Composable
fun ProductListLoading() {
    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        CircularProgressIndicator()
        Text(text="Loading...")
    }
}

@Composable
fun ProductListError() {
    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Icon(
            imageVector = Icons.Default.Warning,
            contentDescription = "error",
            tint= Color.Red,
            modifier = Modifier
                .width(84.dp)
                .height(84.dp)
        )
        Text(
            text="An error occurred while fetching data",
            lineHeight = 1.25.em,
            textAlign = TextAlign.Center,
            fontSize = 36.sp,
            )
    }
}

@Composable
fun ProductListScreen(productList: List<WFProduct>) {
    LazyColumn {
        items(productList) {
            FurnitureItem(product = it)
        }
    }
    
}

@Composable
fun FurnitureItem(product: WFProduct) {
    Card(
        modifier = Modifier.padding(start =8.dp, end=8.dp, top=8.dp, bottom =8.dp),
        shape = RoundedCornerShape(24.dp),
        border = BorderStroke(2.dp, color=Color.LightGray )
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text=product.name,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text=product.tagline,
                fontSize = 18.sp,
            )
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(
                    text="stars ${product.rating}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.W700
                )
                Text(text = product.date, fontSize = 14.sp, color = Color.Magenta)
            }

        }
    }

}

@Preview(showBackground = true)
@Composable
fun PreviewProductListError() {
    WayFairAssessmentTheme {
        ProductListError()
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    WayFairAssessmentTheme {
        val product = WFProduct(
            name="Yellow Chair",
            tagline="Great if you like it Great if you like it Great if you like it Great if you like it Great if you like it Great if you like it Great if you like it Great if you like it Great if you like it Great if you like it Great if you like it",
            rating=5.45,
            date="1-15-2018"
        )
        FurnitureItem(product)
    }
}