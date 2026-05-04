package cst.unibucfmi2026.utils

import cst.unibucfmi2026.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

data class FoodItem(
    val fdcId: Int,
    val description: String,
    val dataType: String?,
    val publicationDate: String?,
    val foodCategory: String?,
    val foodNutrients: List<FoodNutrient>?
)

data class FoodNutrient(
    val nutrientId: Int?,
    val nutrientName: String?,
    val nutrientNumber: String?,
    val unitName: String?,
    val value: Double?
)

data class AbridgedFoodItem(
    val fdcId: Int,
    val description: String,
    val dataType: String?,
    val publishedDate: String?,
    val brandOwner: String?,
    val foodNutrients: List<AbridgedFoodNutrient>?
)

data class AbridgedFoodNutrient(
    val nutrientId: Int?,
    val name: String?,
    val unitName: String?,
    val value: Double?
)

data class FoodSearchResult(
    val totalHits: Int,
    val currentPage: Int,
    val totalPages: Int,
    val foods: List<SearchResultFood>
)

data class SearchResultFood(
    val fdcId: Int,
    val description: String,
    val dataType: String?,
    val publishedDate: String?,
    val brandOwner: String?,
    val score: Double?,
    val foodNutrients: List<AbridgedFoodNutrient>?
)

private interface FdcApiService {
    @GET("food/{fdcId}")
    suspend fun getFood(
        @Path("fdcId") fdcId: Int,
        @Query("api_key") apiKey: String,
        @Query("format") format: String?,
        @Query("nutrients") nutrients: List<Int>?
    ): FoodItem

    @GET("foods")
    suspend fun getFoods(
        @Query("fdcIds") fdcIds: List<Int>,
        @Query("api_key") apiKey: String,
        @Query("format") format: String?,
        @Query("nutrients") nutrients: List<Int>?
    ): List<FoodItem>

    @GET("foods/list")
    suspend fun listFoods(
        @Query("api_key") apiKey: String,
        @Query("dataType") dataType: List<String>?,
        @Query("pageSize") pageSize: Int?,
        @Query("pageNumber") pageNumber: Int?,
        @Query("sortBy") sortBy: String?,
        @Query("sortOrder") sortOrder: String?
    ): List<AbridgedFoodItem>

    @GET("foods/search")
    suspend fun searchFoods(
        @Query("api_key") apiKey: String,
        @Query("query") query: String,
        @Query("dataType") dataType: List<String>?,
        @Query("pageSize") pageSize: Int?,
        @Query("pageNumber") pageNumber: Int?,
        @Query("sortBy") sortBy: String?,
        @Query("sortOrder") sortOrder: String?,
        @Query("brandOwner") brandOwner: String?
    ): FoodSearchResult
}

class FtcFoodAPI(private val apiKey: String = BuildConfig.FDC_API_KEY) {

    private val service: FdcApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.nal.usda.gov/fdc/v1/")
            .client(
                OkHttpClient.Builder()
                    .addInterceptor(HttpLoggingInterceptor().apply {
                        level = HttpLoggingInterceptor.Level.BASIC
                    })
                    .build()
            )
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(FdcApiService::class.java)
    }

    suspend fun getFood(
        fdcId: Int,
        format: String? = null,
        nutrients: List<Int>? = null
    ): FoodItem = service.getFood(fdcId, apiKey, format, nutrients)

    suspend fun getFoods(
        fdcIds: List<Int>,
        format: String? = null,
        nutrients: List<Int>? = null
    ): List<FoodItem> = service.getFoods(fdcIds, apiKey, format, nutrients)

    suspend fun listFoods(
        dataType: List<String>? = null,
        pageSize: Int? = null,
        pageNumber: Int? = null,
        sortBy: String? = null,
        sortOrder: String? = null
    ): List<AbridgedFoodItem> =
        service.listFoods(apiKey, dataType, pageSize, pageNumber, sortBy, sortOrder)

    suspend fun searchFoods(
        query: String,
        dataType: List<String>? = null,
        pageSize: Int? = null,
        pageNumber: Int? = null,
        sortBy: String? = null,
        sortOrder: String? = null,
        brandOwner: String? = null
    ): FoodSearchResult =
        service.searchFoods(apiKey, query, dataType, pageSize, pageNumber, sortBy, sortOrder, brandOwner)
}
