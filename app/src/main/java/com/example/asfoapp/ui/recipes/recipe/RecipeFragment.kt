package com.example.asfoapp.ui.recipes.recipe

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager.VERTICAL
import com.example.asfoapp.R
import com.example.asfoapp.databinding.FragmentRecipeBinding
import com.example.asfoapp.ui.recipes.recipe.adapters.IngredientsAdapter
import com.example.asfoapp.ui.recipes.recipe.adapters.MethodAdapter
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
    private val navArgs: RecipeFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipeBinding.inflate(layoutInflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapters()
        initSeekBar()
        initItemDecorator()
        binding.ibAddToFavoritesButton.setOnClickListener {
            viewModel.toggleFavoriteState()
        }
        viewModel.recipeState.observe(viewLifecycleOwner) { newState ->
            initUi(newState)
        }

        viewModel.loadRecipe(navArgs.recipeId)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initUi(recipeState: RecipeViewModel.RecipeState) {
        recipeState.recipeImage?.let { binding.ivRecipeImage.setImageDrawable(it) }
        binding.tvRecipeTitle.text = recipeState.recipe?.title
        binding.ibAddToFavoritesButton.isSelected = recipeState.isFavorite
        binding.seekBar.progress = recipeState.portionsCount
        binding.tvPortions.text = getString(R.string.portions, recipeState.portionsCount)
        ingredientsAdapter?.let {
            it.setData(recipeState.recipe?.ingredients ?: emptyList())
            it.updateIngredientsQuantity(recipeState.portionsCount)
        }
        methodAdapter?.setData(recipeState.recipe?.method ?: emptyList())
    }

    private fun initAdapters() {
        ingredientsAdapter = IngredientsAdapter()
        methodAdapter = MethodAdapter()
        binding.rvIngredients.adapter = ingredientsAdapter
        binding.rvMethod.adapter = methodAdapter
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

    private fun initSeekBar() {
        binding.seekBar.apply {
            min = 1
            max = 10
            setOnSeekBarChangeListener(
                PortionsSeekBarListener { it: Int -> viewModel.setPortionsCount(it) }
            )
        }
    }
}

class PortionsSeekBarListener(val onChangePortions: (Int) -> Unit) :
    SeekBar.OnSeekBarChangeListener {
    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        onChangePortions(progress)
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {}
    override fun onStopTrackingTouch(seekBar: SeekBar?) {}
}