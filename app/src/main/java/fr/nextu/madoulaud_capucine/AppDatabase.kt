package fr.nextu.madoulaud_capucine

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import fr.nextu.madoulaud_capucine.entity.Cocktail

@Database(entities = [Cocktail::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cocktailDao(): CocktailDAO

    companion object {
        fun getInstance(applicationContext: Context): AppDatabase {
            return Room.databaseBuilder(
                applicationContext,
                AppDatabase::class.java,
                "app_android_kotlin.db"
            )
                .build()
        }
    }
}