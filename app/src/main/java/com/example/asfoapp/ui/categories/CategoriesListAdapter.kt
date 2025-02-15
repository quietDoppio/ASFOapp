package com.example.asfoapp.ui.categories

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.asfoapp.data.Category
import com.example.asfoapp.databinding.ItemCategoryBinding

class CategoriesListAdapter(private val dataSet: List<Category>) :
    Adapter<CategoriesListAdapter.CategoryItemViewHolder>() {

    class CategoryItemViewHolder(private val binding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Category) {
            binding.cardViewTitle.text = item.title
            binding.cardViewDescription.text = item.description
            try {
                val inputStream = itemView.context.assets.open(item.imageUrl)
                val image = Drawable.createFromStream(inputStream, null)
                binding.cardViewImage.setImageDrawable(image)
                binding.cardViewImage.contentDescription = "${item.title} - ${item.description}"
            } catch (e: Exception) {
                val stackTrace = Log.getStackTraceString(e)
                Log.e(
                    "CategoriesListAdapter",
                    "Image - ${item.imageUrl} not found in assets\n$stackTrace"
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemCategoryBinding.inflate(layoutInflater, parent, false)
        return CategoryItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryItemViewHolder, position: Int) {
        val item = dataSet[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

}
