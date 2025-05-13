package com.example.asfoapp.ui.recipes

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.asfoapp.model.Recipe
import com.example.asfoapp.databinding.ItemRecipeBinding
import com.example.asfoapp.interfaces.OnItemClickListener

class RecipesListAdapter(dataSet: List<Recipe> = emptyList()) :
    Adapter<RecipesListAdapter.RecipeItemViewHolder>() {

    private var dataSet: List<Recipe> = dataSet
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    private var itemClickListener: OnItemClickListener? = null
    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemRecipeBinding.inflate(layoutInflater, parent, false)
        return RecipeItemViewHolder(binding)
    }

    override fun getItemCount(): Int = dataSet.size

    override fun onBindViewHolder(holder: RecipeItemViewHolder, position: Int) {
        val item = dataSet[position]
        holder.bind(item, itemClickListener)
    }
    fun setData(data: List<Recipe>){
        if(data != dataSet) dataSet = data
    }
    class RecipeItemViewHolder(private val binding: ItemRecipeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Recipe, itemClickListener: OnItemClickListener?) {
            binding.tvRecipeName.text = item.title
            try {
                val inputStream = itemView.context.assets.open(item.imageUrl)
                val image = Drawable.createFromStream(inputStream, null)
                binding.ivRecipeImage.setImageDrawable(image)
                binding.ivRecipeImage.contentDescription = "$item.title"
            } catch (e: Exception) {
                val stackTrace = Log.getStackTraceString(e)
                Log.e(
                    "RecipeListAdapter",
                    "Image - ${item.imageUrl} not found in assets\n$stackTrace"
                )
            }
            binding.root.setOnClickListener {
                itemClickListener?.onItemClick(item.id)
            }
        }
    }
}