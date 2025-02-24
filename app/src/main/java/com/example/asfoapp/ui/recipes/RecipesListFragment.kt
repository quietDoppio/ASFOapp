package com.example.asfoapp.ui.recipes

import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.example.asfoapp.R
import com.example.asfoapp.data.Category
import com.example.asfoapp.data.Recipe
import com.example.asfoapp.data.STUB
import com.example.asfoapp.databinding.FragmentRecipesListBinding
import com.example.asfoapp.interfaces.OnItemClickListener
import com.example.asfoapp.ui.categories.ARG_CATEGORY
import com.example.asfoapp.ui.recipes.adapters.RecipesListAdapter

const val ARG_RECIPE = "ARG_RECIPE"

class RecipesListFragment : Fragment(){
    private var _binding: FragmentRecipesListBinding? = null
    private val binding get() =
        _binding ?: throw IllegalStateException("binding for RecipesListFragment must not be null")

    private var category: Category? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipesListBinding.inflate(layoutInflater, container, false)
        val view = binding.root
        category = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requireArguments().getParcelable(ARG_CATEGORY, Category::class.java)
        } else {
            requireArguments().getParcelable(ARG_CATEGORY) as? Category
        }

        setViewContent()
        initRecycler()

        return view
    }

    private fun initRecycler() {
        category?.id?.let { categoryId ->
            val recipesList = STUB.getRecipesByCategoryId(categoryId)
            val adapter = RecipesListAdapter(recipesList)
            adapter.setOnItemClickListener(
                object : OnItemClickListener {
                    override fun onItemClick(itemId: Int) {
                        openRecipeByRecipeId(itemId, recipesList,)
                    }
                }
            )
            binding.rvRecipes.adapter = adapter
        }

    }
    private fun openRecipeByRecipeId(recipeId: Int, recipesList: List<Recipe>,){
        val recipe = STUB.getRecipeById(recipeId, recipesList)
        val bundle = bundleOf(ARG_RECIPE to recipe)
        requireActivity().supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace<RecipeFragment>(R.id.mainContainer, args = bundle)
            addToBackStack("RecipesListFragment")
        }
    }

    private fun setViewContent() {
        category?.let { category ->
        binding.categoryName.text = category.title
            try {
                val inputStream = requireContext().assets.open(category.imageUrl)
                val image = Drawable.createFromStream(inputStream, null)
                binding.categoryImage.setImageDrawable(image)
            } catch (e: Exception) {
                val stackTrace = Log.getStackTraceString(e)
                Log.e(
                    "RecipesListFragment",
                    "Image - ${category.imageUrl} not found in assets\n$stackTrace"
                )
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

