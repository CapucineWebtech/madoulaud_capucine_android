package fr.nextu.madoulaud_capucine

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import fr.nextu.madoulaud_capucine.entity.Cocktails

class CocktailAdapter(val cocktails: Cocktails) : RecyclerView.Adapter<CocktailAdapter.CoctailViewHolder>() {
    class CoctailViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.strDrink_cocktail)
        val imageView: ImageView = view.findViewById(R.id.strDrinkThumb_cocktail)
        val alcoholTag: ImageView = view.findViewById(R.id.alcohol_tag)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoctailViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cocktail_item, parent, false)
        return CoctailViewHolder(view)
    }

    override fun onBindViewHolder(holder: CoctailViewHolder, position: Int) {
        val cocktail = cocktails.drinks[position]
        holder.textView.text = cocktail.strDrink
        if (cocktail.isAlcoholic) {
            holder.alcoholTag.visibility = View.VISIBLE
        } else {
            holder.alcoholTag.visibility = View.GONE
        }

        Glide.with(holder.imageView.context)
            .load(cocktail.strDrinkThumb)
            .placeholder(R.drawable.placeholder)
            .error(R.drawable.error)
            .into(holder.imageView)
    }

    override fun getItemCount(): Int {
        return cocktails.drinks.size
    }
}


