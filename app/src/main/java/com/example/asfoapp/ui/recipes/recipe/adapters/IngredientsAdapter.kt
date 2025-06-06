package com.example.asfoapp.ui.recipes.recipe.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.text.isDigitsOnly
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.asfoapp.model.Ingredient
import com.example.asfoapp.databinding.ItemIngredientBinding

class IngredientsAdapter(dataSet: List<Ingredient> = emptyList()) :
    Adapter<IngredientsAdapter.IngredientsItemViewHolder>() {

    private var dataSet: List<Ingredient> = dataSet
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    private var defaultQuantities = dataSet.map { it.quantity }

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
    fun setData(data: List<Ingredient>){
        val newQuantities = data.map { it.quantity }
        if(defaultQuantities != newQuantities) {
            dataSet = data
            defaultQuantities = newQuantities
        }
    }
    fun updateIngredientsQuantity(progress: Int) {

        val newIngredients = dataSet.mapIndexed { index, ingredient ->
            if (defaultQuantities[index].isDigitsOnly()) {
                ingredient.copy(quantity = (defaultQuantities[index].toInt() * progress).toString())
            } else if(defaultQuantities[index].toDoubleOrNull() != null){
                ingredient.copy(quantity = (defaultQuantities[index].toDouble() * progress).toString())
            } else {
                ingredient
            }
        }
        dataSet = newIngredients
    }

    class IngredientsItemViewHolder(private val binding: ItemIngredientBinding) :
        ViewHolder(binding.root) {
        fun bind(item: Ingredient) {
            binding.cuantity.text = "%s ".format(item.quantity)
            binding.unitOfMeasure.text = item.unitOfMeasure
            binding.ingredient.text = item.description
        }
    }
}