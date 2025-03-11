package com.example.asfoapp.ui.recipes.recipe

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager.VERTICAL
import com.example.asfoapp.R
import com.example.asfoapp.model.Recipe
import com.example.asfoapp.databinding.FragmentRecipeBinding
import com.example.asfoapp.ui.recipes.recipe.adapters.IngredientsAdapter
import com.example.asfoapp.ui.recipes.recipe.adapters.MethodAdapter
import com.example.asfoapp.ui.recipes.ARG_RECIPE
import com.google.android.material.divider.MaterialDividerItemDecoration

const val ASFOAPP_PREFS_FILE_KEY = "ASFOAPP_PREFS_FILE_KEY"
const val FAVORITES_PREFS_KEY = "FAVORITES_PREFS_KEY"

class RecipeFragment : Fragment() {
    private var _binding: FragmentRecipeBinding? = null
    private val binding
        get() =
            _binding ?: throw IllegalStateException("binding for RecipeFragment must not be null")
    private val viewModel: RecipeViewModel by viewModels()

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

        viewModel.recipeState.observe(viewLifecycleOwner) { state ->
            Log.i("LiveDataFragment", "${state.isFavorite}")
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
            binding.tvRecipeTitle.text = recipe.title
            binding.tvPortions.text = getString(R.string.portions, 1)
            try {
                val inputStream = requireContext().assets.open(recipe.imageUrl)
                val image = Drawable.createFromStream(inputStream, null)
                binding.ivRecipeImage.setImageDrawable(image)
            } catch (e: Exception) {
                val stackTrace = Log.getStackTraceString(e)
                Log.e(
                    "RecipesFragment",
                    "Image - ${recipe.imageUrl} not found in assets\n$stackTrace"
                )
            }

            binding.ibAddToFavoritesButton.isSelected =
                getFavorites().contains(recipe.id.toString())
            binding.ibAddToFavoritesButton.setOnClickListener {
                binding.ibAddToFavoritesButton.isSelected = toggleFavoriteState()
            }
        }
    }

    private fun initRecycler() {
        recipe?.let { recipe ->
            ingredientsAdapter = IngredientsAdapter(recipe.ingredients)
            val methodAdapter = MethodAdapter(recipe.method)
            binding.rvIngredients.adapter = ingredientsAdapter
            binding.rvMethod.adapter = methodAdapter
            context?.let { context ->
                val divider = MaterialDividerItemDecoration(
                    context, VERTICAL
                ).apply {
                    setDividerInsetEndResource(context, R.dimen.spacing_medium_8dp)
                    setDividerInsetStartResource(context, R.dimen.spacing_medium_8dp)
                    setDividerColorResource(context, R.color.figma_gray_light)
                    isLastItemDecorated = false
                }
                binding.rvIngredients.addItemDecoration(divider)
                binding.rvMethod.addItemDecoration(divider)
            }
        }
    }

    private fun initSeekBar() {
        binding.seekBar.apply {
            min = 1
            max = 10
            progress = 1
            setOnSeekBarChangeListener(
                object : SeekBar.OnSeekBarChangeListener {
                    override fun onProgressChanged(
                        seekBar: SeekBar?,
                        newProgress: Int,
                        fromUser: Boolean
                    ) {
                        binding.tvPortions.text = getString(R.string.portions, newProgress)
                        ingredientsAdapter?.updateIngredientsQuantity(newProgress)
                    }

                    override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                    override fun onStopTrackingTouch(seekBar: SeekBar?) {}
                }
            )
        }
    }

    private fun saveFavorites(recipesIdSet: Set<String>) {
        context?.let { context ->
            val sharedPreferences = context.getSharedPreferences(
                ASFOAPP_PREFS_FILE_KEY,
                Context.MODE_PRIVATE
            )
            with(sharedPreferences.edit()) {
                putStringSet(FAVORITES_PREFS_KEY, recipesIdSet)
                apply()
            }
        }
    }

    private fun getFavorites(): MutableSet<String> {
        val sharedPreferences = context?.getSharedPreferences(
            ASFOAPP_PREFS_FILE_KEY,
            Context.MODE_PRIVATE
        )
        return HashSet(
            sharedPreferences?.getStringSet(FAVORITES_PREFS_KEY, emptySet()) ?: emptySet()
        )
    }

    private fun toggleFavoriteState(): Boolean {
        val favoritesIds = getFavorites()
        recipe?.id?.let { id ->
            val isFavorite = favoritesIds.contains(id.toString())
            if (isFavorite) {
                favoritesIds.remove(id.toString())
                saveFavorites(favoritesIds.toSet())
            } else {
                favoritesIds.add(id.toString())
                saveFavorites(favoritesIds)
            }
            return !isFavorite
        }
        return false
    }
}
