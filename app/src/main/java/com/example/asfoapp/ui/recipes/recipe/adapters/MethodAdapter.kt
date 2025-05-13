package com.example.asfoapp.ui.recipes.recipe.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.asfoapp.databinding.ItemMethodBinding

class MethodAdapter(dataSet: List<String> = emptyList()) :
    Adapter<MethodAdapter.MethodItemViewHolder>() {

    private var dataSet: List<String> = dataSet
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MethodItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemMethodBinding.inflate(layoutInflater, parent, false)
        return MethodItemViewHolder(binding)
    }

    override fun getItemCount(): Int = dataSet.size

    override fun onBindViewHolder(holder: MethodItemViewHolder, position: Int) {
        val item = dataSet[position]
        holder.bind(item)
    }
    fun setData(data: List<String>){
        if(dataSet != data)
        dataSet = data
    }
    class MethodItemViewHolder(private val binding: ItemMethodBinding) : ViewHolder(binding.root) {
        fun bind(item: String) {
            binding.method.text = item
        }
    }
}