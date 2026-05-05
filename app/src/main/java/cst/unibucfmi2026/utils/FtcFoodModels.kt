package cst.unibucfmi2026.utils

data class Nutrient(
    val name: String?,
    val unitName: String?
)

data class FoodNutrient(
    val id: Int,
    val amount: Float?,
    val nutrient: Nutrient?
)

data class AbridgedFoodNutrient(
    val number: Int?,
    val name: String?,
    val amount: Float?,
    val unitName: String?
)

data class FoodItemDetail(
    val fdcId: Int,
    val description: String,
    val brandOwner: String?,
    val ingredients: String?,
    val servingSize: Float?,
    val servingSizeUnit: String?,
    val foodNutrients: List<FoodNutrient>?
)

data class SearchResultFood(
    val fdcId: Int,
    val description: String,
    val brandOwner: String?,
    val score: Float?,
    val foodNutrients: List<AbridgedFoodNutrient>?
)

data class FoodSearchResult(
    val totalHits: Int,
    val currentPage: Int,
    val totalPages: Int,
    val foods: List<SearchResultFood>
)
