package com.example.asfoapp.ui.recipes.recipe

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager.VERTICAL
import com.example.asfoapp.R
import com.example.asfoapp.databinding.FragmentRecipeBinding
import com.example.asfoapp.ui.recipes.recipe.adapters.IngredientsAdapter
import com.example.asfoapp.ui.recipes.recipe.adapters.MethodAdapter
import com.example.asfoapp.ui.recipes.ARG_RECIPE_ID
import com.google.android.material.divider.MaterialDividerItemDecoration

const val ASFOAPP_PREFS_FILE_KEY = "ASFOAPP_PREFS_FILE_KEY"
const val FAVORITES_PREFS_KEY = "FAVORITES_PREFS_KEY"

class RecipeFragment : Fragment() {
    private var _binding: FragmentRecipeBinding? = null
    private val binding
        get() =
            _binding ?: throw IllegalStateException("binding for RecipeFragment must not be null")
    private val viewModel: RecipeViewModel by viewModels()

    private var ingredientsAdapter: IngredientsAdapter? = null
    private var methodAdapter: MethodAdapter? = null
    private var isSeekBarInit: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipeBinding.inflate(layoutInflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.recipeState.observe(viewLifecycleOwner) { newState ->
            initUi(newState)
            initRecycler(newState)
            initSeekBar(newState.portionsCount)
        }
        initItemDecorator()
        viewModel.loadRecipe(requireArguments().getInt(ARG_RECIPE_ID))
        binding.ibAddToFavoritesButton.setOnClickListener {
            viewModel.toggleFavoriteState()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initUi(recipeState: RecipeViewModel.RecipeState) {
        binding.tvRecipeTitle.text = recipeState.recipe?.title
        binding.tvPortions.text = getString(R.string.portions, recipeState.portionsCount)
        try {
            val inputStream =
                requireContext().assets.open(recipeState.recipe?.imageUrl ?: "burger.png")
            val image = Drawable.createFromStream(inputStream, null)
            binding.ivRecipeImage.setImageDrawable(image)
        } catch (e: Exception) {
            val stackTrace = Log.getStackTraceString(e)
            Log.e(
                "RecipesFragment",
                "Image - ${recipeState.recipe?.imageUrl} not found in assets\n$stackTrace"
            )
        }
        binding.ibAddToFavoritesButton.isSelected = recipeState.isFavorite
    }

    private fun initRecycler(recipeState: RecipeViewModel.RecipeState) {
        if (binding.rvIngredients.adapter == null || binding.rvMethod.adapter == null) {
            ingredientsAdapter = IngredientsAdapter(recipeState.recipe?.ingredients ?: emptyList())
            methodAdapter = MethodAdapter(recipeState.recipe?.method ?: emptyList())
            binding.rvIngredients.adapter = ingredientsAdapter
            binding.rvMethod.adapter = methodAdapter
        } else {
            ingredientsAdapter?.setData(recipeState.recipe?.ingredients ?: emptyList())
            methodAdapter?.setData(recipeState.recipe?.method ?: emptyList())
        }
    }

    private fun initItemDecorator() {
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

    private fun initSeekBar(newProgress: Int) {
        binding.seekBar.progress = newProgress
        if (!isSeekBarInit) {
            binding.seekBar.apply {
                min = 1
                max = 10
                setOnSeekBarChangeListener(
                    object : SeekBar.OnSeekBarChangeListener {
                        override fun onProgressChanged(
                            seekBar: SeekBar?,
                            progress: Int,
                            fromUser: Boolean
                        ) {
                            binding.tvPortions.text = getString(R.string.portions, progress)
                            ingredientsAdapter?.updateIngredientsQuantity(progress)
                        }
                        override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                        override fun onStopTrackingTouch(seekBar: SeekBar?) {
                            viewModel.setPortionsCount(seekBar?.progress ?: newProgress)
                        }
                    }
                )
            }
            isSeekBarInit = true
        }

    }
}