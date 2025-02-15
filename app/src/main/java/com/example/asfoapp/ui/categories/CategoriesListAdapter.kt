package com.example.asfoapp.ui.categories

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.asfoapp.R
import com.example.asfoapp.data.Category
import com.example.asfoapp.databinding.ItemCategoryBinding

class CategoriesListAdapter(private val dataSet: List<Category>) :
    Adapter<CategoriesListAdapter.CategoryItemViewHolder>() {

    class CategoryItemViewHolder(view: View) :
        RecyclerView.ViewHolder(view) {
        private val binding = ItemCategoryBinding.bind(view)

        companion object {
            fun inflateFrom(parent: ViewGroup): CategoryItemViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.item_category, parent, false)
                return CategoryItemViewHolder(view)
            }
        }

        fun bind(item: Category) {
            binding.cardViewTitle.text = item.title
            binding.cardViewDescription.text = item.description
            try {
                val inputStream = itemView.context.assets.open(item.imageUrl)
                val image = Drawable.createFromStream(inputStream, null)
                binding.cardViewImage.setImageDrawable(image)
            } catch (e: Exception) {
                val stackTrace = Log.getStackTraceString(e)
                Log.e(
                    "CategoriesListAdapter",
                    "Image - ${item.imageUrl} not found in assets\n$stackTrace"
                )
                binding.cardViewImage.setImageResource(R.drawable.img_error)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryItemViewHolder {
        val holder = CategoryItemViewHolder.inflateFrom(parent)
        return holder
    }

    override fun onBindViewHolder(holder: CategoryItemViewHolder, position: Int) {
        val item = dataSet[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

}
