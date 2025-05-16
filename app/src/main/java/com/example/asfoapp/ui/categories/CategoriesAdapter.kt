package com.example.asfoapp.ui.categories

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.bumptech.glide.Glide
import com.example.asfoapp.Constants
import com.example.asfoapp.R
import com.example.asfoapp.model.Category
import com.example.asfoapp.databinding.ItemCategoryBinding
import com.example.asfoapp.interfaces.OnItemClickListener

class CategoriesAdapter(dataSet: List<Category> = emptyList()) :
    Adapter<CategoriesAdapter.CategoryItemViewHolder>() {

    private var dataSet: List<Category> = dataSet
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    private var itemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }

    fun setData(data: List<Category>) {
        if (dataSet != data) dataSet = data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemCategoryBinding.inflate(layoutInflater, parent, false)
        return CategoryItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryItemViewHolder, position: Int) {
        val item = dataSet[position]
        holder.bind(item, itemClickListener)
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    class CategoryItemViewHolder(private val binding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Category, itemClickListener: OnItemClickListener?) {
            Glide.with(binding.root)
                .load("${Constants.BASE_URL}images/${item.imageUrl}")
                .error(R.drawable.img_error)
                .placeholder(R.drawable.img_placeholder)
                .into(binding.cardViewImage)
            binding.cardViewTitle.text = item.title
            binding.cardViewDescription.text = item.description

            binding.root.setOnClickListener {
                itemClickListener?.onItemClick(item.id)
            }
        }
    }

}
