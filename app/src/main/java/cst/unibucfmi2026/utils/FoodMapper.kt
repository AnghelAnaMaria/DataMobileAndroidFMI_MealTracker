package cst.unibucfmi2026.utils

import cst.unibucfmi2026.data.entities.FoodEntity

fun SearchResultFood.toEntity(): FoodEntity {

    val nutrients = foodNutrients ?: emptyList()

    val map = nutrients.associateBy { it.number }

    val calories = map[1008]?.amount
    val protein = map[1003]?.amount
    val fat = map[1004]?.amount
    val carbs = map[1005]?.amount

    return FoodEntity(
        id = fdcId,
        description = description,
        brandOwner = brandOwner,
        calories = calories,
        protein = protein,
        fat = fat,
        carbs = carbs
    )
}