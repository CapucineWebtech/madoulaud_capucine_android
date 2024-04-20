package fr.nextu.madoulaud_capucine.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Cocktail")
class Cocktail (
    @PrimaryKey val idDrink: Int,
    @ColumnInfo(name = "strDrink") var strDrink: String,
    @ColumnInfo(name = "strDrinkThumb") var strDrinkThumb: String,
    @ColumnInfo(name = "isAlcoholic") var isAlcoholic: Boolean,
){
    @ColumnInfo(name = "strGlass", defaultValue = "") var strGlass: String? = ""
        set(value) {
            field = value ?: ""
        }
    @ColumnInfo(name = "strInstructions", defaultValue = "") var strInstructions: String? = ""
        set(value) {
            field = value ?: ""
        }
    @ColumnInfo(name = "strIngredient1", defaultValue = "") var strIngredient1: String? = ""
        set(value) {
            field = value ?: ""
        }
    @ColumnInfo(name = "strIngredient2", defaultValue = "") var strIngredient2: String? = ""
        set(value) {
            field = value ?: ""
        }
    @ColumnInfo(name = "strIngredient3", defaultValue = "") var strIngredient3: String? = ""
        set(value) {
            field = value ?: ""
        }
    @ColumnInfo(name = "strIngredient4", defaultValue = "") var strIngredient4: String? = ""
        set(value) {
            field = value ?: ""
        }
    @ColumnInfo(name = "strIngredient5", defaultValue = "") var strIngredient5: String? = ""
        set(value) {
            field = value ?: ""
        }
    @ColumnInfo(name = "strIngredient6", defaultValue = "") var strIngredient6: String? = ""
        set(value) {
            field = value ?: ""
        }
    @ColumnInfo(name = "strIngredient7", defaultValue = "") var strIngredient7: String? = ""
        set(value) {
            field = value ?: ""
        }
    @ColumnInfo(name = "strIngredient8", defaultValue = "") var strIngredient8: String? = ""
        set(value) {
            field = value ?: ""
        }
    @ColumnInfo(name = "strIngredient9", defaultValue = "") var strIngredient9: String? = ""
        set(value) {
            field = value ?: ""
        }
    @ColumnInfo(name = "strIngredient10", defaultValue = "") var strIngredient10: String? = ""
        set(value) {
            field = value ?: ""
        }
    @ColumnInfo(name = "strIngredient11", defaultValue = "") var strIngredient11: String? = ""
        set(value) {
            field = value ?: ""
        }
    @ColumnInfo(name = "strIngredient12", defaultValue = "") var strIngredient12: String? = ""
        set(value) {
            field = value ?: ""
        }
    @ColumnInfo(name = "strIngredient13", defaultValue = "") var strIngredient13: String? = ""
        set(value) {
            field = value ?: ""
        }
    @ColumnInfo(name = "strIngredient14", defaultValue = "") var strIngredient14: String? = ""
        set(value) {
            field = value ?: ""
        }
    @ColumnInfo(name = "strIngredient15", defaultValue = "") var strIngredient15: String? = ""
        set(value) {
            field = value ?: ""
        }
    @ColumnInfo(name = "strMeasure1", defaultValue = "") var strMeasure1: String? = ""
        set(value) {
            field = value ?: ""
        }
    @ColumnInfo(name = "strMeasure2", defaultValue = "") var strMeasure2: String? = ""
        set(value) {
            field = value ?: ""
        }
    @ColumnInfo(name = "strMeasure3", defaultValue = "") var strMeasure3: String? = ""
        set(value) {
            field = value ?: ""
        }
    @ColumnInfo(name = "strMeasure4", defaultValue = "") var strMeasure4: String? = ""
        set(value) {
            field = value ?: ""
        }
    @ColumnInfo(name = "strMeasure5", defaultValue = "") var strMeasure5: String? = ""
        set(value) {
            field = value ?: ""
        }
    @ColumnInfo(name = "strMeasure6", defaultValue = "") var strMeasure6: String? = ""
        set(value) {
            field = value ?: ""
        }
    @ColumnInfo(name = "strMeasure7", defaultValue = "") var strMeasure7: String? = ""
        set(value) {
            field = value ?: ""
        }
    @ColumnInfo(name = "strMeasure8", defaultValue = "") var strMeasure8: String? = ""
        set(value) {
            field = value ?: ""
        }
    @ColumnInfo(name = "strMeasure9", defaultValue = "") var strMeasure9: String? = ""
        set(value) {
            field = value ?: ""
        }
    @ColumnInfo(name = "strMeasure10", defaultValue = "") var strMeasure10: String? = ""
        set(value) {
            field = value ?: ""
        }
    @ColumnInfo(name = "strMeasure11", defaultValue = "") var strMeasure11: String? = ""
        set(value) {
            field = value ?: ""
        }
    @ColumnInfo(name = "strMeasure12", defaultValue = "") var strMeasure12: String? = ""
        set(value) {
            field = value ?: ""
        }
    @ColumnInfo(name = "strMeasure13", defaultValue = "") var strMeasure13: String? = ""
        set(value) {
            field = value ?: ""
        }
    @ColumnInfo(name = "strMeasure14", defaultValue = "") var strMeasure14: String? = ""
        set(value) {
            field = value ?: ""
        }
    @ColumnInfo(name = "strMeasure15", defaultValue = "") var strMeasure15: String? = ""
        set(value) {
            field = value ?: ""
        }
    @ColumnInfo(name = "dateModified", defaultValue = "") var dateModified: String? = null
        set(value) {
            field = value ?: ""
        }
    @ColumnInfo(name = "strAlcoholic", defaultValue = "") var strAlcoholic: String? = ""
        set(value) {
            field = value ?: ""
        }

    fun formattedIngredientsAndMeasures(): String = buildString {
        val ingredients = listOf(strIngredient1, strIngredient2, strIngredient3, strIngredient4, strIngredient5, strIngredient6, strIngredient7, strIngredient8, strIngredient9, strIngredient10, strIngredient11, strIngredient12, strIngredient13, strIngredient14, strIngredient15)
        val measures = listOf(strMeasure1, strMeasure2, strMeasure3, strMeasure4, strMeasure5, strMeasure6, strMeasure7, strMeasure8, strMeasure9, strMeasure10, strMeasure11, strMeasure12, strMeasure13, strMeasure14, strMeasure15)
        ingredients.zip(measures).forEach { (ingredient, measure) ->
            if (!ingredient.isNullOrEmpty() && !measure.isNullOrEmpty()) {
                append("$ingredient - $measure\n")
            }
        }
    }
}