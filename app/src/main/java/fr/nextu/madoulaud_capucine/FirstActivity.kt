package fr.nextu.madoulaud_capucine

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.gson.Gson
import fr.nextu.madoulaud_capucine.databinding.ActivityFirstBinding
import fr.nextu.madoulaud_capucine.db.AppDatabase
import fr.nextu.madoulaud_capucine.entity.Cocktail
import fr.nextu.madoulaud_capucine.entity.Cocktails
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request

class FirstActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFirstBinding
    lateinit var db: AppDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_first)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding = ActivityFirstBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonViewCompleteList.setOnClickListener {
            val intent = Intent(this, ListActivity::class.java)
            intent.putExtra("listType", "Complete")
            startActivity(intent)
        }

        binding.buttonViewAlcoholicList.setOnClickListener {
            val intent = Intent(this, ListActivity::class.java)
            intent.putExtra("listType", "Alcoholic")
            startActivity(intent)
        }

        binding.buttonViewNonAlcoholicList.setOnClickListener {
            val intent = Intent(this, ListActivity::class.java)
            intent.putExtra("listType", "Non_Alcoholic")
            startActivity(intent)
        }

        db = AppDatabase.getInstance(applicationContext)
    }

    override fun onStart(){
        super.onStart()
        getCocktailList()
    }

    fun getCocktailList() {
        getSpecificCocktailList("Non_Alcoholic", false)
        getSpecificCocktailList("Alcoholic", true)
    }

    private fun getSpecificCocktailList(type: String, isAlcoholic: Boolean) {
        CoroutineScope(Dispatchers.IO).launch {
            requestCocktailList(type) { jsonResponse ->
                cocktailsFromJson(jsonResponse, isAlcoholic)
            }
        }
    }

    private fun requestCocktailList(type: String, callback: (String) -> Unit) {
        val client = OkHttpClient()
        val request: Request = Request.Builder()
            .url("https://www.thecocktaildb.com/api/json/v1/1/filter.php?a=$type")
            .get()
            .build()

        client.newCall(request).execute().use { response ->
            callback(response.body?.string() ?: "")
        }
    }

    private fun cocktailsFromJson(jsonString: String, isAlcoholic: Boolean) {
        val gson = Gson()
        val cocktailList = gson.fromJson(jsonString, Cocktails::class.java)
        cocktailList.drinks.forEach {
            it.isAlcoholic = isAlcoholic
            saveCocktail(it)
        }
    }

    private fun saveCocktail(cocktail: Cocktail) {
        val id = db.cocktailDao().insertCocktail(cocktail)
        if (id == -1L) {
            mergeAndUpdateCocktail(cocktail)
        }
    }

    private fun mergeAndUpdateCocktail(newCocktail: Cocktail) {
        val existingCocktail = db.cocktailDao().getById(newCocktail.idDrink.toString())
        existingCocktail.strDrink = newCocktail.strDrink
        existingCocktail.strDrinkThumb = newCocktail.strDrinkThumb
        existingCocktail.isAlcoholic = newCocktail.isAlcoholic
        db.cocktailDao().updateCocktail(existingCocktail)
    }
}