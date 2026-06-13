package cst.unibucfmi2026.utils

import cst.unibucfmi2026.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

private interface FdcApiService { //Retrofit API interface
    @GET("food/{fdcId}")
    suspend fun getFood(
        @Path("fdcId") fdcId: Int,
        @Query("api_key") apiKey: String,
        @Query("format") format: String?,
        @Query("nutrients") nutrients: List<Int>?
    ): FoodItemDetail

    @GET("foods/search")
    suspend fun searchFoods(
        @Query("api_key") apiKey: String,
        @Query("query") query: String,
        @Query("dataType") dataType: List<String>?,
        @Query("pageSize") pageSize: Int?,
        @Query("pageNumber") pageNumber: Int?,
        @Query("sortBy") sortBy: String?,
        @Query("sortOrder") sortOrder: String?,
        @Query("brandOwner") brandOwner: String?,
        @Query("tradeChannel") tradeChannel: List<String>?,
        @Query("startDate") startDate: String?,
        @Query("endDate") endDate: String?
    ): FoodSearchResult
}

class FtcFoodAPI(private val apiKey: String = BuildConfig.FDC_API_KEY) { //Wrapper around Retrofit

    private val service: FdcApiService by lazy {
        Retrofit.Builder() //Retrofit for http; implements interface above
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
    ): FoodItemDetail = service.getFood(fdcId, apiKey, format, nutrients)

    suspend fun searchFoods(
        query: String,
        dataType: List<String>? = null,
        pageSize: Int? = null,
        pageNumber: Int? = null,
        sortBy: String? = null,
        sortOrder: String? = null,
        brandOwner: String? = null,
        tradeChannel: List<String>? = null,
        startDate: String? = null,
        endDate: String? = null
    ): FoodSearchResult =
        service.searchFoods(
            apiKey, query, dataType, pageSize, pageNumber,
            sortBy, sortOrder, brandOwner, tradeChannel, startDate, endDate
        )
}
