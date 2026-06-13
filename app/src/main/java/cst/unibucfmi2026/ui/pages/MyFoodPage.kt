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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import cst.unibucfmi2026.R
import cst.unibucfmi2026.data.entities.FoodEntity
import cst.unibucfmi2026.ui.theme.UnibucFMI2026Theme
import cst.unibucfmi2026.utils.Background
import cst.unibucfmi2026.viewmodel.FoodViewModel
import cst.unibucfmi2026.viewmodel.SortConfig
import cst.unibucfmi2026.viewmodel.SortField
import cst.unibucfmi2026.viewmodel.SortOrder

@Composable
fun MyFoodPage(
    viewModel: FoodViewModel = viewModel(),
    onGoHome: () -> Unit = {}
) {
    val foods by viewModel.pagedFoods.collectAsState()
    val sortConfig by viewModel.sortConfig.collectAsState()
    val currentPage by viewModel.currentPage.collectAsState()
    val totalPages by viewModel.totalPages.collectAsState()
    val detailState by viewModel.foodDetailState.collectAsState()
    var foodToDelete by remember { mutableStateOf<FoodEntity?>(null) }

    if (detailState.isLoading || detailState.detail != null || detailState.errorMessage != null) {
        FoodDetailDialog(
            state = detailState,
            onDismiss = { viewModel.clearFoodDetail() }
        )
    }

    foodToDelete?.let { food ->
        AlertDialog(
            onDismissRequest = { foodToDelete = null },
            title = { Text(stringResource(R.string.remove_food)) },
            text = { Text(stringResource(R.string.remove_food_confirm, food.description)) },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.deleteFood(food.id)
                    foodToDelete = null
                }) { Text(stringResource(R.string.remove)) }
            },
            dismissButton = {
                TextButton(onClick = { foodToDelete = null }) {
                    Text(stringResource(R.string.cancel))
                }
            }
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
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = stringResource(R.string.my_foods),
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(12.dp))
                SortBar(sortConfig = sortConfig, onSortChange = viewModel::setSortConfig)
                Spacer(modifier = Modifier.height(8.dp))
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
                    SavedFoodItem(
                        food = food,
                        onDetailsClick = { viewModel.fetchFoodDetail(food.id) },
                        onDeleteClick = { foodToDelete = food }
                    )
                    HorizontalDivider(modifier = Modifier.padding(top = 8.dp))
                }
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
                PaginationBar(
                    currentPage = currentPage,
                    totalPages = totalPages,
                    onPrev = viewModel::prevPage,
                    onNext = viewModel::nextPage
                )
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
private fun SortBar(sortConfig: SortConfig, onSortChange: (SortConfig) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(
            stringResource(R.string.sort_by),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        SortField.entries.forEach { field ->
            FilterChip(
                selected = sortConfig.field == field,
                onClick = {
                    onSortChange(
                        if (sortConfig.field == field)
                            sortConfig.copy(order = if (sortConfig.order == SortOrder.ASC) SortOrder.DESC else SortOrder.ASC)
                        else
                            sortConfig.copy(field = field, order = SortOrder.ASC)
                    )
                },
                label = { Text(field.label) },
                trailingIcon = if (sortConfig.field == field) {
                    {
                        Icon(
                            imageVector = if (sortConfig.order == SortOrder.ASC) Icons.Default.ArrowUpward else Icons.Default.ArrowDownward,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                } else null
            )
        }
    }
}

@Composable
private fun PaginationBar(
    currentPage: Int,
    totalPages: Int,
    onPrev: () -> Unit,
    onNext: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onPrev, enabled = currentPage > 0) {
            Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = "Previous page")
        }
        Text(
            text = "${currentPage + 1} / $totalPages",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
        IconButton(onClick = onNext, enabled = currentPage < totalPages - 1) {
            Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = "Next page")
        }
    }
}

@Composable
private fun SavedFoodItem(
    food: FoodEntity,
    onDetailsClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f),
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
            Spacer(modifier = Modifier.height(4.dp))
            OutlinedButton(onClick = onDetailsClick) {
                Text(stringResource(R.string.details))
            }
        }
        IconButton(onClick = onDeleteClick) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = stringResource(R.string.remove),
                tint = MaterialTheme.colorScheme.error
            )
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
