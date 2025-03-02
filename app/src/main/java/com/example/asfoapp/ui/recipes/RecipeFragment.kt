package com.example.asfoapp.ui.recipes

import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.core.content.res.ResourcesCompat
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

    private var ingredientsAdapter: IngredientsAdapter? = null

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
        initSeekBar()

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setContentView() {
        recipe?.let { recipe ->
            binding.recipeTitle.text = recipe.title
            binding.portions.text = getString(R.string.portions, 1)
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
            binding.addToFavoritesButton.setOnClickListener {
                binding.addToFavoritesButton.isSelected = !binding.addToFavoritesButton.isSelected
            }
        }
    }

    private fun initRecycler() {
        recipe?.let { recipe ->
            ingredientsAdapter = IngredientsAdapter(recipe.ingredients)
            val methodAdapter = MethodAdapter(recipe.method)
            val divider = MaterialDividerItemDecoration(
                requireContext(), VERTICAL
            ).apply {
                setDividerInsetEndResource(requireContext(), R.dimen.spacing_medium_8dp)
                setDividerInsetStartResource(requireContext(), R.dimen.spacing_medium_8dp)
                setDividerColorResource(requireContext(), R.color.figma_gray_light)
                isLastItemDecorated = false
            }
            binding.rvIngredients.adapter = ingredientsAdapter
            binding.rvIngredients.addItemDecoration(divider)
            binding.rvMethod.adapter = methodAdapter
            binding.rvMethod.addItemDecoration(divider)
        }
    }

    private fun initSeekBar() {
        binding.seekBar.apply {
            min = 1
            max = 10
            progress = 1
            setOnSeekBarChangeListener(
                object : SeekBar.OnSeekBarChangeListener {
                    override fun onProgressChanged(seekBar: SeekBar?, newProgress: Int, fromUser: Boolean) {
                            binding.portions.text = getString(R.string.portions, newProgress)
                            ingredientsAdapter?.updateIngredientsQuantity(newProgress)
                    }
                    override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                    override fun onStopTrackingTouch(seekBar: SeekBar?) {}
                }
            )
        }
    }
}
