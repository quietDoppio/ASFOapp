package com.example.asfoapp.recycler

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.asfoapp.R
import com.example.asfoapp.data.Category
import com.example.asfoapp.databinding.ItemCategoryBinding
import java.io.InputStream

class CategoriesListAdapter(private val data: List<Category>) :
    Adapter<CategoriesListAdapter.CategoryItemViewHolder>() {

    class CategoryItemViewHolder(private val binding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        companion object {
            fun inflateFrom(parent: ViewGroup): CategoryItemViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemCategoryBinding.inflate(layoutInflater, parent, false)
                return CategoryItemViewHolder(binding)
            }
        }

        fun bind(item: Category) {
            binding.cardViewTitle.text = item.title
            binding.cardViewDescription.text = item.description
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryItemViewHolder {
        return CategoryItemViewHolder.inflateFrom(parent)
    }

    override fun onBindViewHolder(holder: CategoryItemViewHolder, position: Int) {
        holder.bind(data[position])

    }

    override fun getItemCount(): Int {
        return data.size
    }

}
