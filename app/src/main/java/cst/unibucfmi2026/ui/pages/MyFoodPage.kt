package cst.unibucfmi2026.ui.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import cst.unibucfmi2026.R
import cst.unibucfmi2026.data.entities.FoodEntity
import cst.unibucfmi2026.ui.theme.UnibucFMI2026Theme
import cst.unibucfmi2026.utils.Background
import cst.unibucfmi2026.viewmodel.FoodViewModel

@Composable
fun MyFoodPage(
    viewModel: FoodViewModel = viewModel(),
    onGoHome: () -> Unit = {}
) {
    val foods by viewModel.userFoods.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        Background()
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = stringResource(R.string.my_foods),
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            if (foods.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 48.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            stringResource(R.string.no_foods),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else {
                items(foods) { food ->
                    SavedFoodItem(food)
                    HorizontalDivider(modifier = Modifier.padding(top = 8.dp))
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
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
        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
private fun SavedFoodItem(food: FoodEntity) {
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
        val parts = listOfNotNull(
            food.calories?.toInt()?.let { "$it kcal" },
            food.protein?.toInt()?.let { "${it}g protein" },
            food.fat?.toInt()?.let { "${it}g fat" },
            food.carbs?.toInt()?.let { "${it}g carbs" }
        )
        if (parts.isNotEmpty()) {
            Text(parts.joinToString(" · "), style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MyFoodPagePreview() {
    UnibucFMI2026Theme {
        MyFoodPage()
    }
}
