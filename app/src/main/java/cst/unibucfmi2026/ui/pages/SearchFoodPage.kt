package cst.unibucfmi2026.ui.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import cst.unibucfmi2026.R
import cst.unibucfmi2026.ui.theme.UnibucFMI2026Theme
import cst.unibucfmi2026.utils.AbridgedFoodNutrient
import cst.unibucfmi2026.utils.Background
import cst.unibucfmi2026.utils.SearchResultFood
import cst.unibucfmi2026.viewmodel.FoodViewModel

@Composable
fun SearchFoodPage(
    viewModel: FoodViewModel = viewModel(),
    onNavigateToMyFoods: () -> Unit ={},
    onGoHome: () -> Unit ={}
) {
    val state by viewModel.searchState.collectAsState()
    val detailState by viewModel.foodDetailState.collectAsState()
    var query by remember { mutableStateOf("") }

    if (detailState.isLoading || detailState.detail != null || detailState.errorMessage != null) {
        FoodDetailDialog(
            state = detailState,
            onDismiss = { viewModel.clearFoodDetail() }
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Background()
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(32.dp))
                Text(
                    text = stringResource(R.string.search_food),
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = query,
                    onValueChange = { query = it },
                    label = { Text(stringResource(R.string.food_name)) },
                    trailingIcon = {
                        IconButton(onClick = { viewModel.search(query) }) {
                            Icon(Icons.Default.Search, contentDescription = "Search")
                        }
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Search
                    ),
                    keyboardActions = KeyboardActions(onSearch = { viewModel.search(query) }),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                if (state.isLoading) {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(28.dp),
                            strokeWidth = 2.dp
                        )
                    }
                }
                state.errorMessage?.let {
                    Text(
                        it,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            items(state.results) { food ->
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    FoodResultItem(food)
                    Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                        Button(
                            onClick = {
                                viewModel.addFood(
                                    food = food,
                                    onSuccess = onNavigateToMyFoods
                                )
                            }
                        ) {
                            Text(stringResource(R.string.add_food))
                        }
                        OutlinedButton(
                            onClick = { viewModel.fetchFoodDetail(food.fdcId) }
                        ) {
                            Text(stringResource(R.string.details))
                        }
                    }

                    HorizontalDivider(
                        modifier = Modifier.padding(top = 12.dp)
                    )
                }
            }

            item {
                Button(
                    onClick = onGoHome,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(stringResource(R.string.home))
                }
            }
        }
    }
}

@Composable
private fun FoodResultItem(food: SearchResultFood) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Text(food.description, fontWeight = FontWeight.SemiBold)
        food.brandOwner?.let {
            Text(
                it,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.secondary
            )
        }
        val summary = nutrientSummary(food.foodNutrients)
        if (summary.isNotEmpty()) {
            Text(summary, style = MaterialTheme.typography.bodySmall)
        }
    }
}

private fun nutrientSummary(nutrients: List<AbridgedFoodNutrient>?): String {
    if (nutrients.isNullOrEmpty()) return ""
    val map = nutrients.associateBy { it.number }
    return listOfNotNull(
        map[1008]?.let { "${it.amount?.toInt() ?: 0} ${it.unitName ?: "kcal"}" },
        map[1003]?.let { "${it.amount?.toInt() ?: 0}g protein" },
        map[1004]?.let { "${it.amount?.toInt() ?: 0}g fat" },
        map[1005]?.let { "${it.amount?.toInt() ?: 0}g carbs" }
    ).joinToString(" · ")
}

@Preview(showBackground = true)
@Composable
fun SearchFoodPagePreview() {
    UnibucFMI2026Theme {
        SearchFoodPage()
    }
}
