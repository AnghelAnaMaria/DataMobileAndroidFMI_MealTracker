package cst.unibucfmi2026.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import cst.unibucfmi2026.data.dao.FoodDAO

import cst.unibucfmi2026.data.dao.UserDAO
import cst.unibucfmi2026.data.dao.UserFoodCrossRefDAO
import cst.unibucfmi2026.data.entities.FoodEntity
import cst.unibucfmi2026.data.entities.UserEntity
import cst.unibucfmi2026.data.entities.UserFoodCrossRef

@Database(
    entities = [UserEntity::class, FoodEntity::class, UserFoodCrossRef::class],
    version = 3
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao () : UserDAO
    abstract fun foodDao () : FoodDAO

    abstract fun userFoodCrossRefDao(): UserFoodCrossRefDAO

    companion object {
//        private val migration_1_2 = object : Migration(startVersion = 1, endVersion = 2) {
//            override fun migrate(db: SupportSQLiteDatabase) {
//                db.execSQL("ALTER TABLE users RENAME COLUMN 'firstName' TO 'first_name'")
//                db.execSQL("ALTER TABLE users RENAME COLUMN 'lastName' TO 'last_name'")
//            }
//        }
        @Volatile
        private var instance : AppDatabase? = null

        fun getInstance(context: Context) : AppDatabase {
            return instance ?: synchronized(this) { // only one thread creates the DB
                val newInstanceConfig = Room.databaseBuilder(
                    context = context,
                    name = "meal_tracker.db",
                    klass = AppDatabase::class.java
                )
                    .fallbackToDestructiveMigration(true)

                val newInstance = newInstanceConfig.build()
                instance = newInstance
                newInstance
            }
        }
    }
}