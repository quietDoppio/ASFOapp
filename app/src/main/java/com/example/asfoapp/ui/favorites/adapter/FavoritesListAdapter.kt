package com.example.asfoapp.ui.favorites.adapter

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.asfoapp.data.Recipe
import com.example.asfoapp.databinding.ItemRecipeBinding
import com.example.asfoapp.interfaces.OnItemClickListener

class FavoritesListAdapter(private val dataSet: List<Recipe?>) :
    RecyclerView.Adapter<FavoritesListAdapter.FavoritesListViewHolder>() {
    private var itemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritesListViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemRecipeBinding.inflate(layoutInflater, parent, false)
        return FavoritesListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoritesListViewHolder, position: Int) {
        val item = dataSet[position]
        holder.bind(item, itemClickListener)
    }

    override fun getItemCount(): Int = dataSet.size
    class FavoritesListViewHolder(private val binding: ItemRecipeBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item: Recipe?, itemClickListener: OnItemClickListener?){
            item?.let {
            binding.tvRecipeName.text = item.title
            try {
                val inputStream = itemView.context.assets.open(item.imageUrl)
                val image = Drawable.createFromStream(inputStream, null)
                binding.imRecipeImage.setImageDrawable(image)
                binding.imRecipeImage.contentDescription = "$item.title"
            } catch (e: Exception) {
                val stackTrace = Log.getStackTraceString(e)
                Log.e(
                    "RecipesListAdapter",
                    "Image - ${item.imageUrl} not found in assets\n$stackTrace"
                )
            }
            binding.root.setOnClickListener {
                itemClickListener?.onItemClick(item.id)
            }
        }
        }
    }
}