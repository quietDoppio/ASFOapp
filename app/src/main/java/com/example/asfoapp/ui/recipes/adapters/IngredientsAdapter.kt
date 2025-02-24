package com.example.asfoapp.ui.recipes.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.asfoapp.data.Ingredient
import com.example.asfoapp.databinding.ItemIngredientBinding

class IngredientsAdapter(private val dataSet: List<Ingredient>) : Adapter<IngredientsAdapter.IngredientsItemViewHolder>(){
    class IngredientsItemViewHolder(private val binding: ItemIngredientBinding) : ViewHolder(binding.root){
        fun bind(item: Ingredient){
            binding.cuantity.text = "%s ".format(item.quantity)
            binding.unitOfMeasure.text = item.unitOfMeasure
            binding.ingredient.text = item.description
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientsItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemIngredientBinding.inflate(layoutInflater, parent, false)
        return IngredientsItemViewHolder(binding)
    }

    override fun getItemCount(): Int = dataSet.size

    override fun onBindViewHolder(holder: IngredientsItemViewHolder, position: Int) {
       val item = dataSet[position]
        holder.bind(item)
    }
}