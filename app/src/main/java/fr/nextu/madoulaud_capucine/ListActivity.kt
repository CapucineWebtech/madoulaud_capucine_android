package fr.nextu.madoulaud_capucine

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.gson.Gson
import fr.nextu.madoulaud_capucine.databinding.ActivityMainBinding
import fr.nextu.madoulaud_capucine.db.AppDatabase
import fr.nextu.madoulaud_capucine.entity.Cocktail
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

        createNotificationChannel()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_home -> {
                val intent = Intent(this, FirstActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.alcoholic_cocktails -> {
                updateListTypeAndRefresh("Alcoholic")
                true
            }
            R.id.non_alcoholic_cocktails -> {
                updateListTypeAndRefresh("Non_Alcoholic")
                true
            }
            R.id.all_cocktails -> {
                updateListTypeAndRefresh("Complete")
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun updateListTypeAndRefresh(newListType: String) {
        if (listType != newListType) {
            listType = newListType
            refreshFragment()
        }
    }

    private fun refreshFragment() {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        navController.navigate(R.id.FirstFragment)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    override fun onStart(){
        super.onStart()
        getCocktailList()
        if(listType == "Alcoholic" || listType == "Complete"){
            createAlcoholWarningNotification()
        }
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

    private fun createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val name = "Attention to alcohol"
            val descriptionText = "When the user clicks to view alcoholic cocktails."
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    companion object{
        const val CHANNEL_ID = "app_fr_nextu_madoulaud_capucine"
    }

    private fun createAlcoholWarningNotification() {
        val intent = Intent(this, ListActivity::class.java).apply {
            putExtra("listType", "Non_Alcoholic")
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Caution with alcohol")
            .setContentText("Excessive alcohol consumption is harmful to health.")
            .setStyle(NotificationCompat.BigTextStyle().bigText("Excessive alcohol consumption can cause serious health problems and dependencies. Consider consuming in moderation or opt for non-alcoholic alternatives."))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .addAction(R.drawable.alcohol, "View non-alcoholic cocktails", pendingIntent)

        with(NotificationManagerCompat.from(this)) {
            if (ActivityCompat.checkSelfPermission(this@ListActivity, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(Manifest.permission.POST_NOTIFICATIONS), 1)
                return@with
            }
            notify(1, builder.build())
        }
    }
}