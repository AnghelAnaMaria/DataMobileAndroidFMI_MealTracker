package cst.unibucfmi2026.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "foods")
data class FoodEntity(
    @PrimaryKey
    @ColumnInfo(name = ARG_ID)
    val id : Int, //we use the id from the government nutrition database
    @ColumnInfo(name="description")
    val description : String,
    @ColumnInfo(name="brand_owner")
    val brandOwner : String?,
    @ColumnInfo(name = "calories")
    val calories: Float?,
    @ColumnInfo(name = "protein")
    val protein: Float?,
    @ColumnInfo(name = "fat")
    val fat: Float?,
    @ColumnInfo(name = "carbs")
    val carbs: Float?
    ) {
    companion object {
        const val ARG_ID = "id"
    }
}
