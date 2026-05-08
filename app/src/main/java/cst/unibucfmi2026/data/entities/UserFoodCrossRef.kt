package cst.unibucfmi2026.data.entities
import androidx.room.ColumnInfo
import androidx.room.Entity


@Entity(
    tableName = "user_food_cross_ref",
    primaryKeys = ["user_id", "food_id"]
)
data class UserFoodCrossRef(
    @ColumnInfo(name = "user_id")
    val userId: String, // Firebase UID

    @ColumnInfo(name = "food_id")
    val foodId: Int
)