package com.example.asfoapp.ui.categories.recipes

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.example.asfoapp.R
import com.example.asfoapp.data.Recipe
import com.example.asfoapp.data.STUB
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

        setInfo()
        initRecycler()

        return view
    }

    private fun initRecycler() {
        categoryId?.let { categoryId ->
            val recipesList = STUB.getRecipesByCategoryId(categoryId)
            val adapter = RecipesListAdapter(recipesList)
            adapter.setOnItemClickListener(
                object : RecipesListAdapter.OnItemClickListener {
                    override fun onItemClick(recipeId: Int) {

                        openRecipeByRecipeId(recipesList, recipeId)
                    }
                }
            )
            binding.rvRecipes.adapter = adapter
        }

    }
    private fun openRecipeByRecipeId(recipesList: List<Recipe>, recipeId: Int){
        val recipe = recipesList.find { it.id == recipeId }

        requireActivity().supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace<RecipeFragment>(R.id.mainContainer)
            addToBackStack("RecipesFragment")
        }
    }

    private fun setInfo() {
        binding.categoryName.text = categoryName
        categoryImageUrl?.let { imageUrl ->
            try {
                val inputStream = requireContext().assets.open(imageUrl)
                val image = Drawable.createFromStream(inputStream, null)
                binding.categoryImage.setImageDrawable(image)
            } catch (e: Exception) {
                val stackTrace = Log.getStackTraceString(e)
                Log.e(
                    "RecipesListFragment",
                    "Image - $imageUrl not found in assets\n$stackTrace"
                )
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

