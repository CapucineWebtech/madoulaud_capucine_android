package fr.nextu.madoulaud_capucine

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.google.gson.Gson
import fr.nextu.madoulaud_capucine.databinding.FragmentSecondBinding
import fr.nextu.madoulaud_capucine.db.AppDatabase
import fr.nextu.madoulaud_capucine.entity.Cocktail
import fr.nextu.madoulaud_capucine.entity.Cocktails
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okio.IOException

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment() {

    private var _binding: FragmentSecondBinding? = null
    private lateinit var db: AppDatabase
    private lateinit var cocktailId: String

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        db = AppDatabase.getInstance(requireContext())
        arguments?.let {
            val args = SecondFragmentArgs.fromBundle(it)
            cocktailId = args.cocktailId
        }
        displayCocktailDetailsFromDb()
        fetchCocktailDetails()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun fetchCocktailDetails() {
        val client = OkHttpClient()
        val url = "https://www.thecocktaildb.com/api/json/v1/1/lookup.php?i=$cocktailId"
        val request = Request.Builder().url(url).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("SecondFragment", "Error fetching cocktail details", e)
            }

            override fun onResponse(call: Call, response: Response) {
                response.body?.string()?.let { jsonResponse ->
                    updateCocktailDetails(jsonResponse)
                }
            }
        })
    }

    private fun updateCocktailDetails(jsonResponse: String) {
        val gson = Gson()
        val cocktailDetails = gson.fromJson(jsonResponse, Cocktails::class.java)

        lifecycleScope.launch(Dispatchers.IO) {
            cocktailDetails.drinks.forEach {
                it.isAlcoholic = it.strAlcoholic == "Alcoholic"
                saveCocktail(it)
            }
            withContext(Dispatchers.Main) {
                displayCocktailDetailsFromDb()
            }
        }
    }

    private fun displayCocktailDetailsFromDb() {
        lifecycleScope.launch(Dispatchers.IO) {
            db.cocktailDao().getById(cocktailId).let { cocktail ->
                withContext(Dispatchers.Main) {
                    binding.strDrinkCocktail.text = cocktail.strDrink
                    Glide.with(binding.strDrinkThumbCocktail.context)
                        .load(cocktail.strDrinkThumb)
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.error)
                        .into(binding.strDrinkThumbCocktail)
                    binding.strGlassCocktail.text = cocktail.strGlass
                    binding.isAlcoholicCocktail.text = if (cocktail.isAlcoholic) "Yes" else "No"
                    binding.strIngredientCocktail.text = cocktail.formattedIngredientsAndMeasures()
                    binding.strInstructionsCocktail.text = cocktail.strInstructions
                    binding.dateModifiedCocktail.text = cocktail.dateModified?.split(" ")?.get(0) ?: ""
                }
            }
        }
    }

    private fun saveCocktail(cocktail: Cocktail) {
        val id = db.cocktailDao().insertCocktail(cocktail)
        if (id == -1L) {
            db.cocktailDao().updateCocktail(cocktail)
        }
    }
}
