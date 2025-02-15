package com.example.asfoapp.recycler

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.asfoapp.R
import com.example.asfoapp.data.Category
import com.example.asfoapp.databinding.ItemCategoryBinding

class CategoriesListAdapter(private val dataSet: List<Category>) :
    Adapter<CategoriesListAdapter.CategoryItemViewHolder>() {

    class CategoryItemViewHolder(binding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val cardImage = binding.cardViewImage
        private val cardTitle = binding.cardViewTitle
        private val cardDescription = binding.cardViewDescription

        companion object {
            fun inflateFrom(parent: ViewGroup): CategoryItemViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemCategoryBinding.inflate(layoutInflater, parent, false)
                return CategoryItemViewHolder(binding)
            }
        }

        fun bind(item: Category) {
            cardTitle.text = item.title
            cardDescription.text = item.description
            try {
                cardImage.setImageDrawable(
                    Drawable.createFromStream(itemView.context.assets.open(item.imageUrl), null)
                )
            } catch (e: Exception) {
                Log.i("CategoriesListAdapter", "bind: image for ${item.imageUrl} not found")
                cardImage.setImageResource(R.drawable.img_error)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryItemViewHolder {
        return CategoryItemViewHolder.inflateFrom(parent)
    }

    override fun onBindViewHolder(holder: CategoryItemViewHolder, position: Int) {
        val item = dataSet[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

}
