package com.example.asfoapp.ui.recipes.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.asfoapp.databinding.ItemMethodBinding

class MethodAdapter(private val dataSet: List<String>) :
    Adapter<MethodAdapter.MethodItemViewHolder>() {

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

    class MethodItemViewHolder(private val binding: ItemMethodBinding) : ViewHolder(binding.root) {
        fun bind(item: String) {
            binding.method.text = item
        }
    }
}