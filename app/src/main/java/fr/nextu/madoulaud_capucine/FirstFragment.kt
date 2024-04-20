package fr.nextu.madoulaud_capucine

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import fr.nextu.madoulaud_capucine.databinding.FragmentFirstBinding
import fr.nextu.madoulaud_capucine.entity.Cocktails
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null
    private val binding get() = _binding!!

    lateinit var cocktail_recycler : RecyclerView
    lateinit var db : AppDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonFirst.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }

        cocktail_recycler = binding.listCocktail.apply{
            adapter = CocktailAdapter(Cocktails(emptyList()))
            layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this@FirstFragment.context)
        }

        db = AppDatabase.getInstance(requireContext())

    }

    override fun onStart() {
        super.onStart()
        val listType = (activity as? ListActivity)?.listType ?: "Complete"
        updateViewFromDB(listType)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun updateViewFromDB(listType: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val flow = when (listType) {
                "Alcoholic" -> db.cocktailDao().getAlcoholicCocktails()
                "Non_Alcoholic" -> db.cocktailDao().getNonAlcoholicCocktails()
                else -> db.cocktailDao().getFlowData()
            }
            flow.collect { cocktailsList ->
                val mutableCocktailsList = cocktailsList.toMutableList()
                mutableCocktailsList.shuffle()
                CoroutineScope(Dispatchers.Main).launch {
                    cocktail_recycler.adapter = CocktailAdapter(Cocktails(mutableCocktailsList))
                }
            }
        }
    }
}