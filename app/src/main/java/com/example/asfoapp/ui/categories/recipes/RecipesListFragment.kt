package com.example.asfoapp.ui.categories.recipes

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.asfoapp.databinding.FragmentRecipesListBinding

class RecipesListFragment : Fragment() {
    private var _binding: FragmentRecipesListBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("binding for RecipesListFragment must not be null")

    private var categoryId: Int? = null
    private var categoryName: String? = null
    private var categoryImageUrl: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRecipesListBinding.inflate(layoutInflater, container, false)
        val view = binding.root
        categoryId = requireArguments().getInt("ARG_CATEGORY_ID")
        categoryName = requireArguments().getString("ARG_CATEGORY_NAME")
        categoryImageUrl = requireArguments().getString("ARG_CATEGORY_IMAGE_URL")

        try {
            val inputStream = requireContext().assets.open(categoryImageUrl!!)
            val drawable = Drawable.createFromStream(inputStream, null)
            binding.categoryImage.setImageDrawable(drawable)
            binding.categoryName.text = categoryName
        } catch (e: Exception) {
            val stackTrace = Log.getStackTraceString(e)
            Log.e(
                "RecipesListFragment",
                "Image - $categoryImageUrl not found in assets\n$stackTrace"
            )
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

