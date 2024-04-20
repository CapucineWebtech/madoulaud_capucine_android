package fr.nextu.madoulaud_capucine

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import com.google.gson.Gson
import fr.nextu.madoulaud_capucine.databinding.ActivityMainBinding
import fr.nextu.madoulaud_capucine.entity.Cocktails
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request

class ListActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    lateinit var db: AppDatabase
    var listType: String = "Complete"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        db = AppDatabase.getInstance(applicationContext)

        listType = intent.getStringExtra("listType") ?: "Complete"
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
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
        cocktailList.drinks.forEach { it.isAlcoholic = isAlcoholic }
        db.cocktailDao().insertAll(*cocktailList.drinks.toTypedArray())
    }
}