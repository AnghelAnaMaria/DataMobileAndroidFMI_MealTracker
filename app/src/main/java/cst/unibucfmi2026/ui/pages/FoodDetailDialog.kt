package cst.unibucfmi2026.ui.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cst.unibucfmi2026.R
import cst.unibucfmi2026.viewmodel.FoodDetailState

@Composable
fun FoodDetailDialog(state: FoodDetailState, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(state.detail?.description ?: stringResource(R.string.food_details))
        },
        text = {
            when {
                state.isLoading -> Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
                state.errorMessage != null -> Text(
                    state.errorMessage,
                    color = MaterialTheme.colorScheme.error
                )
                state.detail != null -> {
                    val detail = state.detail
                    Column(
                        modifier = Modifier.verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        detail.brandOwner?.let {
                            Text(
                                it,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.secondary
                            )
                        }
                        detail.servingSize?.let { size ->
                            val unit = detail.servingSizeUnit ?: ""
                            Text(
                                "${stringResource(R.string.serving)}: $size $unit".trim(),
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                        detail.ingredients?.let { ingredients ->
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                stringResource(R.string.ingredients),
                                fontWeight = FontWeight.SemiBold,
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(ingredients, style = MaterialTheme.typography.bodySmall)
                        }
                        detail.foodNutrients?.takeIf { it.isNotEmpty() }?.let { nutrients ->
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                stringResource(R.string.nutrients),
                                fontWeight = FontWeight.SemiBold,
                                style = MaterialTheme.typography.bodyMedium
                            )
                            nutrients.forEach { fn ->
                                val name = fn.nutrient?.name ?: return@forEach
                                val amount = fn.amount ?: return@forEach
                                val unit = fn.nutrient.unitName ?: ""
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(name, style = MaterialTheme.typography.bodySmall)
                                    Text(
                                        "$amount $unit".trim(),
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.close))
            }
        }
    )
}