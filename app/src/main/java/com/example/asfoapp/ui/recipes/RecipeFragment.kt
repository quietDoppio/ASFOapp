package com.example.asfoapp.ui.recipes

import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager.VERTICAL
import com.example.asfoapp.R
import com.example.asfoapp.data.Recipe
import com.example.asfoapp.databinding.FragmentRecipeBinding
import com.example.asfoapp.ui.recipes.adapters.IngredientsAdapter
import com.example.asfoapp.ui.recipes.adapters.MethodAdapter
import com.google.android.material.divider.MaterialDividerItemDecoration

class RecipeFragment : Fragment() {
    private var _binding: FragmentRecipeBinding? = null
    private val binding get() =
        _binding ?: throw IllegalStateException("binding for RecipeFragment must not be null")

    private var recipe: Recipe? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipeBinding.inflate(layoutInflater, container, false)
        val view = binding.root

        recipe = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requireArguments().getParcelable(ARG_RECIPE, Recipe::class.java)
        } else {
            requireArguments().getParcelable(ARG_RECIPE) as? Recipe
        }

        setContentView()
        initRecycler()
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setContentView() {
        recipe?.let { recipe ->
            binding.recipeTitle.text = recipe.title
            try {
                val inputStream = requireContext().assets.open(recipe.imageUrl)
                val image = Drawable.createFromStream(inputStream, null)
                binding.recipeImage.setImageDrawable(image)
            } catch (e: Exception) {
                val stackTrace = Log.getStackTraceString(e)
                Log.e(
                    "RecipesFragment",
                    "Image - ${recipe.imageUrl} not found in assets\n$stackTrace"
                )
            }
        }
    }

    private fun initRecycler() {
        recipe?.let {
            Log.i("initAdapter", "recipe.method - ${it.method}")
            val ingredientsAdapter = IngredientsAdapter(it.ingredients)
            val methodAdapter = MethodAdapter(it.method)
            val divider = MaterialDividerItemDecoration(
                requireContext(), VERTICAL
            ).apply {
                setDividerInsetEndResource(requireContext(), R.dimen.spacing_medium_8dp)
                setDividerInsetStartResource(requireContext(), R.dimen.spacing_medium_8dp)
                setDividerColorResource(requireContext(), R.color.figma_gray_light)
            }
            binding.rvIngredients.adapter = ingredientsAdapter
            binding.rvIngredients.addItemDecoration(divider)
            binding.rvMethod.adapter = methodAdapter
            binding.rvMethod.addItemDecoration(divider)
        }

    }
}
