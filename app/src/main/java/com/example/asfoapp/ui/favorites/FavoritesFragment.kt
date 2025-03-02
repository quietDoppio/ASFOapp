package com.example.asfoapp.ui.favorites

import android.content.Context
import android.content.SharedPreferences
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
import com.example.asfoapp.ui.favorites.adapter.FavoritesListAdapter
import com.example.asfoapp.ui.recipes.ARG_RECIPE
import com.example.asfoapp.ui.recipes.ID_SET_PREFS_KEY
import com.example.asfoapp.ui.recipes.RecipeFragment

class FavoritesFragment : Fragment() {
    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding
            ?: throw IllegalStateException("binding for FavoritesFragment must not be null")
    private var sharedPrefs: SharedPreferences? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        sharedPrefs = requireContext().getSharedPreferences(
            getString(R.string.recipe_id_set_preferences_key),
            Context.MODE_PRIVATE
        )
    }
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

    private fun initAdapter(){
        val recipesId = sharedPrefs?.getStringSet(ID_SET_PREFS_KEY, emptySet()) ?: emptySet()
        val recipes: List<Recipe> = recipesId.mapNotNull {
            STUB.getRecipeById(it.toInt(), STUB.getBurgerRecipes())
        }.toList()
        val favoritesAdapter = FavoritesListAdapter(recipes)
        favoritesAdapter.setOnItemClickListener(
            object : OnItemClickListener {
                override fun onItemClick(itemId: Int) {
                    openRecipeByRecipeId(itemId, recipes)
                }

            }
        )
        binding.rvRecipes.adapter = favoritesAdapter
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
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}