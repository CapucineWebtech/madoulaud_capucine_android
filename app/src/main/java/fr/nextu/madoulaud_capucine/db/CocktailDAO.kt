package fr.nextu.madoulaud_capucine.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import fr.nextu.madoulaud_capucine.entity.Cocktail
import kotlinx.coroutines.flow.Flow

@Dao
interface CocktailDAO {
    @Query("SELECT * FROM cocktail")
    fun getAll(): List<Cocktail>

    @Query("SELECT * FROM cocktail WHERE idDrink = :id")
    fun getById(id: String): Cocktail

    //insert or update
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg cocktails: Cocktail)

    @Update
    fun updateCocktail(cocktail: Cocktail)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertCocktail(cocktail: Cocktail): Long

    @Delete
    fun delete(cocktail: Cocktail)

    @Query("SELECT * FROM cocktail")
    fun getFlowData(): Flow<List<Cocktail>>

    @Query("SELECT * FROM cocktail WHERE isAlcoholic = 1")
    fun getAlcoholicCocktails(): Flow<List<Cocktail>>

    @Query("SELECT * FROM cocktail WHERE isAlcoholic = 0")
    fun getNonAlcoholicCocktails(): Flow<List<Cocktail>>
}