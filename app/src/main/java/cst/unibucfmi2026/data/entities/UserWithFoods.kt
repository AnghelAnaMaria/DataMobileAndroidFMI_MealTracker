package cst.unibucfmi2026.data.entities

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class UserWithFoods(
    @Embedded
    val user: UserEntity,

    @Relation(
        parentColumn = UserEntity.ARG_ID,
        entityColumn = FoodEntity.ARG_ID,
        associateBy = Junction(
            value = UserFoodCrossRef::class,
            parentColumn = "user_id",
            entityColumn = "food_id"
        )
    )
    val foods: List<FoodEntity>
)
