package com.example.asfoapp.presentation.screens.recipes

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.bumptech.glide.Glide
import com.example.asfoapp.R
import com.example.asfoapp.domain.models.Recipe
import com.example.asfoapp.databinding.ItemRecipeBinding
import com.example.asfoapp.presentation.interfaces.OnItemClickListener

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

    fun setData(data: List<Recipe>) {
        if (data != dataSet) dataSet = data
    }

    class RecipeItemViewHolder(private val binding: ItemRecipeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Recipe, itemClickListener: OnItemClickListener?) {
            binding.tvRecipeName.text = item.title
            Glide.with(binding.root)
                .load(item.imageUrl)
                .error(R.drawable.img_error)
                .placeholder(R.drawable.img_placeholder)
                .into(binding.ivRecipeImage)
            binding.root.setOnClickListener {
                itemClickListener?.onItemClick(item.recipeId)
            }
        }
    }
}
