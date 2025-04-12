package com.example.asfoapp.ui.recipes.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.fragment.app.viewModels
import com.example.asfoapp.R
import com.example.asfoapp.model.Recipe
import com.example.asfoapp.databinding.FragmentFavoritesBinding
import com.example.asfoapp.interfaces.OnItemClickListener
import com.example.asfoapp.ui.recipes.ARG_RECIPE_ID
import com.example.asfoapp.ui.recipes.recipe.RecipeFragment
import com.example.asfoapp.ui.recipes.RecipesListAdapter

class FavoritesFragment : Fragment() {
    private var _binding: FragmentFavoritesBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("binding for FavoritesFragment must not be null")
    private val viewModel: FavoritesViewModel by viewModels()
    private var recipesListAdapter: RecipesListAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        viewModel.favoritesState.observe(viewLifecycleOwner) { newState ->
            initUi(newState)
        }
        viewModel.loadRecipes()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initUi(state: FavoritesViewModel.FavoritesState) {
            handleRecyclerVisibleStatus(state.favoritesRecipes)
            recipesListAdapter?.setData(state.favoritesRecipes)
    }

    private fun initAdapter() {
        recipesListAdapter = RecipesListAdapter().apply {
            setOnItemClickListener(
                object : OnItemClickListener {
                    override fun onItemClick(itemId: Int) {
                        openRecipeByRecipeId(itemId)
                    }
                }
            )
        }
        binding.rvRecipes.adapter = recipesListAdapter
    }

    private fun openRecipeByRecipeId(recipeId: Int) {
        val bundle = bundleOf(ARG_RECIPE_ID to recipeId)
        requireActivity().supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace<RecipeFragment>(R.id.mainContainer, args = bundle)
            addToBackStack("FavoritesFragment")
        }
    }

    private fun handleRecyclerVisibleStatus(recipes: List<Recipe>) {
        binding.apply {
            tvEmptyListStub.visibility = View.VISIBLE.takeIf { recipes.isEmpty() } ?: View.GONE
            rvRecipes.visibility = View.GONE.takeIf { recipes.isEmpty() } ?: View.VISIBLE
        }
    }

}