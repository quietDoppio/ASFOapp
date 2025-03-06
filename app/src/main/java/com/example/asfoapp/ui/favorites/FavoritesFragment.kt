package com.example.asfoapp.ui.favorites

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.example.asfoapp.R
import com.example.asfoapp.data.Recipe
import com.example.asfoapp.data.STUB
import com.example.asfoapp.databinding.FragmentFavoritesBinding
import com.example.asfoapp.interfaces.OnItemClickListener
import com.example.asfoapp.ui.recipes.ARG_RECIPE
import com.example.asfoapp.ui.recipes.ASFOAPP_PREFS_FILE_KEY
import com.example.asfoapp.ui.recipes.FAVORITES_PREFS_KEY
import com.example.asfoapp.ui.recipes.RecipeFragment
import com.example.asfoapp.ui.recipes.adapters.RecipesListAdapter

class FavoritesFragment : Fragment() {
    private var _binding: FragmentFavoritesBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("binding for FavoritesFragment must not be null")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        val view = binding.root
        initAdapter()

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initAdapter() {
        val recipesIdSet = getFavoritesIds().map { it.toInt() }.toSet()
        val recipes: List<Recipe> = STUB.getRecipesByIds(recipesIdSet)
        val recipesListAdapter = RecipesListAdapter(recipes)
        recipesListAdapter.setOnItemClickListener(
            object : OnItemClickListener {
                override fun onItemClick(itemId: Int) {
                    openRecipeByRecipeId(itemId, recipes)
                }
            }
        )
        binding.rvRecipes.adapter = recipesListAdapter
        handleRecyclerVisibleStatus(recipes)
    }

    private fun openRecipeByRecipeId(recipeId: Int, recipesList: List<Recipe>) {
        val recipe = STUB.getRecipeById(recipeId, recipesList)
        val bundle = bundleOf(ARG_RECIPE to recipe)
        requireActivity().supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace<RecipeFragment>(R.id.mainContainer, args = bundle)
            addToBackStack("FavoritesFragment")
        }
    }

    private fun getFavoritesIds(): MutableSet<String> {
        val sharedPrefs = context?.getSharedPreferences(
            ASFOAPP_PREFS_FILE_KEY,
            Context.MODE_PRIVATE
        )
        return HashSet(sharedPrefs?.getStringSet(FAVORITES_PREFS_KEY, emptySet()) ?: emptySet())
    }

   private fun handleRecyclerVisibleStatus(recipes: List<Recipe>) {
        binding.apply {
            tvEmptyListStub.visibility = View.VISIBLE.takeIf { recipes.isEmpty() } ?: View.GONE
            rvRecipes.visibility = View.GONE.takeIf { recipes.isEmpty() } ?: View.VISIBLE
        }
    }

}